package com.waldemartech.psstorage.ui.deal.detail

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.waldemartech.psstorage.domain.store.LoadProductByPageUseCase
import com.waldemartech.psstorage.domain.store.LoadProductByPageUseCase.Companion.PAGE_ITEM
import com.waldemartech.psstorage.ui.widget.entity.ProductItemData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DealDetailViewModel @Inject constructor(
    private val loadProductByPageUseCase: LoadProductByPageUseCase
) : ViewModel() {

    private var _currentPage = mutableStateOf(0)
    fun currentPage() : State<Int> = _currentPage

    private val _productList = mutableStateListOf<ProductItemData>()
    fun productList(): SnapshotStateList<ProductItemData> = _productList



    fun loadProductList(storeId: String, dealId: String, pageIndex: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val list = loadProductByPageUseCase(storeId = storeId, dealId = dealId, offset = pageIndex + PAGE_ITEM)
            _productList.clear()
            _productList.addAll(list)
        }
    }

    fun loadNextPage(storeId: String, dealId: String) {
        _currentPage.value++
        loadProductList(storeId = storeId, dealId = dealId, pageIndex = _currentPage.value)
    }

    fun  loadPreviousPage(storeId: String, dealId: String) {
        _currentPage.value--
        loadProductList(storeId = storeId, dealId = dealId, pageIndex = _currentPage.value)
    }

}