package com.benjaminearley.stockcost

import com.benjaminearley.stockcost.repository.network.ProductService
import com.benjaminearley.stockcost.repository.network.data.NetworkProduct
import com.benjaminearley.stockcost.util.randomizer
import com.benjaminearley.stockcost.util.sequenceRandomizer
import kotlin.random.Random

class TestProductService(
    private val getProduct: (() -> NetworkProduct)? = null
) : ProductService {

    private val generated = NetworkProduct.randomizer.sequenceRandomizer()(Random(seed))

    override suspend fun getProduct(productId: String): NetworkProduct =
        getProduct?.invoke() ?: generated.take(1).first()

    companion object {
        private const val seed: Long = 1234
    }
}