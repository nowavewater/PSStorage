package com.waldemartech.psstorage.ui.widget.entity

data class DealWidgetData(
    val dealId: String,
    val imageUrl: String
)

data class ProductPriceItemData(
    val productId: String,
    val salePriceText: String,
    val originalPriceText: String,
    val titleText: String,
    val percentOffText: String,
    val classificationText: String,
    val imageUrl: String,
    val platformList: List<String>
)

data class ProductItemData(
    val productId: String,
    val titleText: String,
    val classificationText: String,
    val imageUrl: String,
    val platformList: List<String>
)