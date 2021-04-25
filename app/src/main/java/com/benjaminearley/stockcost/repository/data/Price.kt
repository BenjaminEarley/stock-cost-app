package com.benjaminearley.stockcost.repository.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Parcelize
@Serializable
data class Price(
    @SerialName("currency")
    val currency: String,
    @SerialName("decimals")
    val decimals: Int,
    @SerialName("amount")
    val amount: String
) : Parcelable
