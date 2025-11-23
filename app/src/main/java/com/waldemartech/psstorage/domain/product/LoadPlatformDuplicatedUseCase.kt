package com.waldemartech.psstorage.domain.product

import com.waldemartech.psstorage.data.local.database.dao.ProductDao
import com.waldemartech.psstorage.data.local.database.entity.ProductDetailData
import com.waldemartech.psstorage.data.store.StoreConstants.PLATFORM_PS4
import com.waldemartech.psstorage.data.store.StoreConstants.PLATFORM_PS5
import com.waldemartech.psstorage.data.store.StoreId
import com.waldemartech.psstorage.domain.product.ProductConstants.toProductItemData
import com.waldemartech.psstorage.ui.widget.entity.ProductItemData
import javax.inject.Inject

class LoadPlatformDuplicatedUseCase @Inject constructor(
    private val productDao: ProductDao
) {
    suspend operator fun invoke(storeId: StoreId) : List<ProductItemData> {
        val duplicatedPairList = mutableListOf<Pair<ProductItemData, ProductItemData>>()
        val productList = productDao.loadAllProductByStore(storeId = storeId.storeId)
        val duplicatedMap = mutableMapOf<String, ProductDetailData>()
        productList.forEach { product ->
            if (duplicatedMap.contains(product.product.name)) {
                duplicatedMap[product.product.name]?.let { existingProduct ->
                    if (existingProduct.platforms.size == 1 && product.platforms.size == 1) {
                        when {
                            existingProduct.platforms.first().name == PLATFORM_PS4 &&
                                    product.platforms.first().name == PLATFORM_PS5 -> {
                                        duplicatedPairList.add(Pair(existingProduct.toProductItemData(), product.toProductItemData()))
                                    }
                            existingProduct.platforms.first().name == PLATFORM_PS5 &&
                                    product.platforms.first().name == PLATFORM_PS4 -> {
                                        duplicatedPairList.add(Pair(product.toProductItemData(), existingProduct.toProductItemData()))
                                    }
                        }
                    }
                }
            } else {
                duplicatedMap[product.product.name] = product
            }
        }
        val resultList = mutableListOf<ProductItemData>()
        duplicatedPairList.forEach { pair ->
            resultList.add(pair.first)
            resultList.add(pair.second)
        }
        return resultList
    }
}