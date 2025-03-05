package com.waldemartech.psstorage.ui.store.list

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.waldemartech.psstorage.data.store.StoreConstants.HK_STORE_ID
import com.waldemartech.psstorage.data.store.StoreConstants.US_STORE_ID
import com.waldemartech.psstorage.ui.MainNavConstants.DEST_STORE_DETAIL
import com.waldemartech.psstorage.ui.MainNavConstants.DEST_STORE_LIST
import com.waldemartech.psstorage.ui.MainNavConstants.navigateSingle
import com.waldemartech.psstorage.ui.widget.button.HeightSpacer
import com.waldemartech.psstorage.ui.widget.button.JellyButton
import com.waldemartech.psstorage.ui.widget.base.theme.LocalNavController

@Composable
fun StoreListScreen(
    storeListViewModel: StoreListViewModel = hiltViewModel()
) {
    val navController = LocalNavController.current

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        HeightSpacer()

        JellyButton(text = "HK") {
            navController.navigateSingle(DEST_STORE_DETAIL, HK_STORE_ID)
        }
        HeightSpacer()

        JellyButton(text = "US") {
            navController.navigateSingle(DEST_STORE_LIST, US_STORE_ID)

        }
    }

}

