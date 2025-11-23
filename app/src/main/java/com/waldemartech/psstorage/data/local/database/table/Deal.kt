package com.waldemartech.psstorage.data.local.database.table

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.waldemartech.psstorage.data.base.SharedConstants.EMPTY_STRING


@Entity(tableName = "deal")
data class Deal(
    @PrimaryKey val dealId: String,
    val imageUrl: String,
    val currentPageIndex: Int = 1,
    val localizedName: String = EMPTY_STRING,
    val storeIdInDeal: String
)



