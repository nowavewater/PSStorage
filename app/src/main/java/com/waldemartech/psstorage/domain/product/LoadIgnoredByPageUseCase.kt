package com.waldemartech.psstorage.domain.product

import com.waldemartech.psstorage.data.local.database.dao.ProductDao
import com.waldemartech.psstorage.domain.store.LoadProductByPageUseCase.Companion.PAGE_ITEM_COUNT
import com.waldemartech.psstorage.ui.widget.entity.ProductItemData
import javax.inject.Inject

class LoadIgnoredByPageUseCase @Inject constructor(
    private val productDao: ProductDao
) {
    suspend operator fun invoke(storeId: String, page: Int) : List<ProductItemData> {
        return productDao.loadIgnoredByPage(
            storeId = storeId,
            limit = PAGE_ITEM_COUNT,
            offset = page * PAGE_ITEM_COUNT
        ).map { productDetail ->
            ProductItemData(
                productId = productDetail.product.productId,
                titleText = productDetail.product.name,
                classificationText = productDetail.product.localizedDisplayClassification,
                imageUrl = productDetail.product.imageUrl,
                platformList = productDetail.platforms.map { it.name }
            )
        }
    }
}