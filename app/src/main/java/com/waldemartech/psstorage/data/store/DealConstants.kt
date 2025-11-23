package com.waldemartech.psstorage.data.store

import com.mohamedrejeb.ksoup.html.parser.KsoupHtmlHandler
import com.mohamedrejeb.ksoup.html.parser.KsoupHtmlParser
import com.waldemartech.psstorage.data.api.entity.DealResponse
import com.waldemartech.psstorage.data.api.entity.ProductResponse
import kotlinx.serialization.json.Json
import org.json.JSONObject
import timber.log.Timber

object DealConstants {

    suspend fun processDealResponse(
        control: ResponseProcessControl,
        response: String,
    ) {
        val dealHandler = object : KsoupHtmlHandler {
            var watchingState : WatchingState = WatchingState.None
            override fun onOpenTag(name: String, attributes: Map<String, String>, isImplied: Boolean) {
                if (watchingState == WatchingState.Finished) {
                    return
                }
                if (control.startData is ProcessState.Tag) {
                    val tagData = control.startData.tagData
                    if (name == tagData.tagName && attributes[tagData.attributeName] == tagData.attributeValue) {
                        watchingState = WatchingState.Watching
                        control.startData.onTag?.invoke(attributes)
                    //    Timber.i("start to watching tag $name attributes name ${attributes[tagData.attributeName]} attribute value ${attributes[tagData.attributeValue]}")
                    }
                }
                if (watchingState == WatchingState.Watching && control.watchingState is ProcessState.Tag) {
                    val tagData = control.watchingState.tagData
                    if (name == tagData.tagName && attributes[tagData.attributeName] == tagData.attributeValue) {
                        watchingState = WatchingState.Finished
                        control.watchingState.onTag?.invoke(attributes)
                    }
                }
            }

            override fun onText(text: String) {
                super.onText(text)
                if (watchingState == WatchingState.Finished) {
                    return
                }
                if (control.startData is ProcessState.Text) {
                    watchingState = WatchingState.Watching
                    control.startData.onText?.invoke(text)
                }
                if (watchingState == WatchingState.Watching && control.watchingState is ProcessState.Text) {
                    watchingState = WatchingState.Finished
                    control.watchingState.onText?.invoke(text)
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

    fun processDealText(
        text: String,
        watchingKey: String,
        onDealResponse: (DealResponse) -> Unit
    ) {
        try {
            val json = JSONObject(text)
            if (json.has("props")) {
                val props = json.getJSONObject("props")
                if (props.has("apolloState")) {
                    val apolloState = props.getJSONObject("apolloState")
                    apolloState.keys().forEach { key ->
                        if (key.contains(watchingKey)) {
                            val dealResponse = Json{ ignoreUnknownKeys = true }.decodeFromString<DealResponse>(apolloState.getJSONObject(key).toString())
                            onDealResponse(dealResponse)
                            /*if (dealLinkMap.contains(dealResponse.link.target)) { 61b9ab7f

                                dealResponse.link.localizedName?.let { localized ->
                                    dealLinkMa+p[dealResponse.link.target]?.let { dealLink ->
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

    suspend fun processPriceText(
        text: String,
        watchingKey: String,
        onProductResponse: suspend (ProductResponse) -> Unit
    ) {
        try {
            val json = JSONObject(text)
            if (json.has("props")) {
                val props = json.getJSONObject("props")
                if (props.has("apolloState")) {
                    val apolloState = props.getJSONObject("apolloState")
                    apolloState.keys().forEach { key ->
                        //    Timber.i("key is ${key}")
                        if (key.contains(watchingKey)) {
                            //    Timber.i("for each product")
                            val productResponse = Json{ ignoreUnknownKeys = true }.decodeFromString<ProductResponse>(apolloState.getJSONObject(key).toString())
                            onProductResponse(productResponse)
                        }
                    }
                }
            }
        } catch (throwable: Throwable) {
            Timber.e("on parse exception ${throwable}")
        }
    }
}

data class ResponseProcessControl(
    val startData: ProcessState,
    val watchingState: ProcessState
)

sealed class ProcessState {
    data class Tag(
        val tagData: TagData,
        val onTag: ((Map<String, String>) -> Unit)? = null
    ) : ProcessState()
    data class Text(
        val onText: ((String) -> Unit)? = null
    ) : ProcessState()
}

data class TagData(
    val tagName: String,
    val attributeName: String,
    val attributeValue: String
)

data class UpdatePriceInput(
    val dealId: DealId,
    val storeId: StoreId,
    val pageIndex: Int
)

data class UpdateDealInput(
    val dealId: DealId,
    val storeId: StoreId,
)

@JvmInline
value class StoreId(
    val storeId: String
)

@JvmInline
value class DealId(
    val dealId: String
)