package com.waldemartech.psstorage.data.store

object StoreConstants {

    const val STORE_ID_KEY = "store_id"
    const val DEAL_ID_KEY = "deal_id"
    const val PAGE_INDEX_KEY = "page_index"



    const val HK_STORE_ID = "zh-hans-hk"

    const val US_STORE_ID = "en-us"

    fun excludeLocalizedName() = setOf(
        "cat.gma.PlayStation_VR2_Games",
        "cat.gma.AllDeals",
        "cat.gma.x_All_PS5_games",
        "cat.gma.x_Free_to_play",
        "cat.gma.PS5_Pro_Enhanced_Games",
        "cat.gma.x_All_PS4_games",
        "cat.gma.AddonsByGame"
    )

}