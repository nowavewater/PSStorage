package com.waldemartech.psstorage.domain.tool

import com.waldemartech.psstorage.data.local.database.dao.ProductDao
import com.waldemartech.psstorage.data.local.database.entity.ProductDetailData
import javax.inject.Inject

class RemoveDuplicatedUseCase @Inject constructor(
    private val productDao: ProductDao
) {
    suspend operator fun invoke(storeId: String) {
        val favoriteMap = mutableMapOf<String, ProductDetailData>()
        productDao.loadAllFavoriteProduct(storeId).forEach { favoriteProduct ->
            favoriteMap[favoriteProduct.product.productId] = favoriteProduct
        }
        val duplicatedList = mutableListOf<ProductDetailData>()
        val ignoredList = productDao.loadAllIgnoredProduct(storeId)
        ignoredList.forEach { ignoredProduct ->
            if(favoriteMap.contains(ignoredProduct.product.productId)) {
                duplicatedList.add(ignoredProduct)
            }
        }
        duplicatedList.forEach { duplicatedProduct ->
            productDao.deleteIgnoredProduct(duplicatedProduct.product.productId)
        }
    }
}
