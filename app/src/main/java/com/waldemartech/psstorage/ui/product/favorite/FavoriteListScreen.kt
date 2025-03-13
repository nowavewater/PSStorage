package com.waldemartech.psstorage.ui.product.favorite

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.waldemartech.psstorage.ui.widget.button.SmallOrionButton
import com.waldemartech.psstorage.ui.widget.item.ProductItemView

@Composable
fun FavoriteListScreen(
    storeId: String,
    favoriteListViewModel: FavoriteListViewModel = hiltViewModel()
) {
    LaunchedEffect(Unit) {
        favoriteListViewModel.loadFavoriteList(storeId = storeId, pageIndex = 0)
    }

    Column {
        LazyVerticalGrid(
            modifier = Modifier.fillMaxSize()
                .padding(horizontal = 20.dp, vertical = 20.dp),
            columns = GridCells.Fixed(2),
            horizontalArrangement = Arrangement.spacedBy(20.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            items(favoriteListViewModel.productList()) { item ->
                ProductItemView(item)
            }

            item(span =  { GridItemSpan(2) }) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {

                    SmallOrionButton("Previous") {
                        favoriteListViewModel.loadPreviousPage(storeId)
                    }

                    Spacer(modifier = Modifier.width(20.dp))

                    Text(text = favoriteListViewModel.currentPage().value.toString())
                    Spacer(modifier = Modifier.width(20.dp))

                    SmallOrionButton("Next") {
                        favoriteListViewModel.loadNextPage(storeId)
                    }
                }
            }
        }
    }

}