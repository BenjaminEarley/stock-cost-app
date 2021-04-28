package com.benjaminearley.stockcost

import arrow.core.Either
import arrow.core.right
import com.benjaminearley.stockcost.data.Product
import com.benjaminearley.stockcost.repository.ProductsRepositoryImpl
import com.benjaminearley.stockcost.repository.network.data.NetworkProduct
import com.benjaminearley.stockcost.util.randomizer
import com.benjaminearley.stockcost.util.sequenceRandomizer
import kotlinx.coroutines.runBlocking
import org.junit.Test
import kotlin.random.Random
import kotlin.time.days
import kotlin.time.hours

class ExampleUnitTest {

    @Test
    fun `Repo should use network when cache is stale`() {
        val currentTime = TestCurrentTime(10.days.toLongMilliseconds())

        val oldId = ""
        val network = TestProductService()
        val oldProduct = Product
            .randomizer().sequenceRandomizer()(Random(seed)).take(1).first().copy(
            securityId = oldId,
            updatedAt = currentTime.get() - 2.hours.toLongMilliseconds()
        )
        val store = TestProductStore(
            getProduct = { oldProduct.right() },
            updateProduct = { oldProduct.copy(securityId = "new").right() }
        )
        val repo = ProductsRepositoryImpl(network, store, currentTime)

        runBlocking {
            val product = repo.getProduct(oldId)
            assert(product.isRight())
            val right = product as Either.Right
            assert(right.value.securityId != oldId) {
                "Old cache should not be used when product is 2 hours old"
            }
        }
    }

    @Test
    fun `Repo should use cache when valid else network`() = runBlocking {
        val currentTime = TestCurrentTime(10.days.toLongMilliseconds())

        Product
            .randomizer(currentTime.get() - 3.days.toLongMilliseconds(), currentTime.get())
            .sequenceRandomizer()(Random(seed)).take(1_000)
            .zip(NetworkProduct.randomizer.sequenceRandomizer()(Random(seed * 7)).take(1_000))
            .filter { (a, b) -> a.securityId != b.securityId } // filtering race condition
            .forEach { (product, networkProduct) ->
                val network = TestProductService(getProduct = { networkProduct })
                val store = TestProductStore(
                    getProduct = { product.right() },
                    updateProduct = { product.copy(securityId = networkProduct.securityId).right() }
                )
                val repo = ProductsRepositoryImpl(network, store, currentTime)
                val repoProduct = repo.getProduct("")
                val new = (repoProduct as Either.Right).value

                if (product.updatedAt >= currentTime.get() - 1.hours.toLongMilliseconds()) {
                    assert(new.securityId == product.securityId)
                } else {
                    assert(new.securityId == networkProduct.securityId)
                }
            }
    }

    companion object {
        private const val seed: Long = 1
    }
}

