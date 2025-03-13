package com.waldemartech.psstorage.domain.store

import com.waldemartech.psstorage.data.local.database.dao.ProductDao
import com.waldemartech.psstorage.ui.widget.entity.ProductPriceItemData
import javax.inject.Inject

class LoadProductByPageUseCase @Inject constructor(
    private val productDao: ProductDao
) {
    suspend operator fun invoke(storeId: String, dealId: String, page: Int) : List<ProductPriceItemData> {
        return productDao.loadProductByPage(
            storeId = storeId,
            dealId = dealId,
            limit = PAGE_ITEM_COUNT,
            offset = page * PAGE_ITEM_COUNT
        ).map { productHistory ->
            val priceHistory = productHistory.priceHistory
            val product = productHistory.product
            ProductPriceItemData(
                productId = product.productId,
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
        const val PAGE_ITEM_COUNT = 100
    }

}