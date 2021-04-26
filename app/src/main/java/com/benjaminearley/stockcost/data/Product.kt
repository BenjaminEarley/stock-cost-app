package com.benjaminearley.stockcost.data

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "products")
data class Product(
    @ColumnInfo(name = "security_id")
    @PrimaryKey
    val securityId: String,
    @ColumnInfo(name = "symbol")
    val symbol: String,
    @ColumnInfo(name = "display_name")
    val displayName: String,
    @ColumnInfo(name = "updated_at")
    val updatedAt: Long,
    @Embedded(prefix = "current_price_")
    val currentPrice: Price,
    @Embedded(prefix = "closing_price_")
    val closingPrice: Price
) : Parcelable