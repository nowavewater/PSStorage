package com.waldemartech.psstorage.domain.store

import com.waldemartech.psstorage.data.local.database.dao.ProductDao
import com.waldemartech.psstorage.ui.widget.entity.ProductItemData
import javax.inject.Inject

class LoadProductByPageUseCase @Inject constructor(
    private val productDao: ProductDao
) {
    suspend operator fun invoke(storeId: String, dealId: String, offset: Int) : List<ProductItemData> {
        return productDao.loadProductByPage(
            storeId = storeId,
            dealId = dealId,
            limit = PAGE_ITEM,
            offset = offset
        ).map { productHistory ->
            val priceHistory = productHistory.priceHistory
            val product = productHistory.product
            ProductItemData(
                salePriceText = priceHistory.unit + priceHistory.discountedPrice,
                originalPriceText = priceHistory.unit + priceHistory.basePrice,
                titleText = product.name,
                percentOffText = priceHistory.discountText,
                productTypeText = product.typeName,
                imageUrl = product.imageUrl
            )
        }
    }

    companion object {
        const val PAGE_ITEM = 100
    }

}