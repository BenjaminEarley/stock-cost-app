package com.benjaminearley.stockcost.repository.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Parcelize
@Serializable
data class Product(
    @SerialName("symbol")
    val symbol: String,
    @SerialName("securityId")
    val securityId: String,
    @SerialName("displayName")
    val displayName: String,
    @SerialName("currentPrice")
    val currentPrice: Price,
    @SerialName("closingPrice")
    val closingPrice: Price
) : Parcelable
