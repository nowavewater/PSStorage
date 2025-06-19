package com.waldemartech.psstorage.data.store

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import androidx.work.hasKeyWithValueOfType
import com.waldemartech.psstorage.data.base.SharedConstants.EMPTY_STRING
import com.waldemartech.psstorage.data.base.SharedConstants.NOT_AVAILABLE
import com.waldemartech.psstorage.data.store.StoreConstants.DEAL_ID_KEY
import com.waldemartech.psstorage.data.store.StoreConstants.PAGE_INDEX_KEY
import com.waldemartech.psstorage.data.store.StoreConstants.STORE_ID_KEY
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class UpdatePriceWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val updatePriceRepository: UpdatePriceRepository
) : CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {
        if (!inputData.hasKeyWithValueOfType<String>(STORE_ID_KEY) ||
            !inputData.hasKeyWithValueOfType<String>(DEAL_ID_KEY) ||
            !inputData.hasKeyWithValueOfType<Int>(PAGE_INDEX_KEY)) {
            return Result.failure()
        }
        val storeId = inputData.getString(STORE_ID_KEY) ?: EMPTY_STRING
        val dealId = inputData.getString(DEAL_ID_KEY) ?: EMPTY_STRING
        val pageIndex = inputData.getInt(PAGE_INDEX_KEY, NOT_AVAILABLE)
        updatePriceRepository.updatePrice(
            UpdatePriceInput(
                dealId = DealId(dealId),
                storeId = StoreId(storeId),
                pageIndex = pageIndex
            )
        )
        return Result.success()
    }

}