package com.waldemartech.psstorage.domain.launch

import android.content.Context
import androidx.work.Data
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkRequest
import com.waldemartech.psstorage.data.store.StoreConstants.HK_STORE_ID
import com.waldemartech.psstorage.data.store.StoreConstants.STORE_ID_KEY
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
        val updateDealWorkRequest: WorkRequest =
            OneTimeWorkRequestBuilder<UpdateDealWorker>()
                .setInitialDelay(1, TimeUnit.SECONDS)
                .setInputData(hongKongData)
                .build()
    //    WorkManager.getInstance(context).enqueue(updateDealWorkRequest)
    }

}