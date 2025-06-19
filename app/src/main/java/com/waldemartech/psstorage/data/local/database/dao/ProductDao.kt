package com.waldemartech.psstorage.data.local.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.waldemartech.psstorage.data.local.database.entity.ProductDetailData
import com.waldemartech.psstorage.data.local.database.entity.ProductPriceInDeal
import com.waldemartech.psstorage.data.local.database.table.FavoriteProduct
import com.waldemartech.psstorage.data.local.database.table.IgnoredProduct
import com.waldemartech.psstorage.data.local.database.table.Product
import com.waldemartech.psstorage.data.local.database.table.ProductPlatformCrossRef

@Dao
interface ProductDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertProduct(item: Product)

    @Query("SELECT EXISTS(SELECT 1 FROM product WHERE productId == :id)")
    suspend fun hasProduct(id: String) : Boolean

    @Query("SELECT EXISTS (SELECT 1 FROM ProductPlatformCrossRef " +
            "INNER JOIN platform ON platform.platformId = ProductPlatformCrossRef.platformId " +
            "WHERE ProductPlatformCrossRef.productId == :productId AND platform.name == :platformName)")
    suspend fun hasProductInPlatform(productId: String, platformName: String): Boolean

    @Insert(onConflict = OnConflictStrategy.NONE)
    suspend fun insertProductPlatformCrossRef(crossRef: ProductPlatformCrossRef)

    @Transaction
    @Query("SELECT * FROM product " +
            "INNER JOIN price_history ON product.productId = price_history.productIdPriceRef " +
            "WHERE storeIdInProduct == :storeId " +
            "AND productId NOT IN (SELECT favoriteProductId FROM favorite_product) " +
            "AND productId NOT IN (SELECT ignoredProductId FROM ignored_product) " +
            "AND dealIdPriceRef = :dealId " +
            "LIMIT :limit OFFSET :offset")
    suspend fun loadDefaultProductWithDealByPage(
        storeId: String,
        dealId: String,
        limit: Int,
        offset: Int
    ) : List<ProductPriceInDeal>

    @Transaction
    @Query("SELECT * FROM product " +
            "INNER JOIN price_history ON product.productId = price_history.productIdPriceRef " +
            "WHERE storeIdInProduct == :storeId " +
            "AND productId IN (SELECT favoriteProductId FROM favorite_product) " +
            "AND dealIdPriceRef = :dealId " +
            "LIMIT :limit OFFSET :offset")
    suspend fun loadFavoriteProductWithDealByPage(
        storeId: String,
        dealId: String,
        limit: Int,
        offset: Int
    ) : List<ProductPriceInDeal>

    @Transaction
    @Query("SELECT * FROM product " +
            "INNER JOIN price_history ON product.productId = price_history.productIdPriceRef " +
            "WHERE storeIdInProduct == :storeId " +
            "AND productId IN (SELECT ignoredProductId FROM ignored_product) " +
            "AND dealIdPriceRef = :dealId " +
            "LIMIT :limit OFFSET :offset")
    suspend fun loadIgnoredProductWithDealByPage(
        storeId: String,
        dealId: String,
        limit: Int,
        offset: Int
    ) : List<ProductPriceInDeal>

    @Transaction
    @Query("SELECT * FROM product " +
            "INNER JOIN price_history ON product.productId = price_history.productIdPriceRef " +
            "WHERE storeIdInProduct == :storeId " +
            "AND dealIdPriceRef = :dealId " +
            "LIMIT :limit OFFSET :offset")
    suspend fun loadAllProductWithDealByPage(
        storeId: String,
        dealId: String,
        limit: Int,
        offset: Int
    ) : List<ProductPriceInDeal>

    @Transaction
    @Query("SELECT * FROM product " +
            "INNER JOIN price_history ON product.productId = price_history.productIdPriceRef " +
            "WHERE storeIdInProduct == :storeId " +
            "AND productId NOT IN (SELECT favoriteProductId FROM favorite_product) " +
            "AND productId NOT IN (SELECT ignoredProductId FROM ignored_product) " +
            "AND storeDisplayClassification NOT IN ('FULL_GAME', 'PREMIUM_EDITION', 'GAME_BUNDLE') " +
            "AND dealIdPriceRef = :dealId " +
            "LIMIT :limit OFFSET :offset")
    suspend fun loadMiscProductWithDealByPage(
        storeId: String,
        dealId: String,
        limit: Int,
        offset: Int
    ) : List<ProductPriceInDeal>

    @Query("SELECT COUNT(*) FROM price_history " +
            "INNER JOIN product ON productId = productIdPriceRef " +
            "WHERE storeIdInProduct == :storeId " +
            "AND productId NOT IN (SELECT favoriteProductId FROM favorite_product) " +
            "AND productId NOT IN (SELECT ignoredProductId FROM ignored_product) " +
            "AND dealIdPriceRef = :dealId")
        suspend fun loadDefaultProductInDealCount(
            storeId: String,
            dealId: String
        ): Int

    @Query("SELECT COUNT(*) FROM price_history " +
            "INNER JOIN product ON productId = productIdPriceRef " +
            "WHERE storeIdInProduct == :storeId " +
            "AND productId IN (SELECT favoriteProductId FROM favorite_product) " +
            "AND dealIdPriceRef = :dealId")
    suspend fun loadFavoriteProductInDealCount(
        storeId: String,
        dealId: String
    ): Int

    @Query("SELECT COUNT(*) FROM price_history " +
            "INNER JOIN product ON productId = productIdPriceRef " +
            "WHERE storeIdInProduct == :storeId " +
            "AND productId IN (SELECT ignoredProductId FROM ignored_product) " +
            "AND dealIdPriceRef = :dealId")
    suspend fun loadIgnoredProductInDealCount(
        storeId: String,
        dealId: String
    ): Int

    @Query("SELECT COUNT(*) FROM price_history " +
            "INNER JOIN product ON productId = productIdPriceRef " +
            "WHERE storeIdInProduct == :storeId " +
            "AND dealIdPriceRef = :dealId")
    suspend fun loadAllProductInDealCount(
        storeId: String,
        dealId: String
    ): Int

    @Query("SELECT COUNT(*) FROM price_history " +
            "INNER JOIN product ON productId = productIdPriceRef " +
            "WHERE storeIdInProduct == :storeId " +
            "AND productId NOT IN (SELECT favoriteProductId FROM favorite_product) " +
            "AND productId NOT IN (SELECT ignoredProductId FROM ignored_product) " +
            "AND storeDisplayClassification NOT IN ('FULL_GAME', 'PREMIUM_EDITION', 'GAME_BUNDLE') " +
            "AND dealIdPriceRef = :dealId")
    suspend fun loadMiscProductInDealCount(
        storeId: String,
        dealId: String
    ): Int

    @Query("SELECT * FROM product WHERE productId IN (SELECT favoriteProductId FROM favorite_product) AND storeIdInProduct == :storeId LIMIT :limit OFFSET :offset")
    suspend fun loadFavoriteByPage(
        storeId: String,
        limit: Int,
        offset: Int
    ) : List<ProductDetailData>

    @Query("SELECT * FROM product WHERE productId IN (SELECT favoriteProductId FROM favorite_product) AND storeIdInProduct == :storeId")
    suspend fun loadAllFavoriteProduct(
        storeId: String,
    ) : List<ProductDetailData>

    @Query("SELECT * FROM product WHERE productId IN (SELECT ignoredProductId FROM ignored_product) AND storeIdInProduct == :storeId LIMIT :limit OFFSET :offset")
    suspend fun loadIgnoredByPage(
        storeId: String,
        limit: Int,
        offset: Int
    ) : List<ProductDetailData>

    @Query("SELECT * FROM product WHERE productId IN (SELECT ignoredProductId FROM ignored_product) AND storeIdInProduct == :storeId")
    suspend fun loadAllIgnoredProduct(
        storeId: String,
    ) : List<ProductDetailData>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertFavoriteProduct(item: FavoriteProduct)

    @Query("DELETE FROM favorite_product WHERE favoriteProductId = :productId")
    suspend fun deleteFavoriteProduct(productId: String)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertIgnoredProduct(item: IgnoredProduct)

    @Query("DELETE FROM ignored_product WHERE ignoredProductId = :productId")
    suspend fun deleteIgnoredProduct(productId: String)



}
