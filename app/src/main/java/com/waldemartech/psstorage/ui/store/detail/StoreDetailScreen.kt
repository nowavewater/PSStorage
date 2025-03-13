package com.waldemartech.psstorage.ui.store.detail

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.waldemartech.psstorage.ui.MainNavConstants.DEST_DEAL_LIST
import com.waldemartech.psstorage.ui.MainNavConstants.DEST_FAVORITE_LIST
import com.waldemartech.psstorage.ui.MainNavConstants.DEST_IGNORED_LIST
import com.waldemartech.psstorage.ui.MainNavConstants.navigateSingle
import com.waldemartech.psstorage.ui.product.favorite.FavoriteListScreen
import com.waldemartech.psstorage.ui.widget.button.HeightSpacer
import com.waldemartech.psstorage.ui.widget.button.JellyButton
import com.waldemartech.psstorage.ui.widget.base.theme.LocalNavController

@Composable
fun StoreDetailScreen(
    storeId: String,
    storeDetailViewModel: StoreDetailViewModel = hiltViewModel()
) {
    val navController = LocalNavController.current

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        HeightSpacer()

        JellyButton(text = "Deals") {
            navController.navigateSingle(DEST_DEAL_LIST, storeId)
        }
        HeightSpacer()

        JellyButton(text = "Favorite") {
            navController.navigateSingle(DEST_FAVORITE_LIST, storeId)

        }

        HeightSpacer()

        JellyButton(text = "Ignored") {
            navController.navigateSingle(DEST_IGNORED_LIST, storeId)

        }
    }

}