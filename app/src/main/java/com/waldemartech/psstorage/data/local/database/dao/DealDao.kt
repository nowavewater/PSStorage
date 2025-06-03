package com.waldemartech.psstorage.data.local.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.waldemartech.psstorage.data.local.database.entity.CurrentDealData
import com.waldemartech.psstorage.data.local.database.table.CurrentDeal
import com.waldemartech.psstorage.data.local.database.table.Deal

@Dao
interface DealDao {

    @Transaction
    @Query("SELECT * FROM current_deal WHERE storeIdInCurrentDeal == :storeId")
    suspend fun loadCurrentDealsByStore(storeId: String): List<CurrentDealData>

    @Query("SELECT EXISTS(SELECT 1 FROM deal WHERE dealId == :id)")
    suspend fun hasDeal(id: String) : Boolean

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertDeal(item: Deal)

    @Query("DELETE FROM current_deal")
    suspend fun clearAllCurrentDeals()

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertCurrentDeal(item: CurrentDeal)

}