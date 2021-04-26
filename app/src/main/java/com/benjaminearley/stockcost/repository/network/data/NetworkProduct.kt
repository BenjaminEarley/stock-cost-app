package com.benjaminearley.stockcost.repository.network.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Parcelize
@Serializable
data class NetworkProduct(
    @SerialName("symbol")
    val symbol: String,
    @SerialName("securityId")
    val securityId: String,
    @SerialName("displayName")
    val displayName: String,
    @SerialName("currentPrice")
    val currentPrice: NetworkPrice,
    @SerialName("closingPrice")
    val closingPrice: NetworkPrice
) : Parcelable
