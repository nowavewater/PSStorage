package com.waldemartech.psstorage.data.local.database.table

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_product")
data class FavoriteProduct(
    @PrimaryKey val favoriteProductId: String,
    val storeIdInFavoriteProduct: String

)
