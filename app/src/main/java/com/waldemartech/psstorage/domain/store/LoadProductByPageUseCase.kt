package com.waldemartech.psstorage.domain.store

import com.waldemartech.psstorage.data.local.database.dao.DealDao
import com.waldemartech.psstorage.data.local.database.dao.ProductDao
import com.waldemartech.psstorage.ui.widget.entity.DealWidgetData
import javax.inject.Inject

class LoadProductByPageUseCase @Inject constructor(
    private val productDao: ProductDao
) {
    suspend operator fun invoke(storeId: String) : List<DealWidgetData> {
        return productDao(storeId)
            .map {
                DealWidgetData(
                    dealId = it.deal.dealId,
                    imageUrl = it.deal.imageUrl
                )
            }
    }

}