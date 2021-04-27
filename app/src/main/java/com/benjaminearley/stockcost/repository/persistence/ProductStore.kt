package com.benjaminearley.stockcost.repository.persistence

import arrow.core.Either
import com.benjaminearley.stockcost.data.Product
import com.benjaminearley.stockcost.repository.StockCostError
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class ProductStoreModule {
    @Binds
    abstract fun bindProductStore(productStore: ProductStoreImpl): ProductStore
}

interface ProductStore {
    suspend fun getProduct(securityId: String): Either<StockCostError, Product>
    suspend fun getProducts(): Flow<List<Product>>
    suspend fun updateProduct(product: Product): Either<StockCostError, Product>
    suspend fun addProduct(product: Product): Either<StockCostError, Product>
    suspend fun deleteProduct(securityId: String): Either<StockCostError, Unit>
}

@Singleton
class ProductStoreImpl @Inject constructor(private val persistence: StockCostDatabase) :
    ProductStore {
    override suspend fun getProduct(securityId: String): Either<StockCostError, Product> =
        withContext(Dispatchers.IO) {
            Either
                .catch { persistence.productDao().getProduct(securityId) }
                .mapLeft { StockCostError.DatabaseError }
        }

    override suspend fun getProducts(): Flow<List<Product>> =
        withContext(Dispatchers.IO) { persistence.productDao().getProducts() }

    override suspend fun updateProduct(product: Product): Either<StockCostError, Product> =
        withContext(Dispatchers.IO) {
            Either
                .catch { persistence.productDao().updateProduct(product) }
                .mapLeft { StockCostError.DatabaseError }
                .map { product }
        }

    override suspend fun addProduct(product: Product): Either<StockCostError, Product> =
        withContext(Dispatchers.IO) {
            Either
                .catch { persistence.productDao().addProduct(product) }
                .mapLeft { StockCostError.DatabaseError }
                .map { product }
        }

    override suspend fun deleteProduct(securityId: String): Either<StockCostError, Unit> =
        withContext(Dispatchers.IO) {
            Either
                .catch { persistence.productDao().deleteProductById(securityId) }
                .mapLeft { StockCostError.NotFound }
                .void()
        }
}