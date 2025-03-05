package com.waldemartech.psstorage.data.api.entity

import com.waldemartech.psstorage.data.base.SharedConstants.EMPTY_STRING
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DealResponse(
    @SerialName("imageUrl")
    val imageUrl : String = EMPTY_STRING,

    @SerialName("link")
    val link: DealLink
)

@Serializable
data class DealLink(
    @SerialName("type")
    val type : String = EMPTY_STRING,

    @SerialName("target")
    val target : String = EMPTY_STRING,

    @SerialName("localizedName")
    val localizedName: String? = null
)