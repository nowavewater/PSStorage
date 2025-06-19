package com.waldemartech.psstorage.domain.store

import android.content.Context
import androidx.work.Data
import androidx.work.OneTimeWorkRequest
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.waldemartech.psstorage.data.store.StoreConstants.STORE_ID_KEY
import com.waldemartech.psstorage.data.store.UpdateDealWorker
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class SyncDealUseCase @Inject constructor(
    @ApplicationContext private val context: Context
) {
    suspend operator fun invoke(storeId: String) {
        val inputData = Data.Builder()
            .putString(STORE_ID_KEY, storeId)
            .build()

        val updateDealWorkRequest: OneTimeWorkRequest =
            OneTimeWorkRequestBuilder<UpdateDealWorker>()
                .setInputData(inputData)
                .build()

        WorkManager.getInstance(context).enqueue(updateDealWorkRequest)
    }

}