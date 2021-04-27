package com.benjaminearley.stockcost.repository

import arrow.core.Either
import arrow.core.ValidatedNel
import arrow.core.flatMap
import arrow.core.right
import com.benjaminearley.stockcost.data.Price
import com.benjaminearley.stockcost.data.Product
import com.benjaminearley.stockcost.repository.StockCostError.*
import com.benjaminearley.stockcost.repository.network.ProductService
import com.benjaminearley.stockcost.repository.network.data.NetworkPrice
import com.benjaminearley.stockcost.repository.network.data.NetworkProduct
import com.benjaminearley.stockcost.repository.persistence.ProductStore
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
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
    ): Either<StockCostError, Product>

    suspend fun getSavedProducts(): Flow<List<Product>>
    suspend fun updateAllProducts(): ValidatedNel<StockCostError, Unit>
    suspend fun addNewProduct(productId: String): Either<StockCostError, Unit>
    suspend fun deleteProduct(securityId: String): Either<StockCostError, Unit>
}

@Singleton
class ProductsRepositoryImpl @Inject constructor(
    private val network: ProductService,
    private val store: ProductStore
) : ProductsRepository {

    override suspend fun getProduct(
        securityId: String,
        forceUpdate: Boolean
    ): Either<StockCostError, Product> =
        if (forceUpdate) downloadProduct(securityId)
        else {
            val product = store.getProduct(securityId).orNull()
            if (product != null && !product.isStale()) product.right()
            else downloadProduct(securityId)
        }

    override suspend fun getSavedProducts(): Flow<List<Product>> = store.getProducts()

    override suspend fun updateAllProducts(): ValidatedNel<StockCostError, Unit> =
        TODO("Not yet implemented")

    override suspend fun addNewProduct(productId: String): Either<StockCostError, Unit> =
        downloadProduct(productId, newProductOnly = true).void()

    override suspend fun deleteProduct(securityId: String): Either<StockCostError, Unit> =
        Either
            .catch { store.deleteProduct(securityId) }
            .mapLeft { NotFound }
            .void()

    private suspend fun downloadProduct(securityId: String, newProductOnly: Boolean = false) =
        withContext(Dispatchers.IO) {
            Either
                .catch { network.getProduct(securityId) }
                .mapLeft { NetworkError }
                .map { it.toProduct() }
                .flatMap { product ->
                    Either
                        .catch {
                            if (newProductOnly) store.addProduct(product)
                            else store.updateProduct(product)
                        }
                        .mapLeft { DatabaseError }
                        .map { product }
                }
        }

    private fun Product.isStale() = updatedAt < System.currentTimeMillis() - 1.hours.inMilliseconds
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