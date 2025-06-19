package com.waldemartech.psstorage.ui.deal.list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil3.compose.AsyncImage
import com.waldemartech.psstorage.R
import com.waldemartech.psstorage.data.store.StoreId
import com.waldemartech.psstorage.ui.MainNavConstants.DEST_DEAL_DETAIL
import com.waldemartech.psstorage.ui.MainNavConstants.navigateDouble
import com.waldemartech.psstorage.ui.widget.base.theme.LocalNavController
import timber.log.Timber


@Composable
fun DealListScreen(
    storeId: StoreId,
    dealListViewModel: DealListViewModel = hiltViewModel()
) {
    val navController = LocalNavController.current

    LaunchedEffect(Unit) {
        dealListViewModel.loadDeals(storeId)
    }
    LazyVerticalGrid(
        modifier = Modifier.fillMaxSize()
            .padding(horizontal = 20.dp, vertical = 20.dp),
        columns = GridCells.Fixed(2),
        horizontalArrangement = Arrangement.spacedBy(20.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        items(dealListViewModel.dealList()) { deal ->
            Timber.i("deal url is ${deal.imageUrl}")
            AsyncImage(
                model = deal.imageUrl,
                error = painterResource(R.drawable.error_placeholder),
                placeholder = painterResource(R.drawable.loading_placeholder),
                contentDescription = null,
                modifier = Modifier.fillMaxWidth()
                    .clickable {
                        navController.navigateDouble(DEST_DEAL_DETAIL, storeId.storeId, deal.dealId)
                    }
            )
        }
    }

}