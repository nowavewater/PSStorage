package com.waldemartech.psstorage.ui.widget.item

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import coil3.compose.AsyncImage
import com.waldemartech.psstorage.ui.widget.entity.ProductItemData

@Composable
fun ProductItemView(item: ProductItemData) {
    Column(
        modifier = Modifier
    ) {
        AsyncImage(
            model = item.imageUrl,
            contentDescription = null
        )
        Text(text = item.productTypeText)
        Text(text = item.titleText)
    }

}