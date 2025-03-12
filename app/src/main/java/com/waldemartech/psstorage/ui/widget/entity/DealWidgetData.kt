package com.waldemartech.psstorage.ui.widget.entity

data class DealWidgetData(
    val dealId: String,
    val imageUrl: String
)

data class ProductItemData(
    val productId: String,
    val salePriceText: String,
    val originalPriceText: String,
    val titleText: String,
    val percentOffText: String,
    val productTypeText: String,
    val imageUrl: String
)