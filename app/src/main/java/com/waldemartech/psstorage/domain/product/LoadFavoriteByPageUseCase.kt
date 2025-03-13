package com.waldemartech.psstorage.domain.product

import com.waldemartech.psstorage.data.local.database.dao.ProductDao
import com.waldemartech.psstorage.domain.store.LoadProductByPageUseCase.Companion.PAGE_ITEM_COUNT
import com.waldemartech.psstorage.ui.widget.entity.ProductItemData
import javax.inject.Inject

class LoadFavoriteByPageUseCase  @Inject constructor(
    private val productDao: ProductDao
) {
    suspend operator fun invoke(storeId: String, page: Int) : List<ProductItemData> {
        return productDao.loadFavoriteByPage(
            storeId = storeId,
            limit = PAGE_ITEM_COUNT,
            offset = page * PAGE_ITEM_COUNT
        ).map { product ->
            ProductItemData(
                productId = product.productId,
                titleText = product.name,
                productTypeText = product.typeName,
                imageUrl = product.imageUrl
            )
        }
    }
}