package com.waldemartech.psstorage.data.local.database.table

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "product")
data class Product(
    @PrimaryKey
    val productId : String,
    val name : String,
    val typeName : String,
    val npTitleId: String,
    val imageUrl: String,
    val localizedDisplayClassification: String,
    val storeDisplayClassification: String,
    val storeIdInProduct: String

)
