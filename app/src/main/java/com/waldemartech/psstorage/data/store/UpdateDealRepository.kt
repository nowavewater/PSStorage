package com.waldemartech.psstorage.data.store

import android.content.Context
import androidx.work.Data
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkRequest
import com.mohamedrejeb.ksoup.html.parser.KsoupHtmlHandler
import com.mohamedrejeb.ksoup.html.parser.KsoupHtmlParser
import com.waldemartech.psstorage.data.api.ApiConstants.BASE_URL
import com.waldemartech.psstorage.data.api.ApiConstants.HTML_ATTRIBUTE_NAME_ID
import com.waldemartech.psstorage.data.api.ApiConstants.HTML_TAG_SCRIPT
import com.waldemartech.psstorage.data.api.ApiConstants.PS_STORE_DEAL_ID
import com.waldemartech.psstorage.data.api.ApiDataSource
import com.waldemartech.psstorage.data.api.entity.DealResponse
import com.waldemartech.psstorage.data.local.database.dao.DealDao
import com.waldemartech.psstorage.data.local.database.table.CurrentDeal
import com.waldemartech.psstorage.data.local.database.table.Deal
import com.waldemartech.psstorage.data.store.StoreConstants.DEAL_ID_KEY
import com.waldemartech.psstorage.data.store.StoreConstants.PAGE_INDEX_KEY
import com.waldemartech.psstorage.data.store.StoreConstants.STORE_ID_KEY
import com.waldemartech.psstorage.data.store.StoreConstants.excludeLocalizedName
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import org.json.JSONObject
import timber.log.Timber
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class UpdateDealRepository @Inject constructor(
    @ApplicationContext private val context: Context,
    private val dealDao: DealDao,
    private val apiDataSource: ApiDataSource
) {

    private val scope = CoroutineScope(Dispatchers.IO)

    suspend fun updateDeal(storeData: StoreData) {
        val url = "$BASE_URL${storeData.storeId}/pages/deals"
        try {
            val headers = HashMap<String, String>()
            val params = HashMap<String, String>()
            val responseBody = apiDataSource.downloadFile(
                url = url,
                params = params,
                headers = headers
            )
            processResponse(responseBody.string(), storeData)
            /*processDealResponse(
                responseBody.string()
            )*/
            //    Timber.i(responseBody.string())
        } catch (throwable: Throwable) {
            //    Timber.e("on network exception $throwable")
        }

    }

    private suspend fun processResponse(response: String, storeData: StoreData) {
        val dealHandler = object : KsoupHtmlHandler {
            var watchingState : WatchingState = WatchingState.None
            override fun onOpenTag(name: String, attributes: Map<String, String>, isImplied: Boolean) {
                if (watchingState == WatchingState.Finished) {
                    return
                }
                if (name == HTML_TAG_SCRIPT) {
                    attributes.forEach { name, value ->
                        if (name == HTML_ATTRIBUTE_NAME_ID  && value == PS_STORE_DEAL_ID) {
                            watchingState = WatchingState.Watching
                        }
                    }
                }
            }

            override fun onText(text: String) {
                super.onText(text)
                if (watchingState == WatchingState.Finished) {
                    return
                }
                if (watchingState == WatchingState.Watching) {
                    Timber.i("on deal $text")
                    watchingState = WatchingState.Finished
                    scope.launch {
                        processText(text, storeData)
                        launchUpdatePriceWorker(storeData)
                    }
                }
            }
        }
        val ksoupHtmlParser = KsoupHtmlParser(
            handler = dealHandler,
        )
        // Pass the HTML to the parser (It is going to parse the HTML and call the callbacks)
        ksoupHtmlParser.write(response)
        // Close the parser when you are done
        ksoupHtmlParser.end()
    }

    private suspend fun processText(text: String, storeData: StoreData) {
        val excludeSet = excludeLocalizedName()
        dealDao.clearAllCurrentDeals()
        try {
            val json = JSONObject(text)
            if (json.has("props")) {
                val props = json.getJSONObject("props")
                if (props.has("apolloState")) {
                    val apolloState = props.getJSONObject("apolloState")
                    apolloState.keys().forEach { key ->
                        if (key.contains("EMSImageComponent")) {
                            val dealResponse = Json{ ignoreUnknownKeys = true }.decodeFromString<DealResponse>(apolloState.getJSONObject(key).toString())
                            dealResponse.link.localizedName?.let { localizedName ->
                                if (!excludeSet.contains(localizedName) ) {
                                    if (!dealDao.hasDeal(dealResponse.link.target)) {
                                        dealDao.insertDeal(Deal(dealId = dealResponse.link.target, imageUrl = dealResponse.imageUrl, localizedName = dealResponse.link.localizedName, storeIdInDeal = storeData.storeId))
                                    }
                                    dealDao.insertCurrentDeal(CurrentDeal(dealResponse.link.target, storeIdInCurrentDeal = storeData.storeId))
                                }
                            }
                            /*if (dealLinkMap.contains(dealResponse.link.target)) {

                                dealResponse.link.localizedName?.let { localized ->
                                    dealLinkMap[dealResponse.link.target]?.let { dealLink ->
                                        Timber.i("before deal ${dealLink.id} ")

                                    }
                                }
                            }*/
                        }
                    }
                }
            }
        } catch (throwable: Throwable) {
            Timber.e("on parse exception ${throwable}")
        }

    }

    private suspend fun launchUpdatePriceWorker(storeData: StoreData) {
        var delay = 1L
        dealDao.getDeals().forEach { deal ->
            val inputData = Data.Builder()
                .putString(STORE_ID_KEY, storeData.storeId)
                .putInt(PAGE_INDEX_KEY, 1)
                .putString(DEAL_ID_KEY, deal.dealId)
                .build()
            val updateDealWorkRequest: WorkRequest =
                OneTimeWorkRequestBuilder<UpdatePriceWorker>()
                    .setInitialDelay(delay, TimeUnit.SECONDS)
                    .setInputData(inputData)
                    .build()
            WorkManager.getInstance(context).enqueue(updateDealWorkRequest)
            delay += 20L
        }
    }
}

enum class WatchingState {
    None, Finished, Watching
}