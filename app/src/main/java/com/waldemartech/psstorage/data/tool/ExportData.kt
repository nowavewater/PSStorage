package com.waldemartech.psstorage.data.tool

import com.waldemartech.psstorage.data.local.database.ProductConstants.PRODUCT_ID_KEY
import com.waldemartech.psstorage.data.store.StoreConstants.STORE_ID_KEY
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ExportData (
    @SerialName("favorites")
    val favorites: List<ExportProduct>,

    @SerialName("ignores")
    val ignores: List<ExportProduct>
)

@Serializable
data class ExportProduct(
    @SerialName(PRODUCT_ID_KEY)
    val productID: String,

    @SerialName(STORE_ID_KEY)
    val storeId: String
)