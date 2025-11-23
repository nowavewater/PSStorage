package com.waldemartech.psstorage.data.local.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.waldemartech.psstorage.data.local.database.table.AllTimeLowPrice
import com.waldemartech.psstorage.data.local.database.table.PriceHistory
import com.waldemartech.psstorage.data.store.StoreId

@Dao
interface PriceDao {

    /*@Query("SELECT * FROM deal")
    suspend fun getDeals(): List<Deal>*/

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertPriceHistory(price: PriceHistory)

    @Query("SELECT EXISTS(SELECT 1 FROM price_history WHERE productIdPriceRef == :productId AND dealIdPriceRef == :dealId AND storeIdInPriceHistory == :storeId)")
    suspend fun hasPrice(productId: String, dealId: String, storeId: String) : Boolean

    @Update
    suspend fun updateAllTimeLowPrice(price: AllTimeLowPrice)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAllTimeLowPrice(price: AllTimeLowPrice)

    @Query("SELECT * FROM all_time_low_price WHERE productIdInLowPrice == :productId AND storeIdInLowPrice == :storeId")
    suspend fun getAllTimeLowPrice(productId: String, storeId: String) : AllTimeLowPrice?

    @Query("SELECT EXISTS(SELECT 1 FROM all_time_low_price WHERE productIdInLowPrice == :productId AND storeIdInLowPrice == :storeId)")
    suspend fun hasAllTimeLowPrice(productId: String, storeId: String) : Boolean
}