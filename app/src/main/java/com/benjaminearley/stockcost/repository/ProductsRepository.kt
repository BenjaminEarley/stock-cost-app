package com.benjaminearley.stockcost.repository

import arrow.core.Either
import arrow.core.ValidatedNel
import arrow.core.right
import com.benjaminearley.stockcost.data.Price
import com.benjaminearley.stockcost.data.Product
import com.benjaminearley.stockcost.repository.network.ProductService
import com.benjaminearley.stockcost.repository.network.data.NetworkPrice
import com.benjaminearley.stockcost.repository.network.data.NetworkProduct
import com.benjaminearley.stockcost.repository.persistence.StockCostDatabase
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.time.hours

@Module
@InstallIn(SingletonComponent::class)
abstract class ProductsRepositoryModule {
    @Binds
    abstract fun bindProductsRepository(productsRepository: ProductsRepositoryImpl): ProductsRepository
}

interface ProductsRepository {
    suspend fun getProduct(
        securityId: String,
        forceUpdate: Boolean = false
    ): Either<RepoError, Product>

    suspend fun getSavedProducts(): Flow<List<Product>>
    suspend fun updateAllProducts(): ValidatedNel<RepoError, Unit>
    suspend fun addNewProduct(productId: String): Either<RepoError, Unit>
}

@Singleton
class ProductsRepositoryImpl @Inject constructor(
    private val network: ProductService,
    private val persistence: StockCostDatabase
) : ProductsRepository {

    override suspend fun getProduct(
        securityId: String,
        forceUpdate: Boolean
    ): Either<RepoError, Product> = withContext(Dispatchers.IO) {
        val product = persistence.productDao().getProduct(securityId).first()

        if (!forceUpdate && product != null && !product.isStale()) product.right()
        else downloadProduct(securityId)
    }


    override suspend fun getSavedProducts(): Flow<List<Product>> =
        withContext(Dispatchers.IO) { persistence.productDao().getProducts() }

    override suspend fun updateAllProducts(): ValidatedNel<RepoError, Unit> {
        TODO("Not yet implemented")
    }

    override suspend fun addNewProduct(productId: String): Either<RepoError, Unit> =
        withContext(Dispatchers.IO) { downloadProduct(productId).void() }

    private suspend fun downloadProduct(securityId: String) = withContext(Dispatchers.IO) {
        Either
            .catch { network.getProduct(securityId) }
            .mapLeft { RepoError.NetworkError }
            .map { it.toProduct() }
            .map {
                persistence.productDao().updateProduct(it)
                it
            }
    }

    private fun Product.isStale() = updatedAt < System.currentTimeMillis() - 2.hours.inMilliseconds
}

sealed class RepoError {
    object NetworkError : RepoError()
}

private fun NetworkProduct.toProduct() = Product(
    securityId = securityId,
    symbol = symbol,
    displayName = displayName,
    updatedAt = System.currentTimeMillis(),
    currentPrice = currentPrice.toPrice(),
    closingPrice = closingPrice.toPrice()
)

private fun NetworkPrice.toPrice() = Price(currency, decimals, amount)