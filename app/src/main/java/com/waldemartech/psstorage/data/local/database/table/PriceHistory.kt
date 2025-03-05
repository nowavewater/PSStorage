package com.waldemartech.psstorage.data.local.database.table

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "price_history",
    indices = [Index(value = ["productIdPriceRef","dealIdPriceRef"], unique = true)]
)
data class PriceHistory(
    val productIdPriceRef: String,
    val dealIdPriceRef: String,
    val unit: String,
    val basePrice: Float,
    val discountedPrice: Float,
    val discountText: String,
    val isFree: Boolean,
    val isExclusive: Boolean,
    val isTiedToSubscription: Boolean,
    val storeIdInPriceHistory: String

)  {
    @PrimaryKey(autoGenerate = true)
    var priceHistoryId: Long = 0L
}
