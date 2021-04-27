package com.benjaminearley.stockcost.repository.network.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Parcelize
@Serializable
data class NetworkSubscription(
    @SerialName("t")
    val type: String,
    @SerialName("body")
    val body: NetworkTradingQuoteBody? = null
) : Parcelable

@Parcelize
@Serializable
data class NetworkTradingQuoteBody(
    @SerialName("securityId")
    val securityId: String? = null,
    @SerialName("currentPrice")
    val currentPrice: String? = null
) : Parcelable