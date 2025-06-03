package com.waldemartech.psstorage.ui.deal.detail

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import coil3.compose.AsyncImage
import com.waldemartech.psstorage.ui.widget.button.HeightSpacer
import com.waldemartech.psstorage.ui.widget.button.JellyButton
import com.waldemartech.psstorage.ui.widget.button.SmallOrionButton
import com.waldemartech.psstorage.ui.widget.entity.ProductPriceItemData
import com.waldemartech.psstorage.ui.widget.icon.FilterListIcon
import timber.log.Timber
import androidx.compose.material3.Surface as Surface1

@Composable
fun DealDetailScreen(
    storeId: String,
    dealId: String,
    dealDetailViewModel: DealDetailViewModel = hiltViewModel()
) {
    LaunchedEffect(Unit) {
        dealDetailViewModel.loadProductList(storeId = storeId, dealId = dealId, pageIndex = 0)
    }

    LaunchedEffect(Unit) {
        dealDetailViewModel.loadTotalItemCount(storeId = storeId, dealId = dealId)
    }

    val showDialog = remember { mutableStateOf(false) }

    val currentItem = remember { mutableStateOf<ProductPriceItemData?>(null) }

    if (showDialog.value) {
        Timber.i("on dialog ${currentItem.value != null}")

        currentItem.value?.let {
            Timber.i("on show dialog")
            OptionDialog(storeId= storeId, item = it, onDismiss = {showDialog.value = false}, dealDetailViewModel = dealDetailViewModel)
        }
    }

    Column(
        modifier = Modifier.padding(20.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = dealDetailViewModel.totalItemCount().value.toString())
            Icon(imageVector = FilterListIcon, contentDescription = null)
        }
        ItemHeightSpacer()

        LazyVerticalGrid(
            modifier = Modifier
                .fillMaxSize(),
            columns = GridCells.Fixed(2),
            horizontalArrangement = Arrangement.spacedBy(20.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            items(dealDetailViewModel.productList()) { item ->
                ProductPriceItemView(item, onLongClick = {
                    Timber.i("on item click")
                    currentItem.value = item
                    showDialog.value = true
                })
            }

            item(span =  {GridItemSpan(2)}) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {

                    SmallOrionButton("Previous") {
                        dealDetailViewModel.loadPreviousPage(storeId, dealId)
                    }

                    Spacer(modifier = Modifier.width(20.dp))

                    Text(text = (dealDetailViewModel.currentPage().value + 1).toString())
                    Spacer(modifier = Modifier.width(20.dp))

                    SmallOrionButton("Next") {
                        dealDetailViewModel.loadNextPage(storeId, dealId)
                    }
                }
            }
        }
    }
}

@Composable
private fun OptionDialog(
    storeId: String,
    item: ProductPriceItemData,
    onDismiss: () -> Unit,
    dealDetailViewModel: DealDetailViewModel
) {
    Dialog(onDismissRequest = onDismiss) {
        Surface1(
            shape = RoundedCornerShape(8.dp),
            color = Color.White
        ) {
            Column(
                modifier = Modifier.padding(20.dp)
            ) {
                JellyButton(text = "Add to favorite") {
                    dealDetailViewModel.addToFavorite(storeId, item)
                    onDismiss()
                }
                HeightSpacer()
                JellyButton(text = "Add to ignored") {
                    dealDetailViewModel.addToIgnored(storeId, item)
                    onDismiss()
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ProductPriceItemView(item: ProductPriceItemData, onLongClick:() -> Unit) {
    Column(
        modifier = Modifier
            .combinedClickable(
                onLongClick = {
                    Timber.i("on long click")
                    onLongClick()
                },
                onClick = {
                    Timber.i("on click")

                }
            )
    ) {
        AsyncImage(
            model = item.imageUrl,
            contentDescription = null
        )
        ItemHeightSpacer()
        Text(
            text = item.classificationText,
            style = TextStyle(
                color = Color.Gray,
                fontSize = 12.sp

        ))
        ItemHeightSpacer()
        Text(text = item.titleText)
        ItemHeightSpacer()

        if (item.percentOffText.isNotBlank()) {
            Text(
                text = item.percentOffText,
                Modifier
                    .background(
                        color = Color.Black,
                        shape = RoundedCornerShape(20)
                    )
                    .padding(6.dp),
                style = TextStyle(
                    color = Color.White,
                    fontSize = 16.sp
                ),
            )
            ItemHeightSpacer()
        }

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = item.salePriceText)
            Spacer(Modifier.width(10.dp))
            Text(
                text = item.originalPriceText,
                style = TextStyle(
                    color = Color.Gray,
                    textAlign = TextAlign.Center,
                    textDecoration = TextDecoration.LineThrough
                )
            )
        }
    }

}

@Composable
private fun ItemHeightSpacer() {
    Spacer(modifier = Modifier.height(4.dp))
}

