package com.waldemartech.psstorage.data.local.database.table

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "all_time_low_price")
data class AllTimeLowPrice(
    @PrimaryKey
    val productIdInLowPrice: String,
    val lowPrice: Float,
    val storeIdInLowPrice: String,
)
