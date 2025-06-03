package com.waldemartech.psstorage.domain.store

import com.waldemartech.psstorage.data.local.database.dao.ProductDao
import javax.inject.Inject

class LoadProductCountUseCase @Inject constructor(
    private val productDao: ProductDao
) {
    suspend operator fun invoke(storeId: String, dealId: String): Int {
        return productDao.loadProductCount(storeId, dealId)
    }
}