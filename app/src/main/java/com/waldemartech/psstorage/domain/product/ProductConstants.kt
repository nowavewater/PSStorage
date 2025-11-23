package com.waldemartech.psstorage.domain.product

import com.waldemartech.psstorage.data.local.database.entity.ProductDetailData
import com.waldemartech.psstorage.ui.widget.entity.ProductItemData

object ProductConstants {

    fun ProductDetailData.toProductItemData(): ProductItemData {
        return ProductItemData(
            productId = product.productId,
            titleText = product.name,
            classificationText = product.localizedDisplayClassification,
            imageUrl = product.imageUrl,
            platformList = platforms.map { it.name }
        )
    }

}