package com.waldemartech.psstorage.domain.product

import com.waldemartech.psstorage.data.local.database.dao.ProductDao
import javax.inject.Inject

class DeleteFavoriteUseCase @Inject constructor(
    private val productDao: ProductDao
) {
    suspend operator fun invoke(productId: String) {
        return productDao.deleteFavoriteProduct(productId = productId)
    }
}