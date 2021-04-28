package com.benjaminearley.stockcost

import arrow.core.Either
import com.benjaminearley.stockcost.data.Product
import com.benjaminearley.stockcost.repository.StockCostError
import com.benjaminearley.stockcost.repository.persistence.ProductStore
import com.benjaminearley.stockcost.util.*
import kotlinx.coroutines.flow.Flow
import kotlin.random.Random

class TestProductStore(
    private val getProduct: (() -> Either<StockCostError, Product>)? = null,
    private val getProducts: (() -> Flow<List<Product>>)? = null,
    private val updateProduct: (() -> Either<StockCostError, Product>)? = null,
    private val addProduct: (() -> Either<StockCostError, Product>)? = null,
    private val deleteProduct: (() -> Either<StockCostError, Unit>)? = null
) : ProductStore {

    private val random = Random(seed)

    override suspend fun getProduct(securityId: String): Either<StockCostError, Product> =
        getProduct?.invoke() ?: Product
            .randomizer()
            .eitherRandomizer(StockCostError.NetworkError)
            .sequenceRandomizer()(random)
            .take(1)
            .first()


    override suspend fun getProducts(): Flow<List<Product>> =
        getProducts?.invoke() ?: Product
            .randomizer()
            .listRandomizer(0, 20)
            .flowRandomizer(0, 5, delay = 500)
            .sequenceRandomizer()(random)
            .take(1)
            .first()

    override suspend fun updateProduct(product: Product): Either<StockCostError, Product> =
        updateProduct?.invoke() ?: Product
            .randomizer()
            .eitherRandomizer(StockCostError.NetworkError)
            .sequenceRandomizer()(random)
            .take(1)
            .first()
            .map { product }

    override suspend fun addProduct(product: Product): Either<StockCostError, Product> =
        addProduct?.invoke() ?: Product
            .randomizer()
            .eitherRandomizer(StockCostError.NetworkError)
            .sequenceRandomizer()(random)
            .take(1)
            .first()
            .map { product }

    override suspend fun deleteProduct(securityId: String): Either<StockCostError, Unit> =
        deleteProduct?.invoke() ?: Product
            .randomizer()
            .eitherRandomizer(StockCostError.NetworkError)
            .sequenceRandomizer()(random)
            .take(1)
            .first()
            .void()

    companion object {
        private const val seed: Long = 4321
    }
}