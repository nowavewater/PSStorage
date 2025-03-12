package com.waldemartech.psstorage.domain.product

import com.waldemartech.psstorage.data.local.database.dao.ProductDao
import com.waldemartech.psstorage.data.local.database.table.FavoriteProduct
import javax.inject.Inject

class AddFavoriteUseCase @Inject constructor(
    private val productDao: ProductDao
) {
    suspend operator fun invoke(storeId: String, productId: String) : Unit {
        return productDao.insertFavoriteProduct(
            FavoriteProduct(
                favoriteProductId = productId,
                storeIdInFavoriteProduct = storeId
            )
        )
    }
}