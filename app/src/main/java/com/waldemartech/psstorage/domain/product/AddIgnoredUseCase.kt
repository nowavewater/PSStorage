package com.waldemartech.psstorage.domain.product

import com.waldemartech.psstorage.data.local.database.dao.ProductDao
import com.waldemartech.psstorage.data.local.database.table.IgnoredProduct
import javax.inject.Inject

class AddIgnoredUseCase @Inject constructor(
    private val productDao: ProductDao
) {
    suspend operator fun invoke(storeId: String, productId: String) {
        return productDao.insertIgnoredProduct(
            IgnoredProduct(
                ignoredProductId = productId,
                storeIdInIgnoredProduct = storeId
            )
        )
    }
}