package com.benjaminearley.stockcost.util

import com.benjaminearley.stockcost.data.Price
import com.benjaminearley.stockcost.data.Product
import com.benjaminearley.stockcost.repository.network.data.NetworkPrice
import com.benjaminearley.stockcost.repository.network.data.NetworkProduct

val NetworkProduct.Companion.randomizer: Randomizer<NetworkProduct>
    get() =
        { random ->
            NetworkProduct(
                symbol = String.randomizer(0, 5)(random),
                securityId = String.randomizer(1, 20)(random),
                displayName = String.randomizer(0, 20)(random),
                currentPrice = NetworkPrice.randomizer(random),
                closingPrice = NetworkPrice.randomizer(random)
            )
        }

val NetworkPrice.Companion.randomizer: Randomizer<NetworkPrice>
    get() =
        { random ->
            NetworkPrice(
                currency = String.randomizer(0, 5)(random),
                decimals = Int.randomizer(0, 5)(random),
                amount = String.randomizer(0, 10)(random),
            )
        }

fun Product.Companion.randomizer(
    from: Long = 0,
    until: Long = System.currentTimeMillis()
): Randomizer<Product> =
    { random ->
        Product(
            symbol = String.randomizer(0, 5)(random),
            securityId = String.randomizer(1, 20)(random),
            displayName = String.randomizer(0, 20)(random),
            currentPrice = Price.randomizer(random),
            closingPrice = Price.randomizer(random),
            updatedAt = Long.randomizer(from, until)(random)
        )
    }

val Price.Companion.randomizer: Randomizer<Price>
    get() =
        { random ->
            Price(
                currency = String.randomizer(0, 5)(random),
                decimals = Int.randomizer(0, 5)(random),
                amount = String.randomizer(0, 10)(random),
            )
        }