package com.waldemartech.psstorage.data.local.database.entity

import androidx.room.Embedded
import androidx.room.Relation
import com.waldemartech.psstorage.data.local.database.table.CurrentDeal
import com.waldemartech.psstorage.data.local.database.table.Deal

data class CurrentDealData(
    @Embedded val currentDeal: CurrentDeal,
    @Relation(
        parentColumn = "currentDealId",
        entityColumn = "dealId"
    )
    val deal: Deal
)
