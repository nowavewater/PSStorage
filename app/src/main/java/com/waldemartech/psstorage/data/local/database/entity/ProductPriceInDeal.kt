package com.waldemartech.psstorage.data.local.database.entity

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.waldemartech.psstorage.data.local.database.table.Platform
import com.waldemartech.psstorage.data.local.database.table.PriceHistory
import com.waldemartech.psstorage.data.local.database.table.Product
import com.waldemartech.psstorage.data.local.database.table.ProductPlatformCrossRef

data class ProductPriceInDeal(
    @Embedded val product: Product,
    @Embedded val priceHistory: PriceHistory,
    @Relation(
        parentColumn = "productId",
        entity = Platform::class,
        entityColumn = "platformId",
        associateBy = Junction(
            value = ProductPlatformCrossRef::class,
            parentColumn = "productId",
            entityColumn = "platformId"
        )
    )
    val platforms: List<Platform>
)
