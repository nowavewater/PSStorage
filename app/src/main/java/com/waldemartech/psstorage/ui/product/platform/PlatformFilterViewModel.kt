package com.waldemartech.psstorage.ui.product.platform

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.waldemartech.psstorage.data.store.StoreId
import com.waldemartech.psstorage.domain.product.LoadPlatformDuplicatedUseCase
import com.waldemartech.psstorage.domain.store.LoadDealUseCase
import com.waldemartech.psstorage.ui.widget.entity.DealWidgetData
import com.waldemartech.psstorage.ui.widget.entity.ProductItemData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject


@HiltViewModel
class PlatformFilterViewModel @Inject constructor(
    private val loadPlatformDuplicatedUseCase: LoadPlatformDuplicatedUseCase
) : ViewModel() {
    private val _productList = mutableStateListOf<ProductItemData>()
    fun productList(): SnapshotStateList<ProductItemData> = _productList

    suspend fun loadPlatformDuplicated(storeId: StoreId) {
        viewModelScope.launch(Dispatchers.IO) {
            _productList.clear()
            Timber.i("on load platform duplicated")
            _productList.addAll(
                loadPlatformDuplicatedUseCase(storeId)
            )
        }
    }
}