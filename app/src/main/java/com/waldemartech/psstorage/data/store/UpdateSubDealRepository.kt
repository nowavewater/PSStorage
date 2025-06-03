package com.waldemartech.psstorage.data.store

import com.waldemartech.psstorage.data.api.ApiConstants.BASE_URL
import com.waldemartech.psstorage.data.api.ApiConstants.DAYS_OF_PLAY_LOCALIZED_NAME
import com.waldemartech.psstorage.data.api.ApiConstants.HTML_ATTRIBUTE_NAME_ID
import com.waldemartech.psstorage.data.api.ApiConstants.HTML_TAG_SCRIPT
import com.waldemartech.psstorage.data.api.ApiConstants.PS_STORE_DEAL_ID
import com.waldemartech.psstorage.data.api.ApiConstants.TEXT_COMPONENT
import com.waldemartech.psstorage.data.api.ApiDataSource
import com.waldemartech.psstorage.data.local.database.table.Deal
import com.waldemartech.psstorage.data.store.DealConstants.processResponse
import com.waldemartech.psstorage.data.store.DealConstants.processText
import com.waldemartech.psstorage.data.store.entity.SubDealData
import timber.log.Timber
import javax.inject.Inject

class UpdateSubDealRepository @Inject constructor(
    private val apiDataSource: ApiDataSource,
) {

    suspend fun updateSubDeal(
        storeData: StoreData,
        subDealData: SubDealData
    ): Result<Deal> {
        var result = Result.failure<Deal>(IllegalArgumentException())
        val url = "$BASE_URL${subDealData.link}"
        try {
            val headers = HashMap<String, String>()
            val params = HashMap<String, String>()
            val responseBody = apiDataSource.downloadFile(
                url = url,
                params = params,
                headers = headers
            )

            val control = ResponseProcessControl(
                startData = ProcessState.Tag(
                    tagData = TagData(
                        tagName = HTML_TAG_SCRIPT,
                        attributeName = HTML_ATTRIBUTE_NAME_ID,
                        attributeValue = PS_STORE_DEAL_ID
                    )
                ),
                watchingState = ProcessState.Text(
                    onText = { text ->
                        processText(
                            text = text,
                            watchingKey = TEXT_COMPONENT,
                            onDealResponse = { dealResponse ->
                                dealResponse.link?.let { link ->
                                    link.localizedName?.let { localizedName ->
                                        if (localizedName == DAYS_OF_PLAY_LOCALIZED_NAME) {
                                            result = Result.success(
                                                Deal(
                                                    dealId = link.target,
                                                    imageUrl = subDealData.imageUrl,
                                                    localizedName = localizedName,
                                                    storeIdInDeal = storeData.storeId
                                                )
                                            )
                                        }
                                    }
                                }
                            }
                        )
                    }
                )
            )

            val responseText = responseBody.string()

            processResponse(
                control = control,
                response = responseText,
            )
        } catch (throwable: Throwable) {
            Timber.e("on network exception $throwable")
        }
        return result
    }

}