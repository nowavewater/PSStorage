package com.waldemartech.psstorage.data.store

import android.content.Context
import androidx.work.Data
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkRequest
import com.waldemartech.psstorage.data.api.ApiConstants.BASE_URL
import com.waldemartech.psstorage.data.api.ApiConstants.HTML_ATTRIBUTE_NAME_ID
import com.waldemartech.psstorage.data.api.ApiConstants.HTML_TAG_SCRIPT
import com.waldemartech.psstorage.data.api.ApiConstants.PRODUCT_COMPONENT
import com.waldemartech.psstorage.data.api.ApiConstants.PS_STORE_DEAL_ID
import com.waldemartech.psstorage.data.api.ApiDataSource
import com.waldemartech.psstorage.data.api.entity.ProductResponse
import com.waldemartech.psstorage.data.base.SharedConstants.COLON_SIGN
import com.waldemartech.psstorage.data.base.SharedConstants.EMPTY_STRING
import com.waldemartech.psstorage.data.base.SharedConstants.SLASH_SIGN
import com.waldemartech.psstorage.data.base.random
import com.waldemartech.psstorage.data.local.database.dao.DealDao
import com.waldemartech.psstorage.data.local.database.dao.PlatformDao
import com.waldemartech.psstorage.data.local.database.dao.PriceDao
import com.waldemartech.psstorage.data.local.database.dao.ProductDao
import com.waldemartech.psstorage.data.local.database.table.AllTimeLowPrice
import com.waldemartech.psstorage.data.local.database.table.Platform
import com.waldemartech.psstorage.data.local.database.table.PriceHistory
import com.waldemartech.psstorage.data.local.database.table.Product
import com.waldemartech.psstorage.data.local.database.table.ProductPlatformCrossRef
import com.waldemartech.psstorage.data.store.DealConstants.processDealResponse
import com.waldemartech.psstorage.data.store.DealConstants.processPriceText
import com.waldemartech.psstorage.data.store.StoreConstants.DEAL_ID_KEY
import com.waldemartech.psstorage.data.store.StoreConstants.PAGE_INDEX_KEY
import com.waldemartech.psstorage.data.store.StoreConstants.STORE_ID_KEY
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import timber.log.Timber
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class UpdatePriceRepository @Inject constructor(
    @ApplicationContext private val context: Context,
    private val dealDao: DealDao,
    private val productDao: ProductDao,
    private val platformDao: PlatformDao,
    private val priceDao: PriceDao,
    private val apiDataSource: ApiDataSource
)  {
    private val scope = CoroutineScope(Dispatchers.IO)

    private var shouldContinue = false

    suspend fun updatePrice(input: UpdatePriceInput) {
        val url = "$BASE_URL$SLASH_SIGN${input.storeId.storeId}/category/${input.dealId.dealId}$SLASH_SIGN${input.pageIndex}"
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
                        Timber.i("on price text $text")
                        val job = scope.launch(Dispatchers.IO) {
                            processPriceText(
                                text = text,
                                watchingKey = PRODUCT_COMPONENT,
                                onProductResponse = { productResponse ->
                                    shouldContinue = true
                                    processProduct(productResponse, input)
                                }
                            )
                        }
                        runBlocking {
                            job.join()
                        }
                    }
                )
            )

            val responseText = responseBody.string()

            processDealResponse(
                control = control,
                response = responseText,
            )

            Timber.i("on should continue $shouldContinue")

            val pageIndex = if (shouldContinue) input.pageIndex + 1 else 1

            dealDao.updateDealPageIndex(input.dealId.dealId, pageIndex)

            if (shouldContinue) {
                Timber.i("continue to ${input.pageIndex}")
                val inputData = Data.Builder()
                    .putString(STORE_ID_KEY, input.storeId.storeId)
                    .putInt(PAGE_INDEX_KEY, pageIndex)
                    .putString(DEAL_ID_KEY, input.dealId.dealId)
                    .build()
                val updateDealWorkRequest: WorkRequest =
                    OneTimeWorkRequestBuilder<UpdatePriceWorker>()
                        .setInitialDelay(20, TimeUnit.MILLISECONDS)
                        .setInputData(inputData)
                        .build()
                random {
                    WorkManager.getInstance(context).enqueue(updateDealWorkRequest)
                }
            }

        } catch (throwable: Throwable) {
            Timber.e("on network exception $throwable")
        }
    }

    private suspend fun processProduct(productResponse: ProductResponse, input: UpdatePriceInput) {
        val productId = productResponse.id + COLON_SIGN + input.storeId.storeId

        if (!productDao.hasProduct(productId)) {
            var imageUrl : String = EMPTY_STRING
            productResponse.mediaList.forEach forEachMedia@{ media ->
                if (media.role == "MASTER") {
                    imageUrl = media.url
                    return@forEachMedia
                }
            }

            productResponse.platforms.forEach { platform ->
                if (!platformDao.hasPlatForm(platform)) {
                    platformDao.insertPlatForm(Platform(name = platform))
                }
            }


            val product = Product(
                productId = productId,
                name = productResponse.name,
                typeName = productResponse.typeName,
                npTitleId = productResponse.titleId,
                imageUrl = imageUrl,
                localizedDisplayClassification = productResponse.localizedClassification,
                storeDisplayClassification = productResponse.storeDisplayClassification,
                storeIdInProduct = input.storeId.storeId
            )
            productDao.insertProduct(product)

            productResponse.platforms.forEach { platformName ->
                if (!productDao.hasProductInPlatform(productId = productId, platformName = platformName)) {
                    val platformId = platformDao.getPlatformIdByName(platformName)
                    productDao.insertProductPlatformCrossRef(
                        ProductPlatformCrossRef(
                            productId = productId,
                            platformId = platformId
                        )
                    )
                }
            }
        }

        val priceResponse = productResponse.price
        val basePriceList = priceResponse.basePrice.split("$")
        val discountedPriceList = priceResponse.discountedPrice.split("$")
        val basePrice = basePriceList[1].replace(",", "").toFloat()
        val discountedPrice = discountedPriceList[1].replace(",", "").toFloat()

        if (!priceDao.hasPrice(productId = productId, dealId = input.dealId.dealId, storeId = input.storeId.storeId)) {

            if (basePriceList.size >= 2 && discountedPriceList.size >= 2) {
                val unit = basePriceList[0]
                val discountText = priceResponse.discountText ?: EMPTY_STRING
                val isTiedToSubscription = priceResponse.isTiedToSubscription == true
                val price = PriceHistory(
                    productIdPriceRef = productId,
                    dealIdPriceRef = input.dealId.dealId,
                    unit = unit,
                    basePrice = basePrice,
                    discountedPrice = discountedPrice,
                    discountText = discountText,
                    isFree = priceResponse.isFree,
                    isExclusive = priceResponse.isExclusive,
                    isTiedToSubscription = isTiedToSubscription,
                    storeIdInPriceHistory = input.storeId.storeId
                )
                priceDao.insertPriceHistory(price)
            }
        }

        if (priceDao.hasAllTimeLowPrice(productId = productId, storeId = input.storeId.storeId).not()) {
            priceDao.insertAllTimeLowPrice(
                AllTimeLowPrice(
                    productIdInLowPrice = productId,
                    lowPrice = discountedPrice,
                    storeIdInLowPrice = input.storeId.storeId,
                )
            )
        } else {
            priceDao.getAllTimeLowPrice(
                productId = productId,
                storeId = input.storeId.storeId
            )?.let { allTimeLowPrice ->
                if (allTimeLowPrice.lowPrice < discountedPrice) {
                    priceDao.insertAllTimeLowPrice(
                        AllTimeLowPrice(
                            productIdInLowPrice = productId,
                            lowPrice = discountedPrice,
                            storeIdInLowPrice = input.storeId.storeId,
                        )
                    )
                }
            }
        }

    }

    suspend fun loadTotalCountInDeal(
        storeId: String, dealId: String
    ): Result<Int> {
        val url = "$BASE_URL$SLASH_SIGN${storeId}/category/${dealId}${SLASH_SIGN}1"
        try {
            val headers = HashMap<String, String>()
            val params = HashMap<String, String>()
            val responseBody = apiDataSource.downloadFile(
                url = url,
                params = params,
                headers = headers
            )
            return Result.success(0)
        } catch (throwable: Throwable) {
            return Result.failure(throwable)
        }
    }

}

