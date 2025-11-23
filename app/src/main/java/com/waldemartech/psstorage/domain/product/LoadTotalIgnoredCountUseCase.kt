package com.waldemartech.psstorage.domain.product

import com.waldemartech.psstorage.data.local.database.dao.ProductDao
import javax.inject.Inject

class LoadTotalIgnoredCountUseCase @Inject constructor(
    private val productDao: ProductDao
) {
    suspend operator fun invoke(
        storeId: String,
    ): Int {
        return productDao.loadTotalIgnoredCount(storeId = storeId)
    }
}