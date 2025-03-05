package com.waldemartech.psstorage.data.local.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.waldemartech.psstorage.data.local.database.entity.CurrentDealData
import com.waldemartech.psstorage.data.local.database.table.Product

@Dao
interface ProductDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertProduct(item: Product)

    @Query("SELECT EXISTS(SELECT 1 FROM product WHERE productId == :id)")
    suspend fun hasProduct(id: String) : Boolean

  //  @Query("SELECT * FROM product WHERE storeIdInProduct == :storeId AND ")
 //   suspend fun loadProductByPage(storeId: String, dealId: String) : List<Product>

}