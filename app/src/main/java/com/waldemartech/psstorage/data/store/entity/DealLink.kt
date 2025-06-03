package com.waldemartech.psstorage.data.store.entity

import com.waldemartech.psstorage.data.base.SharedConstants.EMPTY_STRING
import com.waldemartech.psstorage.data.local.database.table.Deal

data class DealUpdateResult(
    val dealList: List<Deal>,
    val subDealList: List<SubDealData>
)

data class SubDealData(
    val imageUrl: String = EMPTY_STRING,
    val link: String = EMPTY_STRING,
    val target: String = EMPTY_STRING
)
