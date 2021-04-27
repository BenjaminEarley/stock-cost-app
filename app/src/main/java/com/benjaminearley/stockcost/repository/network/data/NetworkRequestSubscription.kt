package com.benjaminearley.stockcost.repository.network.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Parcelize
@Serializable
data class NetworkRequestSubscription(
    @SerialName("subscribeTo")
    val subscribeTo: List<String>? = null,
    @SerialName("unsubscribeFrom")
    val unsubscribeFrom: List<String>? = null
) : Parcelable