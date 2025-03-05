package com.waldemartech.psstorage.data.store

import com.mohamedrejeb.ksoup.html.parser.KsoupHtmlHandler
import com.mohamedrejeb.ksoup.html.parser.KsoupHtmlParser
import com.waldemartech.psstorage.data.api.ApiConstants.BASE_URL
import com.waldemartech.psstorage.data.api.ApiConstants.HTML_ATTRIBUTE_NAME_ID
import com.waldemartech.psstorage.data.api.ApiConstants.HTML_TAG_SCRIPT
import com.waldemartech.psstorage.data.api.ApiConstants.PS_STORE_DEAL_ID
import com.waldemartech.psstorage.data.api.ApiDataSource
import com.waldemartech.psstorage.data.api.entity.ProductResponse
import com.waldemartech.psstorage.data.base.SharedConstants.EMPTY_STRING
import com.waldemartech.psstorage.data.base.SharedConstants.SLASH_SIGN
import com.waldemartech.psstorage.data.local.database.dao.PlatformDao
import com.waldemartech.psstorage.data.local.database.dao.PriceDao
import com.waldemartech.psstorage.data.local.database.dao.ProductDao
import com.waldemartech.psstorage.data.local.database.table.Platform
import com.waldemartech.psstorage.data.local.database.table.PriceHistory
import com.waldemartech.psstorage.data.local.database.table.Product
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import org.json.JSONObject
import timber.log.Timber
import javax.inject.Inject

class UpdatePriceRepository @Inject constructor(
    private val productDao: ProductDao,
    private val platformDao: PlatformDao,
    private val priceDao: PriceDao,
    private val apiDataSource: ApiDataSource
)  {

    private val scope = CoroutineScope(Dispatchers.IO)

    suspend fun updatePrice(input: UpdatePriceInput) {
        val url = "$BASE_URL${input.storeId}/category/${input.dealId}$SLASH_SIGN${input.pageIndex}"
        try {
            val headers = HashMap<String, String>()
            val params = HashMap<String, String>()
            val responseBody = apiDataSource.downloadFile(
                url = url,
                params = params,
                headers = headers
            )
            processResponse(responseBody.string(), input)
            /*processDealResponse(
                responseBody.string()
            )*/
            //    Timber.i(responseBody.string())
        } catch (throwable: Throwable) {
            //    Timber.e("on network exception $throwable")
        }
    }

    private suspend fun processResponse(response: String, input: UpdatePriceInput) {
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
                    Timber.i("on price $text")
                    watchingState = WatchingState.Finished
                    scope.launch {
                        processText(text, input)
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

    private suspend fun processText(text: String, input: UpdatePriceInput) {
        try {
            val json = JSONObject(text)
            if (json.has("props")) {
                val props = json.getJSONObject("props")
                if (props.has("apolloState")) {
                    val apolloState = props.getJSONObject("apolloState")
                    apolloState.keys().forEach { key ->
                        Timber.i("key is ${key}")
                        if (key.contains("Product")) {
                            Timber.i("for each product")
                            val productResponse = Json{ ignoreUnknownKeys = true }.decodeFromString<ProductResponse>(apolloState.getJSONObject(key).toString())
                            if (!productDao.hasProduct(productResponse.id)) {
                                var imageUrl : String = EMPTY_STRING
                                productResponse.mediaList.forEach forEachMedia@{ media ->
                                    if (media.role == "MASTER") {
                                        imageUrl = media.url
                                        return@forEachMedia
                                    }
                                }
                                val product = Product(
                                    productId = productResponse.id,
                                    name = productResponse.name,
                                    typeName = productResponse.typeName,
                                    npTitleId = productResponse.titleId,
                                    imageUrl = imageUrl,
                                    localizedDisplayClassification = productResponse.localizedClassification,
                                    storeDisplayClassification = productResponse.storeDisplayClassification,
                                    storeIdInProduct = input.storeId
                                )
                                productDao.insertProduct(product)
                            }

                            productResponse.platforms.forEach { platform ->
                                if (!platformDao.hasPlatForm(platform)) {
                                    platformDao.insertPlatForm(Platform(name = platform))
                                }
                            }

                            if (!priceDao.hasPrice(productId = productResponse.id, dealId = input.dealId)) {
                                val priceResponse = productResponse.price
                                val basePriceList = priceResponse.basePrice.split("\$")
                                val discountedPriceList = priceResponse.discountedPrice.split("\$")
                                if (basePriceList.size >= 2) {
                                    val unit = basePriceList[0]
                                    val basePrice = basePriceList[1].toFloat()
                                    val discountedPrice = discountedPriceList[1].toFloat()
                                    val price = PriceHistory(
                                        productIdPriceRef = productResponse.id,
                                        dealIdPriceRef = input.dealId,
                                        unit = unit,
                                        basePrice = basePrice,
                                        discountedPrice = discountedPrice,
                                        discountText = priceResponse.discountText,
                                        isFree = priceResponse.isFree,
                                        isExclusive = priceResponse.isExclusive,
                                        isTiedToSubscription = priceResponse.isTiedToSubscription,
                                        storeIdInPriceHistory = input.storeId
                                    )
                                    priceDao.insertPriceHistory(price)
                                }
                            }
                        }
                    }
                }
            }
        } catch (throwable: Throwable) {
            Timber.e("on parse exception ${throwable}")
        }

    }

}

data class UpdatePriceInput(
    val dealId: String,
    val storeId: String,
    val pageIndex: Int
)

data class PriceData(
    val unit: String,
    val price: Float
)