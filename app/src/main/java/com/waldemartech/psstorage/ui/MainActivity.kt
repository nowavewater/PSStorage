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

package com.waldemartech.psstorage.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.waldemartech.psstorage.data.store.StoreConstants.DEAL_ID_KEY
import com.waldemartech.psstorage.data.store.StoreConstants.STORE_ID_KEY
import com.waldemartech.psstorage.ui.MainNavConstants.DEST_DEAL_DETAIL
import com.waldemartech.psstorage.ui.MainNavConstants.DEST_DEAL_LIST
import com.waldemartech.psstorage.ui.MainNavConstants.DEST_FAVORITE_LIST
import com.waldemartech.psstorage.ui.MainNavConstants.DEST_IGNORED_LIST
import com.waldemartech.psstorage.ui.MainNavConstants.DEST_PRODUCT_DETAIL
import com.waldemartech.psstorage.ui.MainNavConstants.DEST_PRODUCT_LIST
import com.waldemartech.psstorage.ui.MainNavConstants.DEST_STORE_DETAIL
import com.waldemartech.psstorage.ui.MainNavConstants.DEST_STORE_LIST
import com.waldemartech.psstorage.ui.MainNavConstants.buildDoubleArgsDest
import com.waldemartech.psstorage.ui.MainNavConstants.buildSingleArgsDest
import dagger.hilt.android.AndroidEntryPoint
import com.waldemartech.psstorage.ui.widget.base.theme.MyApplicationTheme
import com.waldemartech.psstorage.ui.deal.detail.DealDetailScreen
import com.waldemartech.psstorage.ui.deal.list.DealListScreen
import com.waldemartech.psstorage.ui.product.detail.ProductDetailScreen
import com.waldemartech.psstorage.ui.product.favorite.FavoriteListScreen
import com.waldemartech.psstorage.ui.product.ignored.IgnoredListScreen
import com.waldemartech.psstorage.ui.product.list.ProductListScreen
import com.waldemartech.psstorage.ui.store.detail.StoreDetailScreen
import com.waldemartech.psstorage.ui.store.list.StoreListScreen

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

            val navController = rememberNavController()

            MyApplicationTheme(
                navController = navController
            ) {
                Surface(
                    modifier = Modifier.fillMaxSize().padding(vertical = 40.dp),
                    color = MaterialTheme.colorScheme.background
                ) {
                    NavHost(navController = navController, startDestination = DEST_STORE_LIST) {
                        composable(DEST_STORE_LIST) { backStackEntry ->
                            StoreListScreen()
                        }
                        composable(buildSingleArgsDest(DEST_STORE_DETAIL, STORE_ID_KEY)) { backStackEntry ->
                            val storeId = backStackEntry.arguments?.getString(STORE_ID_KEY)
                            requireNotNull(storeId)
                            StoreDetailScreen(storeId)
                        }
                        composable(buildSingleArgsDest(DEST_DEAL_LIST, STORE_ID_KEY)) { backStackEntry ->
                            val storeId = backStackEntry.arguments?.getString(STORE_ID_KEY)
                            requireNotNull(storeId)
                            DealListScreen(storeId)
                        }
                        composable(buildDoubleArgsDest(DEST_DEAL_DETAIL, STORE_ID_KEY, DEAL_ID_KEY) ) { backStackEntry ->
                            val storeId = backStackEntry.arguments?.getString(STORE_ID_KEY)
                            val dealId = backStackEntry.arguments?.getString(DEAL_ID_KEY)
                            requireNotNull(storeId)
                            requireNotNull(dealId)
                            DealDetailScreen(storeId, dealId)
                        }
                        composable(DEST_PRODUCT_LIST) { backStackEntry ->
                            //    val downloadId = backStackEntry.arguments?.getString(DOWNLOAD_ID_KEY)
                            ProductListScreen()
                        }
                        composable(DEST_PRODUCT_DETAIL) { backStackEntry ->
                            //    val downloadId = backStackEntry.arguments?.getString(DOWNLOAD_ID_KEY)
                            ProductDetailScreen()
                        }
                        composable(buildSingleArgsDest(DEST_IGNORED_LIST, STORE_ID_KEY)) { backStackEntry ->
                            val storeId = backStackEntry.arguments?.getString(STORE_ID_KEY)
                            requireNotNull(storeId)
                            IgnoredListScreen(storeId)
                        }

                        composable(buildSingleArgsDest(DEST_FAVORITE_LIST, STORE_ID_KEY)) { backStackEntry ->
                            val storeId = backStackEntry.arguments?.getString(STORE_ID_KEY)
                            requireNotNull(storeId)
                            FavoriteListScreen(storeId)
                        }
                    }
                }
            }
        }
    }
}
