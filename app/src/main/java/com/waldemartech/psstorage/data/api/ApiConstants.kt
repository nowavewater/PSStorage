package com.waldemartech.psstorage.data.api

object ApiConstants {

    const val HTML_TAG_SCRIPT = "script"

    const val HTML_TAG_A  = "a"

    const val HTML_TAG_DIV = "div"

    const val HTML_ATTRIBUTE_NAME_CLASS = "class"

    const val HTML_ATTRIBUTE_NAME_ID = "id"

    const val HTML_ATTRIBUTE_NAME_HREF = "href"

    const val HTML_ATTRIBUTE_NAME_DATA_TARGET = "data-target"

    const val PS_STORE_DEAL_LINK_CLASS = "psw-link psw-content-link"

    const val PS_STORE_DEAL_ALLY_INDICATOR_CLASS = "psw-l-line-center psw-ally-indicator psw-b-0"

    const val PS_STORE_KEY_WORD_VIEW = "view"

    const val PS_STORE_KEY_WORD_CATEGORY = "category"

    const val PS_STORE_DEAL_ID = "__NEXT_DATA__"

    const val BASE_URL = "https://store.playstation.com"

    const val DAYS_OF_PLAY_ALT_TEXT = "Days of Play"

    const val DAYS_OF_PLAY_LOCALIZED_NAME = "cat.gma.daysofplay"

    const val SUMMER_SALE_LOCALIZED_NAME = "cat.gma.SummerSale"

    const val IMAGE_COMPONENT = "EMSImageComponent"

    const val TEXT_COMPONENT = "EMSTextComponent"

    const val PRODUCT_COMPONENT = "Product"

    const val CATEGORY_GRID_COMPONENT = "CategoryGrid"

    const val LINK_TYPE_VIEW = "EMS_VIEW"

    const val TEXT_SEE_ALL_EN = "See all"

    const val TEXT_SEE_ALL_CN = "查看全部"

    val EXCLUDED_LOCALIZED_NAME = setOf(
        "cat.gma.PlayStation_VR2_Games",
        "cat.gma.AllDeals",
        "cat.gma.x_All_PS5_games",
        "cat.gma.x_Free_to_play",
        "cat.gma.PS5_Pro_Enhanced_Games",
        "cat.gma.x_All_PS4_games",
        "cat.gma.AddonsByGame"
    )

    fun generateTargetId(id: String) = "ems-sdk-collection-list-item-target-$id"

}