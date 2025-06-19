package com.waldemartech.psstorage.domain.store

import android.content.Context
import androidx.work.Data
import androidx.work.OneTimeWorkRequest
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.waldemartech.psstorage.data.store.StoreConstants.HK_STORE_ID
import com.waldemartech.psstorage.data.store.StoreConstants.STORE_ID_KEY
import com.waldemartech.psstorage.data.store.StoreConstants.US_STORE_ID
import com.waldemartech.psstorage.data.store.UpdateDealWorker
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class SyncDealUseCase @Inject constructor(
    @ApplicationContext private val context: Context
) {
    suspend operator fun invoke() {
        val hongKongData = Data.Builder()
            .putString(STORE_ID_KEY, HK_STORE_ID)
            .build()
        val usData = Data.Builder()
            .putString(STORE_ID_KEY, US_STORE_ID)
            .build()
        val updateHongKongDealWorkRequest: OneTimeWorkRequest =
            OneTimeWorkRequestBuilder<UpdateDealWorker>()
                .setInputData(hongKongData)
                .build()

        val updateUsDealWorkRequest: OneTimeWorkRequest =
            OneTimeWorkRequestBuilder<UpdateDealWorker>()
                .setInputData(usData)
                .build()

        WorkManager.getInstance(context).enqueue(updateHongKongDealWorkRequest)
        WorkManager.getInstance(context).enqueue(updateUsDealWorkRequest)
    }

}