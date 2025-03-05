/*
 * Copyright (C) 2022 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.waldemartech.psstorage.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.waldemartech.psstorage.data.local.database.dao.DealDao
import com.waldemartech.psstorage.data.local.database.dao.PlatformDao
import com.waldemartech.psstorage.data.local.database.dao.PriceDao
import com.waldemartech.psstorage.data.local.database.dao.ProductDao
import com.waldemartech.psstorage.data.local.database.table.CurrentDeal
import com.waldemartech.psstorage.data.local.database.table.Deal
import com.waldemartech.psstorage.data.local.database.table.FavoriteProduct
import com.waldemartech.psstorage.data.local.database.table.IgnoredProduct
import com.waldemartech.psstorage.data.local.database.table.Platform
import com.waldemartech.psstorage.data.local.database.table.PriceHistory
import com.waldemartech.psstorage.data.local.database.table.Product
import com.waldemartech.psstorage.data.local.database.table.ProductPlatformCrossRef

@Database(
    entities = [
        Deal::class,
        CurrentDeal::class,
        Platform::class,
        PriceHistory::class,
        Product::class,
        ProductPlatformCrossRef::class,
        FavoriteProduct::class,
        IgnoredProduct::class
               ],
    version = 1
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun dealDao(): DealDao

    abstract fun priceDao(): PriceDao

    abstract fun platformDao(): PlatformDao

    abstract fun productDao(): ProductDao

}
