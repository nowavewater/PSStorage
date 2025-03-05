package com.waldemartech.psstorage.ui

import androidx.navigation.NavController
import com.waldemartech.psstorage.data.base.SharedConstants.LEFT_BRACE
import com.waldemartech.psstorage.data.base.SharedConstants.RIGHT_BRACE
import com.waldemartech.psstorage.data.base.SharedConstants.SLASH_SIGN

object MainNavConstants {

    const val DEST_STORE_LIST = "store_list"

    const val DEST_STORE_DETAIL = "store_detail"

    const val DEST_DEAL_LIST = "deal_list"

    const val DEST_DEAL_DETAIL = "deal_detail"

    const val DEST_PRODUCT_LIST = "product_list"

    const val DEST_PRODUCT_DETAIL = "product_detail"

    const val DEST_IGNORED_LIST = "ignored_list"

    const val DEST_FAVORITE_LIST = "favorite_list"



    fun buildSingleArgsDest(dest: String, key: String) =
        dest + SLASH_SIGN + LEFT_BRACE + key + RIGHT_BRACE

    fun buildDoubleArgsDest(dest: String, first: String, second: String) =
        dest + SLASH_SIGN + LEFT_BRACE + first + RIGHT_BRACE +
                SLASH_SIGN + LEFT_BRACE + second + RIGHT_BRACE

    fun NavController.navigateSingle(
        dest: String,
        args: String
    ) {
        navigate(dest + SLASH_SIGN + args)
    }

    fun NavController.navigateDouble(
        dest: String,
        first: String,
        second: String
    ) {
        navigate(dest + SLASH_SIGN + first + SLASH_SIGN + second)
    }

}