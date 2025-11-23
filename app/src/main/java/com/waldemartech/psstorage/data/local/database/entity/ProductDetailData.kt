package com.waldemartech.psstorage.data.local.database.entity

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.waldemartech.psstorage.data.base.SharedConstants.UNDER_SCORE
import com.waldemartech.psstorage.data.local.database.table.Platform
import com.waldemartech.psstorage.data.local.database.table.Product
import com.waldemartech.psstorage.data.local.database.table.ProductPlatformCrossRef

data class ProductDetailData(
    @Embedded val product: Product,
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
