package com.waldemartech.psstorage.data.local.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.waldemartech.psstorage.data.local.database.table.Platform

@Dao
interface PlatformDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertPlatForm(item: Platform)

    @Query("SELECT EXISTS(SELECT 1 FROM product WHERE name == :name)")
    suspend fun hasPlatForm(name: String) : Boolean

    @Query("SELECT platformId FROM platform WHERE name = :platformName LIMIT 1")
    suspend fun getPlatformIdByName(platformName: String): Long

}