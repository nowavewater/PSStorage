package com.waldemartech.psstorage.ui.deal.detail

import com.waldemartech.psstorage.ui.deal.detail.DealDetailConstants.ALL_TEXT
import com.waldemartech.psstorage.ui.deal.detail.DealDetailConstants.DEFAULT_TEXT
import com.waldemartech.psstorage.ui.deal.detail.DealDetailConstants.FAVORITE_TEXT
import com.waldemartech.psstorage.ui.deal.detail.DealDetailConstants.IGNORED_TEXT
import com.waldemartech.psstorage.ui.deal.detail.DealDetailConstants.MISC_TEXT

enum class FilterMode(val text: String) {
    All(ALL_TEXT),
    Favorite(FAVORITE_TEXT),
    Ignored(IGNORED_TEXT),
    Misc(MISC_TEXT),
    Default(DEFAULT_TEXT);
}

object DealDetailConstants {
    const val ALL_TEXT = "All"
    const val FAVORITE_TEXT = "Favorite"
    const val IGNORED_TEXT = "Ignored"
    const val MISC_TEXT = "Misc"
    const val DEFAULT_TEXT = "Default"
}