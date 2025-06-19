package com.waldemartech.psstorage.domain.store

import com.waldemartech.psstorage.data.local.database.dao.ProductDao
import com.waldemartech.psstorage.ui.deal.detail.FilterMode
import javax.inject.Inject

class LoadProductCountUseCase @Inject constructor(
    private val productDao: ProductDao
) {
    suspend operator fun invoke(
        filterMode: FilterMode,
        storeId: String,
        dealId: String
    ): Int {
        return when(filterMode) {
            FilterMode.Default -> productDao.loadDefaultProductInDealCount(storeId = storeId, dealId = dealId)
            FilterMode.Favorite -> productDao.loadFavoriteProductInDealCount(storeId = storeId, dealId = dealId)
            FilterMode.Ignored -> productDao.loadIgnoredProductInDealCount(storeId = storeId, dealId = dealId)
            FilterMode.Misc -> productDao.loadMiscProductInDealCount(storeId = storeId, dealId = dealId)
            FilterMode.All -> productDao.loadAllProductInDealCount(storeId = storeId, dealId = dealId)
        }
    }
}