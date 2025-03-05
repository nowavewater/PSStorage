package com.waldemartech.psstorage.data.api.entity

import com.waldemartech.psstorage.data.base.SharedConstants.EMPTY_STRING
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ProductResponse(
    @SerialName("id")
    val id : String = EMPTY_STRING,

    @SerialName("__typename")
    val typeName : String = EMPTY_STRING,

    @SerialName("name")
    val name : String = EMPTY_STRING,

    @SerialName("npTitleId")
    val titleId : String = EMPTY_STRING,

    @SerialName("localizedStoreDisplayClassification")
    val localizedClassification : String = EMPTY_STRING,

    @SerialName("storeDisplayClassification")
    val storeDisplayClassification : String = EMPTY_STRING,

    @SerialName("price")
    val price: SinglePrice,

    @SerialName("media")
    val mediaList: List<SingleMedia>,

    @SerialName("platforms")
    val platforms: List<String>
)

@Serializable
data class SinglePrice(
    @SerialName("__typename")
    val typeName : String = EMPTY_STRING,
    @SerialName("basePrice")
    val basePrice : String = EMPTY_STRING,
    @SerialName("discountedPrice")
    val discountedPrice : String = EMPTY_STRING,
    @SerialName("discountText")
    val discountText : String = EMPTY_STRING,
    @SerialName("isFree")
    val isFree : Boolean = false,
    @SerialName("isExclusive")
    val isExclusive : Boolean = false,
    @SerialName("isTiedToSubscription")
    val isTiedToSubscription : Boolean = false,
)

@Serializable
data class SingleMedia(
    @SerialName("__typename")
    val typeName : String = EMPTY_STRING,

    @SerialName("type")
    val type : String = EMPTY_STRING,

    @SerialName("url")
    val url : String = EMPTY_STRING,

    @SerialName("role")
    val role : String = EMPTY_STRING,
)