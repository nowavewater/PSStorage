package com.waldemartech.psstorage.data.local.database.entity

import androidx.room.Embedded
import com.waldemartech.psstorage.data.local.database.table.PriceHistory
import com.waldemartech.psstorage.data.local.database.table.Product

data class ProductPriceInDeal(
    @Embedded val product: Product,
    @Embedded val priceHistory: PriceHistory
)
