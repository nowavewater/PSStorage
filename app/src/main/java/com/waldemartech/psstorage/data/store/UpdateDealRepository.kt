package com.waldemartech.psstorage.data.store

import com.waldemartech.psstorage.data.api.ApiConstants.BASE_URL
import com.waldemartech.psstorage.data.api.ApiConstants.EXCLUDED_LOCALIZED_NAME
import com.waldemartech.psstorage.data.api.ApiConstants.HTML_ATTRIBUTE_NAME_CLASS
import com.waldemartech.psstorage.data.api.ApiConstants.HTML_ATTRIBUTE_NAME_DATA_TARGET
import com.waldemartech.psstorage.data.api.ApiConstants.HTML_ATTRIBUTE_NAME_HREF
import com.waldemartech.psstorage.data.api.ApiConstants.HTML_ATTRIBUTE_NAME_ID
import com.waldemartech.psstorage.data.api.ApiConstants.HTML_TAG_A
import com.waldemartech.psstorage.data.api.ApiConstants.HTML_TAG_DIV
import com.waldemartech.psstorage.data.api.ApiConstants.HTML_TAG_SCRIPT
import com.waldemartech.psstorage.data.api.ApiConstants.IMAGE_COMPONENT
import com.waldemartech.psstorage.data.api.ApiConstants.LINK_TYPE_VIEW
import com.waldemartech.psstorage.data.api.ApiConstants.PS_STORE_DEAL_ALLY_INDICATOR_CLASS
import com.waldemartech.psstorage.data.api.ApiConstants.PS_STORE_DEAL_ID
import com.waldemartech.psstorage.data.api.ApiConstants.PS_STORE_DEAL_LINK_CLASS
import com.waldemartech.psstorage.data.api.ApiConstants.PS_STORE_KEY_WORD_VIEW
import com.waldemartech.psstorage.data.api.ApiDataSource
import com.waldemartech.psstorage.data.local.database.table.Deal
import com.waldemartech.psstorage.data.store.DealConstants.processDealResponse
import com.waldemartech.psstorage.data.store.DealConstants.processDealText
import com.waldemartech.psstorage.data.store.entity.DealUpdateResult
import com.waldemartech.psstorage.data.store.entity.SubDealData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import timber.log.Timber
import javax.inject.Inject

class UpdateDealRepository @Inject constructor(
    private val apiDataSource: ApiDataSource
) {
    private val scope = CoroutineScope(Dispatchers.IO)

    suspend fun updateDeal(storeData: StoreData): DealUpdateResult {
        val dealList = mutableListOf<Deal>()
        val subDealList = mutableListOf<SubDealData>()

        val excludeSet = EXCLUDED_LOCALIZED_NAME

        val url = "$BASE_URL${storeData.storeId}/pages/deals"
        try {
            val headers = HashMap<String, String>()
            val params = HashMap<String, String>()
            val responseBody = apiDataSource.downloadFile(
                url = url,
                params = params,
                headers = headers
            )

            var dealLinkData = SubDealData()

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
                        Timber.i("on deal $text")
                        processDealText(
                            text = text,
                            watchingKey = IMAGE_COMPONENT,
                            onDealResponse = { dealResponse ->
                                dealResponse.link?.let { link ->
                                    link.localizedName?.let { localizedName ->
                                        if (!excludeSet.contains(localizedName)) {
                                            dealList.add(
                                                Deal(
                                                    dealId = link.target,
                                                    imageUrl = dealResponse.imageUrl,
                                                    localizedName = localizedName,
                                                    storeIdInDeal = storeData.storeId
                                                )
                                            )
                                        }
                                    }
                                    if (link.type == LINK_TYPE_VIEW) {
                                        dealLinkData = dealLinkData.copy(
                                            imageUrl = dealResponse.imageUrl
                                        )
                                    } else {
                                        Timber.i("link type is not view ${link.type}")
                                    }
                                }
                            }
                        )
                    }
                )
            )

            val subControl = ResponseProcessControl(
                startData = ProcessState.Tag(
                    tagData = TagData(
                        tagName = HTML_TAG_A,
                        attributeName = HTML_ATTRIBUTE_NAME_CLASS,
                        attributeValue = PS_STORE_DEAL_LINK_CLASS,
                    ),
                    onTag = { attributes ->
                        attributes[HTML_ATTRIBUTE_NAME_HREF]?.let { href ->
                            if (href.contains(PS_STORE_KEY_WORD_VIEW)) {
                                dealLinkData = dealLinkData.copy(link = href)
                            }
                        }
                    }
                ),
                watchingState = ProcessState.Tag(
                    tagData = TagData(
                        tagName = HTML_TAG_DIV,
                        attributeName = HTML_ATTRIBUTE_NAME_CLASS,
                        attributeValue = PS_STORE_DEAL_ALLY_INDICATOR_CLASS,
                    ),
                    onTag = { attributes ->
                        attributes[HTML_ATTRIBUTE_NAME_DATA_TARGET]?.let { dataTarget ->
                            dealLinkData = dealLinkData.copy(
                                target = dataTarget
                            )
                        }
                    }
                )
            )

            val responseText = responseBody.string()

            processDealResponse(
                control = control,
                response = responseText,
            )

            processDealResponse(
                control = subControl,
                response = responseText,
            )

            subDealList.add(dealLinkData)

        } catch (throwable: Throwable) {
            Timber.e("on network exception $throwable")
        }

        return DealUpdateResult(dealList, subDealList)
    }



}

enum class WatchingState {
    None, Finished, Watching
}
