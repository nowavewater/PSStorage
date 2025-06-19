package com.waldemartech.psstorage.domain.store

import com.waldemartech.psstorage.data.local.database.dao.ProductDao
import com.waldemartech.psstorage.data.local.database.entity.ProductPriceInDeal
import com.waldemartech.psstorage.ui.deal.detail.FilterMode
import com.waldemartech.psstorage.ui.widget.entity.ProductPriceItemData
import javax.inject.Inject

class LoadProductByPageUseCase @Inject constructor(
    private val productDao: ProductDao
) {
    suspend operator fun invoke(
        filterMode: FilterMode,
        storeId: String,
        dealId: String,
        page: Int
    ) : List<ProductPriceItemData> {
        return when(filterMode) {
            FilterMode.Default ->
                productDao.loadDefaultProductWithDealByPage(
                    storeId = storeId,
                    dealId = dealId,
                    limit = PAGE_ITEM_COUNT,
                    offset = page * PAGE_ITEM_COUNT
                ).map { it.toProductPriceItemData() }
            FilterMode.Favorite ->
                productDao.loadFavoriteProductWithDealByPage(
                    storeId = storeId,
                    dealId = dealId,
                    limit = PAGE_ITEM_COUNT,
                    offset = page * PAGE_ITEM_COUNT
                ).map { it.toProductPriceItemData() }
            FilterMode.Ignored ->
                productDao.loadIgnoredProductWithDealByPage(
                    storeId = storeId,
                    dealId = dealId,
                    limit = PAGE_ITEM_COUNT,
                    offset = page * PAGE_ITEM_COUNT
                ).map { it.toProductPriceItemData() }
            FilterMode.Misc ->
                productDao.loadMiscProductWithDealByPage(
                    storeId = storeId,
                    dealId = dealId,
                    limit = PAGE_ITEM_COUNT,
                    offset = page * PAGE_ITEM_COUNT
                ).map { it.toProductPriceItemData() }
            FilterMode.All ->
                productDao.loadAllProductWithDealByPage(
                    storeId = storeId,
                    dealId = dealId,
                    limit = PAGE_ITEM_COUNT,
                    offset = page * PAGE_ITEM_COUNT
                ).map { it.toProductPriceItemData() }
        }
    }

    companion object {
        const val PAGE_ITEM_COUNT = 100

        fun ProductPriceInDeal.toProductPriceItemData(): ProductPriceItemData {
            return ProductPriceItemData(
                productId = product.productId,
                salePriceText = priceHistory.unit + priceHistory.discountedPrice,
                originalPriceText = priceHistory.unit + priceHistory.basePrice,
                titleText = product.name,
                percentOffText = priceHistory.discountText,
                classificationText = product.localizedDisplayClassification,
                imageUrl = product.imageUrl,
                platformList = platforms.map { it.name }
            )
        }
    }

}