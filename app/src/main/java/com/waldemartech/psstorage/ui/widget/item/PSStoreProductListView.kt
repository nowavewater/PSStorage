package com.waldemartech.psstorage.ui.widget.item

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.waldemartech.psstorage.ui.widget.base.theme.ItemHeightSpacer
import com.waldemartech.psstorage.ui.widget.button.SmallOrionButton

@Composable
fun <T> PSStoreProductListView(
    productItemList: List<T>,
    onPrevious: (String) -> Unit,
    onNext: (String) -> Unit,
    config: ProductListConfig,
    itemView: @Composable (T) -> Unit
) {
    Column(
        modifier = Modifier.padding(20.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = config.totalItemCount.toString())

            // place top right action here

        }
        ItemHeightSpacer()

        LazyVerticalGrid(
            modifier = Modifier
                .fillMaxSize(),
            columns = GridCells.Fixed(2),
            horizontalArrangement = Arrangement.spacedBy(20.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            items(productItemList) { item ->
                itemView(item)
            }

            item(span =  {GridItemSpan(2)}) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {

                    SmallOrionButton("Previous") {
                        onPrevious(config.storeId)
                    }

                    Spacer(modifier = Modifier.width(20.dp))

                    Text(text = config.currentPage.toString())
                    Spacer(modifier = Modifier.width(20.dp))

                    SmallOrionButton("Next") {
                        onNext(config.storeId)
                    }
                }
            }
        }
    }
}

data class ProductListConfig(
    val totalItemCount: Int,
    val currentPage: Int,
    val storeId: String
)