package com.waldemartech.psstorage.ui.deal.detail

import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun DealDetailScreen(
    storeId: String,
    dealId: String,
    dealDetailViewModel: DealDetailViewModel = hiltViewModel()
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2)
    ) {
        /*items(photos) { photo ->
            PhotoItem(photo)
        }*/
    }
}