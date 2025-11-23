package com.waldemartech.psstorage.domain.tool

import android.content.Context
import android.net.Uri
import com.waldemartech.psstorage.data.local.database.dao.ProductDao
import com.waldemartech.psstorage.data.local.database.table.FavoriteProduct
import com.waldemartech.psstorage.data.local.database.table.IgnoredProduct
import com.waldemartech.psstorage.data.tool.ExportData
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.serialization.json.Json
import timber.log.Timber
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import javax.inject.Inject

class ImportUseCase @Inject constructor(
    @ApplicationContext private val context: Context,
    private val productDao: ProductDao
) {
    suspend operator fun invoke(uri: Uri) {
        try {
            context.contentResolver.openInputStream(uri)?.use { inputStream ->
                val stringBuilder = StringBuilder()
                BufferedReader(InputStreamReader(inputStream)).use { reader ->
                    var line: String?
                    while (reader.readLine().also { line = it } != null) {
                        stringBuilder.append(line).append("\n")
                    }
                }
                val jsonString = stringBuilder.toString()
                val importData = Json.decodeFromString<ExportData>(jsonString)
                importData.ignores.forEach { ignoredProduct ->
                    productDao.insertIgnoredProduct(
                        IgnoredProduct(
                            ignoredProductId = ignoredProduct.productID,
                            storeIdInIgnoredProduct = ignoredProduct.storeId
                        )
                    )
                }
                importData.favorites.forEach { favoriteProduct ->
                    productDao.insertFavoriteProduct(
                        FavoriteProduct(
                            favoriteProductId = favoriteProduct.productID,
                            storeIdInFavoriteProduct = favoriteProduct.storeId
                        )
                    )
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
            Timber.i("import exception $e")
            //    Toast.makeText(context, "文件保存失败: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }
}