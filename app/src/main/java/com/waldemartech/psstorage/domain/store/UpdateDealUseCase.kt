package com.waldemartech.psstorage.domain.store

import android.content.Context
import androidx.work.Data
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkRequest
import com.waldemartech.psstorage.data.local.database.dao.DealDao
import com.waldemartech.psstorage.data.local.database.table.CurrentDeal
import com.waldemartech.psstorage.data.store.StoreConstants.DEAL_ID_KEY
import com.waldemartech.psstorage.data.store.StoreConstants.PAGE_INDEX_KEY
import com.waldemartech.psstorage.data.store.StoreConstants.STORE_ID_KEY
import com.waldemartech.psstorage.data.store.StoreData
import com.waldemartech.psstorage.data.store.UpdateSubDealRepository
import com.waldemartech.psstorage.data.store.UpdateDealRepository
import com.waldemartech.psstorage.data.store.UpdatePriceWorker
import dagger.hilt.android.qualifiers.ApplicationContext
import timber.log.Timber
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class UpdateDealUseCase @Inject constructor(
    @ApplicationContext private val context: Context,
    private val dealDao: DealDao,
    private val updateDealRepository: UpdateDealRepository,
    private val updateSubDealRepository: UpdateSubDealRepository
) {

    suspend operator fun invoke(storeData: StoreData) {
        dealDao.clearAllCurrentDeals()
        val result = updateDealRepository.updateDeal(storeData)
        Timber.i("result is $result")
        val finalDealList = result.subDealList.map { subDeal ->
            updateSubDealRepository.updateSubDeal(storeData, subDeal)
        }.filter {
            it.isSuccess
        }.map {
            it.getOrThrow()
        } + result.dealList

        finalDealList.forEach { deal ->
            if (!dealDao.hasDeal(deal.dealId)) {
                dealDao.insertDeal(deal)
            }
            dealDao.insertCurrentDeal(CurrentDeal(deal.dealId, storeIdInCurrentDeal = storeData.storeId))
        }
        launchUpdatePriceWorker(storeData)
    }

    private suspend fun launchUpdatePriceWorker(storeData: StoreData) {
        var delay = 1L
        dealDao.loadCurrentDealsByStore(storeData.storeId).forEach { deal ->
            val inputData = Data.Builder()
                .putString(STORE_ID_KEY, storeData.storeId)
                .putInt(PAGE_INDEX_KEY, 1)
                .putString(DEAL_ID_KEY, deal.deal.dealId)
                .build()
            val updateDealWorkRequest: WorkRequest =
                OneTimeWorkRequestBuilder<UpdatePriceWorker>()
                    .setInitialDelay(delay, TimeUnit.MINUTES)
                    .setInputData(inputData)
                    .build()
            WorkManager.getInstance(context).enqueue(updateDealWorkRequest)
            delay += 10L
        }
    }
}