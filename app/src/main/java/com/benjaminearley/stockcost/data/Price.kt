package com.benjaminearley.stockcost.data

import android.os.Parcelable
import androidx.room.ColumnInfo
import kotlinx.parcelize.Parcelize

@Parcelize
data class Price(
    @ColumnInfo(name = "currency")
    val currency: String,
    @ColumnInfo(name = "decimals")
    val decimals: Int,
    @ColumnInfo(name = "amount")
    val amount: String
) : Parcelable