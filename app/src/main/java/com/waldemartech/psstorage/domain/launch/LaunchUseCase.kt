package com.waldemartech.psstorage.domain.launch

import android.content.Context
import androidx.work.Data
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequest
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.waldemartech.psstorage.data.store.StoreConstants.HK_STORE_ID
import com.waldemartech.psstorage.data.store.StoreConstants.STORE_ID_KEY
import com.waldemartech.psstorage.data.store.StoreConstants.US_STORE_ID
import com.waldemartech.psstorage.data.store.UpdateDealWorker
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class LaunchUseCase @Inject constructor(
    @ApplicationContext private val context: Context
) {
    operator fun invoke() {
        onLaunch()
    }

    private fun onLaunch() {
        val hongKongData = Data.Builder()
            .putString(STORE_ID_KEY, HK_STORE_ID)
            .build()
        val updateHongKongDealWorkRequest: OneTimeWorkRequest =
            OneTimeWorkRequestBuilder<UpdateDealWorker>()
                .setInitialDelay(1, TimeUnit.SECONDS)
                .setInputData(hongKongData)
                .build()

        val usData = Data.Builder()
            .putString(STORE_ID_KEY, US_STORE_ID)
            .build()
        val updateUSDealWorkRequest: OneTimeWorkRequest =
            OneTimeWorkRequestBuilder<UpdateDealWorker>()
                .setInitialDelay(1, TimeUnit.SECONDS)
                .setInputData(usData)
                .build()

        WorkManager.getInstance(context).enqueueUniqueWork(uniqueWorkName = HK_STORE_ID, existingWorkPolicy = ExistingWorkPolicy.KEEP, request = updateHongKongDealWorkRequest)
        WorkManager.getInstance(context).enqueueUniqueWork(uniqueWorkName = US_STORE_ID, existingWorkPolicy = ExistingWorkPolicy.KEEP, request = updateUSDealWorkRequest)

    }

}