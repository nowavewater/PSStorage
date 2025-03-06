package com.waldemartech.psstorage.ui.deal.detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil3.compose.AsyncImage
import com.waldemartech.psstorage.ui.widget.button.SmallOrionButton
import com.waldemartech.psstorage.ui.widget.entity.ProductItemData

@Composable
fun DealDetailScreen(
    storeId: String,
    dealId: String,
    dealDetailViewModel: DealDetailViewModel = hiltViewModel()
) {
    LaunchedEffect(Unit) {
        dealDetailViewModel.loadProductList(storeId = storeId, dealId = dealId, pageIndex = 0)
    }

    Column {
        LazyVerticalGrid(
            modifier = Modifier.fillMaxSize()
                .padding(horizontal = 20.dp, vertical = 20.dp),
            columns = GridCells.Fixed(2),
            horizontalArrangement = Arrangement.spacedBy(20.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            items(dealDetailViewModel.productList()) { item ->
                ProductItemView(item)
            }
        }
        Row {

            SmallOrionButton("Previous") {
                dealDetailViewModel.loadPreviousPage(storeId, dealId)
            }

            Text(text = dealDetailViewModel.currentPage().value.toString())
            SmallOrionButton("Next") {
                dealDetailViewModel.loadNextPage(storeId, dealId)
            }

        }
    }
}

@Composable
fun ProductItemView(item: ProductItemData) {
    Column {
        AsyncImage(
            model = item.imageUrl,
            contentDescription = null
        )
        Text(text = item.productTypeText)
        Text(text = item.titleText)
        Text(text = item.percentOffText)
        Row {
            Text(text = item.salePriceText)
            Text(text = item.originalPriceText)
        }
    }

}

