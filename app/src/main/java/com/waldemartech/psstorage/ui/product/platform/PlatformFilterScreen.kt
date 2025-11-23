package com.waldemartech.psstorage.ui.product.platform

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.waldemartech.psstorage.data.store.StoreId
import com.waldemartech.psstorage.ui.widget.base.theme.LocalNavController
import com.waldemartech.psstorage.ui.widget.item.ProductItemView

@Composable
fun PlatformFilterScreen(
    storeId: StoreId,
    platformFilterViewModel: PlatformFilterViewModel = hiltViewModel()
) {
    val navController = LocalNavController.current

    LaunchedEffect(Unit) {
        platformFilterViewModel.loadPlatformDuplicated(storeId = storeId)
    }

    Column {
        LazyVerticalGrid(
            modifier = Modifier.fillMaxSize()
                .padding(horizontal = 20.dp, vertical = 20.dp),
            columns = GridCells.Fixed(2),
            horizontalArrangement = Arrangement.spacedBy(20.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            items(platformFilterViewModel.productList()) { item ->
                ProductItemView(item, onLongClick = {})
            }
        }
    }


}