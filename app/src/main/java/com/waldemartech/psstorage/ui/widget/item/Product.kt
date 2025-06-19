package com.waldemartech.psstorage.ui.widget.item

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.waldemartech.psstorage.ui.widget.entity.ProductItemData

@Composable
fun ProductItemView(item: ProductItemData) {
    Column(
        modifier = Modifier
    ) {
        Box {
            AsyncImage(
                model = item.imageUrl,
                contentDescription = null
            )
            Row(
                modifier = Modifier.align(Alignment.BottomStart)
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
        Text(text = item.classificationText)
        Text(text = item.titleText)
    }

}