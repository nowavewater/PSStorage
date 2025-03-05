package com.waldemartech.psstorage.data.local.database.table

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "current_deal")
data class CurrentDeal(
    @PrimaryKey val currentDealId: String,
    val storeIdInCurrentDeal: String
)
