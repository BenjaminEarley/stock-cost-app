package com.benjaminearley.stockcost.repository

import arrow.core.Either
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
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
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

    suspend fun getProducts(): Flow<List<Product>>
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
    ): Either<RepoError, Product> {
        val product = persistence.productDao().getProduct(securityId).first()

        return if (
            !forceUpdate &&
            product != null &&
            product.updatedAt > System.currentTimeMillis() - 2.hours.inMilliseconds
        ) {
            product.right()
        } else {
            downloadProduct(securityId)
        }
    }


    override suspend fun getProducts(): Flow<List<Product>> {
        return persistence.productDao().getProducts()
    }

    override suspend fun addNewProduct(productId: String): Either<RepoError, Unit> =
        downloadProduct(productId).void()

    private suspend fun downloadProduct(securityId: String) = Either
        .catch { network.getProduct(securityId) }
        .mapLeft { RepoError.NetworkError }
        .map { it.toProduct() }
        .map {
            persistence.productDao().updateProduct(it)
            it
        }
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