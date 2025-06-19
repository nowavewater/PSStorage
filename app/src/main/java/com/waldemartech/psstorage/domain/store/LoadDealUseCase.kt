package com.waldemartech.psstorage.domain.store

import com.waldemartech.psstorage.data.local.database.dao.DealDao
import com.waldemartech.psstorage.data.store.StoreId
import com.waldemartech.psstorage.ui.widget.entity.DealWidgetData
import javax.inject.Inject

class LoadDealUseCase @Inject constructor(
    private val dealDao: DealDao
) {
    suspend operator fun invoke(storeId: StoreId) : List<DealWidgetData> {
        return dealDao.loadCurrentDealsByStore(storeId.storeId)
            .map {
                DealWidgetData(
                    dealId = it.deal.dealId,
                    imageUrl = it.deal.imageUrl
                )
            }
    }

}