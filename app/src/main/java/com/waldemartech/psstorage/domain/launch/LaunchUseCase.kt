package com.waldemartech.psstorage.domain.launch

import android.content.Context
import androidx.work.Data
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequest
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequest
import androidx.work.PeriodicWorkRequestBuilder
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
        val updateHongKongDealWorkRequest: PeriodicWorkRequest =
            PeriodicWorkRequestBuilder<UpdateDealWorker>(1, TimeUnit.DAYS)
                .setInitialDelay(1, TimeUnit.SECONDS)
                .setInputData(hongKongData)
                .build()

        val usData = Data.Builder()
            .putString(STORE_ID_KEY, US_STORE_ID)
            .build()
        val updateUSDealWorkRequest: PeriodicWorkRequest =
            PeriodicWorkRequestBuilder<UpdateDealWorker>(1, TimeUnit.DAYS)
                .setInitialDelay(1, TimeUnit.SECONDS)
                .setInputData(usData)
                .build()

        WorkManager.getInstance(context).enqueueUniquePeriodicWork(uniqueWorkName = HK_STORE_ID, existingPeriodicWorkPolicy = ExistingPeriodicWorkPolicy.KEEP, request = updateHongKongDealWorkRequest)
        WorkManager.getInstance(context).enqueueUniquePeriodicWork(uniqueWorkName = US_STORE_ID, existingPeriodicWorkPolicy = ExistingPeriodicWorkPolicy.KEEP, request = updateUSDealWorkRequest)

    }

}