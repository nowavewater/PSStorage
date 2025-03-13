package com.waldemartech.psstorage.data.local.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.waldemartech.psstorage.data.local.database.entity.CurrentDealData
import com.waldemartech.psstorage.data.local.database.entity.ProductPriceInDeal
import com.waldemartech.psstorage.data.local.database.table.CurrentDeal
import com.waldemartech.psstorage.data.local.database.table.FavoriteProduct
import com.waldemartech.psstorage.data.local.database.table.IgnoredProduct
import com.waldemartech.psstorage.data.local.database.table.Product

@Dao
interface ProductDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertProduct(item: Product)

    @Query("SELECT EXISTS(SELECT 1 FROM product WHERE productId == :id)")
    suspend fun hasProduct(id: String) : Boolean

    @Query("SELECT * FROM price_history INNER JOIN product ON productId = productIdPriceRef WHERE storeIdInProduct == :storeId AND productId NOT IN (SELECT favoriteProductId FROM favorite_product) AND productId NOT IN (SELECT ignoredProductId FROM ignored_product) AND dealIdPriceRef = :dealId LIMIT :limit OFFSET :offset")
    suspend fun loadProductByPage(
        storeId: String,
        dealId: String,
        limit: Int,
        offset: Int
    ) : List<ProductPriceInDeal>

    @Query("SELECT * FROM product WHERE productId IN (SELECT favoriteProductId FROM favorite_product) AND storeIdInProduct == :storeId LIMIT :limit OFFSET :offset")
    suspend fun loadFavoriteByPage(
        storeId: String,
        limit: Int,
        offset: Int
    ) : List<Product>

    @Query("SELECT * FROM product WHERE productId IN (SELECT ignoredProductId FROM ignored_product) AND storeIdInProduct == :storeId LIMIT :limit OFFSET :offset")
    suspend fun loadIgnoredByPage(
        storeId: String,
        limit: Int,
        offset: Int
    ) : List<Product>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertFavoriteProduct(item: FavoriteProduct)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertIgnoredProduct(item: IgnoredProduct)

}