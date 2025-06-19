package com.waldemartech.psstorage.domain.tool

import android.content.Context
import android.net.Uri
import com.waldemartech.psstorage.data.local.database.dao.ProductDao
import com.waldemartech.psstorage.data.store.StoreConstants.HK_STORE_ID
import com.waldemartech.psstorage.data.store.StoreConstants.US_STORE_ID
import com.waldemartech.psstorage.data.tool.ExportData
import com.waldemartech.psstorage.data.tool.ExportProduct
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.serialization.json.Json
import java.io.IOException
import javax.inject.Inject

class ExportUseCase @Inject constructor(
    @ApplicationContext private val context: Context,
    private val productDao: ProductDao
) {
    suspend operator fun invoke(uri: Uri) {
        val favoriteList = (productDao.loadAllFavoriteProduct(HK_STORE_ID) +
                productDao.loadAllFavoriteProduct(US_STORE_ID)).map { product ->
                    ExportProduct(
                        productID = product.product.productId,
                        storeId = product.product.storeIdInProduct
                    )
                }
        val ignoredList = (productDao.loadAllIgnoredProduct(HK_STORE_ID) +
                productDao.loadAllIgnoredProduct(US_STORE_ID)).map { product ->
                    ExportProduct(
                        productID = product.product.productId,
                        storeId = product.product.storeIdInProduct
                    )
                }
        val exportData = ExportData(
            favorites = favoriteList,
            ignores = ignoredList
        )

        try {
            context.contentResolver.openOutputStream(uri)?.use { outputStream ->
                val jsonString = Json.encodeToString(exportData)
                outputStream.write(jsonString.encodeToByteArray())
            //    Toast.makeText(context, "文件保存成功！", Toast.LENGTH_SHORT).show()
            }
        } catch (e: IOException) {
            e.printStackTrace()
        //    Toast.makeText(context, "文件保存失败: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }
}