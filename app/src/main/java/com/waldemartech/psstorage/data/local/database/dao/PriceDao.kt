package com.waldemartech.psstorage.data.local.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.waldemartech.psstorage.data.local.database.table.PriceHistory

@Dao
interface PriceDao {

    /*@Query("SELECT * FROM deal")
    suspend fun getDeals(): List<Deal>*/

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertPriceHistory(item: PriceHistory)

    @Query("SELECT EXISTS(SELECT 1 FROM price_history WHERE productIdPriceRef == :productId AND dealIdPriceRef == :dealId )")
    suspend fun hasPrice(productId: String, dealId: String) : Boolean
}