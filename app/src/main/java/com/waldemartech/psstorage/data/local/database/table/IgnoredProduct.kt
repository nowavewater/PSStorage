package com.waldemartech.psstorage.data.local.database.table

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "ignored_product")
data class IgnoredProduct(
    @PrimaryKey val ignoredProductId: String,
    val storeIdInIgnoredProduct: String

)
