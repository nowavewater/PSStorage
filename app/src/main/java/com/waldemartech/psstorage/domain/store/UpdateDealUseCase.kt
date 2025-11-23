package com.waldemartech.psstorage.domain.store

import android.content.Context
import androidx.work.Data
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkRequest
import com.waldemartech.psstorage.data.base.SharedConstants.COLON_SIGN
import com.waldemartech.psstorage.data.local.database.dao.DealDao
import com.waldemartech.psstorage.data.local.database.dao.ProductDao
import com.waldemartech.psstorage.data.local.database.table.CurrentDeal
import com.waldemartech.psstorage.data.store.StoreConstants.DEAL_ID_KEY
import com.waldemartech.psstorage.data.store.StoreConstants.PAGE_INDEX_KEY
import com.waldemartech.psstorage.data.store.StoreConstants.STORE_ID_KEY
import com.waldemartech.psstorage.data.store.StoreData
import com.waldemartech.psstorage.data.store.UpdateSubDealRepository
import com.waldemartech.psstorage.data.store.UpdateDealRepository
import com.waldemartech.psstorage.data.store.UpdatePriceRepository
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
        dealDao.clearStoreCurrentDeals(storeData.storeId)
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
            val dealId = deal.dealId
            val updatedDeal = deal.copy(dealId = dealId)
            if (!dealDao.hasDeal(dealId)) {
                dealDao.insertDeal(updatedDeal)
            }
            dealDao.insertCurrentDeal(CurrentDeal(dealId, storeIdInCurrentDeal = storeData.storeId))
        }
        launchUpdatePriceWorker(storeData)
    }

    private suspend fun launchUpdatePriceWorker(storeData: StoreData) {
        var delay = 1L
        /*val dealsRequireUpdate = dealDao.loadCurrentDealsByStore(storeData.storeId).filter { deal ->
            val countInDatabase = productDao.loadAllProductInDealCount(
                storeId = storeData.storeId,
                dealId = deal.deal.dealId
            )
            val countTotal = updatePriceRepository.loadTotalCountInDeal(
                storeData = storeData
            )
        }*/
        dealDao.loadCurrentDealsByStore(storeData.storeId).forEach { currentDeal ->
            var pageIndex = currentDeal.deal.currentPageIndex

            val inputData = Data.Builder()
                .putString(STORE_ID_KEY, storeData.storeId)
                .putInt(PAGE_INDEX_KEY, pageIndex)
                .putString(DEAL_ID_KEY, currentDeal.deal.dealId)
                .build()
            val updateDealWorkRequest: WorkRequest =
                OneTimeWorkRequestBuilder<UpdatePriceWorker>()
                    .setInputData(inputData)
                    .build()

            WorkManager.getInstance(context).enqueue(updateDealWorkRequest)
            delay += 10L
        }
    }
}