package com.waldemartech.psstorage.data.local.database.table

import androidx.room.Entity

@Entity(primaryKeys = ["productId", "platformId"])
data class ProductPlatformCrossRef(
    val productId: String,
    val platformId: Long
)