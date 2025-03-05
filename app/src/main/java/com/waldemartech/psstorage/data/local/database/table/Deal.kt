package com.waldemartech.psstorage.data.local.database.table

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "deal")
data class Deal(
    @PrimaryKey val dealId: String,
    val imageUrl: String,
    val localizedName: String,
    val storeIdInDeal: String
)



