package com.waldemartech.psstorage.ui.product.favorite

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import com.waldemartech.psstorage.ui.widget.button.JellyButton
import com.waldemartech.psstorage.ui.widget.entity.ProductItemData
import com.waldemartech.psstorage.ui.widget.item.PSStoreProductListView
import com.waldemartech.psstorage.ui.widget.item.ProductItemView
import com.waldemartech.psstorage.ui.widget.item.ProductListConfig
import timber.log.Timber

@Composable
fun FavoriteListScreen(
    storeId: String,
    favoriteListViewModel: FavoriteListViewModel = hiltViewModel()
) {
    LaunchedEffect(Unit) {
        favoriteListViewModel.loadFavoriteList(storeId = storeId, pageIndex = 0)
    }

    LaunchedEffect(Unit
    ) {
        favoriteListViewModel.loadFavoriteCount(storeId = storeId)
    }

    val currentItem = remember { mutableStateOf<ProductItemData?>(null) }

    val showDialog = remember { mutableStateOf(false) }

    if (showDialog.value) {
        Timber.i("on show dialog")
        currentItem.value?.let {
            Timber.i("on show dialog")
            OptionDialog(
                storeId = storeId,
                item = it,
                onDismiss = { showDialog.value = false },
                favoriteListViewModel = favoriteListViewModel
            )
        }

    }
    val config = ProductListConfig(
        totalItemCount = favoriteListViewModel.totalItemCount().value,
        currentPage = favoriteListViewModel.currentPage().value,
        storeId = storeId
    )
    PSStoreProductListView(
        productItemList = favoriteListViewModel.productList(),
        onPrevious = { favoriteListViewModel.loadPreviousPage(storeId = storeId) },
        onNext = { favoriteListViewModel.loadNextPage(storeId = storeId) },
        config = config,
        itemView = { item ->
            ProductItemView(
                item = item,
                onLongClick = {
                    currentItem.value = item
                    showDialog.value = true
                }
            )
        }
    )

}

@Composable
private fun OptionDialog(
    storeId: String,
    item: ProductItemData,
    onDismiss: () -> Unit,
    favoriteListViewModel: FavoriteListViewModel
) {
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(8.dp),
            color = Color.White
        ) {
            Column(
                modifier = Modifier.padding(20.dp)
            ) {
                JellyButton(text = "Add to ignored") {
                    favoriteListViewModel.addToIgnored(storeId, item)
                    onDismiss()
                }
            }
        }
    }
}