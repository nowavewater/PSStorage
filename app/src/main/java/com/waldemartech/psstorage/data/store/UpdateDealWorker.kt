package com.waldemartech.psstorage.data.store

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import androidx.work.hasKeyWithValueOfType
import com.waldemartech.psstorage.data.base.SharedConstants.EMPTY_STRING
import com.waldemartech.psstorage.data.store.StoreConstants.STORE_ID_KEY
import com.waldemartech.psstorage.domain.store.UpdateDealUseCase
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class UpdateDealWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val updateDealUseCase: UpdateDealUseCase
) : CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {
        if (!inputData.hasKeyWithValueOfType<String>(STORE_ID_KEY)) {
            return Result.failure()
        }
        val storeId = inputData.getString(STORE_ID_KEY) ?: EMPTY_STRING
        updateDealUseCase(storeData = StoreData(storeId = storeId))
        return Result.success()
    }

}