package com.waldemartech.psstorage.ui.deal.detail

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.Popup
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

    val showDialog = remember { mutableStateOf(false) }

    var showPopupMenu by remember { mutableStateOf(false) }

    val currentItem = remember { mutableStateOf<ProductPriceItemData?>(null) }

    if (showPopupMenu) {
        Popup(
            alignment = Alignment.TopEnd,
            onDismissRequest = {
                showPopupMenu = false
            }
        ) {
            Column(
                Modifier.width(80.dp)
                    .background(color = Color.White)
                    .border(color = Color.Black, width = 1.dp)
            ) {
                PopupMenuView(
                    title = FilterMode.Default.text,
                    onClick = {
                        dealDetailViewModel.updateFilterMode(
                            mode = FilterMode.Default,
                            storeId = storeId,
                            dealId = dealId
                        )
                        showPopupMenu = false
                    }
                )
                HorizontalDivider()
                PopupMenuView(
                    title = FilterMode.All.text,
                    onClick = {
                        dealDetailViewModel.updateFilterMode(
                            mode = FilterMode.All,
                            storeId = storeId,
                            dealId = dealId
                        )
                        showPopupMenu = false
                    }
                )
                HorizontalDivider()
                PopupMenuView(
                    title = FilterMode.Favorite.text,
                    onClick = {
                        dealDetailViewModel.updateFilterMode(
                            mode = FilterMode.Favorite,
                            storeId = storeId,
                            dealId = dealId
                        )
                        showPopupMenu = false
                    }
                )
                HorizontalDivider()
                PopupMenuView(
                    title = FilterMode.Ignored.text,
                    onClick = {
                        dealDetailViewModel.updateFilterMode(
                            mode = FilterMode.Ignored,
                            storeId = storeId,
                            dealId = dealId
                        )
                        showPopupMenu = false
                    }
                )
                HorizontalDivider()
                PopupMenuView(
                    title = FilterMode.Misc.text,
                    onClick = {
                        dealDetailViewModel.updateFilterMode(
                            mode = FilterMode.Misc,
                            storeId = storeId,
                            dealId = dealId
                        )
                        showPopupMenu = false
                    }
                )
            }
        }
    }

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
            Row(
                verticalAlignment = Alignment.CenterVertically
            ){
                Text(text = dealDetailViewModel.totalItemCount().value.toString())
                Spacer(modifier = Modifier.width(16.dp))
                Text(text = dealDetailViewModel.filterMode.value.text)
            }



            Icon(
                imageVector = FilterListIcon,
                contentDescription = null,
                modifier = Modifier.clickable {
                    showPopupMenu = true
                }
            )
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
        Box {
            AsyncImage(
                model = item.imageUrl,
                contentDescription = null
            )
            Row(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                val backgroundShape = RoundedCornerShape(4.dp)
                item.platformList.forEach { platform ->
                    Text(
                        text = platform,
                        style = TextStyle(
                            color = Color.White,
                            fontSize = 10.sp
                        ),
                        modifier = Modifier
                            .background(
                                color = Color.Black.copy(alpha = 0.4f),
                                shape = backgroundShape
                            )
                            .border(width = 1.dp, color = Color.White, shape = backgroundShape)
                            .padding(horizontal = 4.dp, vertical = 2.dp)
                    )
                }
            }
        }
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

@Composable
private fun PopupMenuView(
    title: String,
    onClick: () -> Unit = {}
) {
    Text(
        text = title,
        modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
            .clickable(onClick = onClick)
    )
}

