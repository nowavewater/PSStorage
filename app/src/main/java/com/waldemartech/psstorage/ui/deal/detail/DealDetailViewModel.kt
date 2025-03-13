package com.waldemartech.psstorage.ui.deal.detail

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.waldemartech.psstorage.domain.product.AddFavoriteUseCase
import com.waldemartech.psstorage.domain.product.AddIgnoredUseCase
import com.waldemartech.psstorage.domain.store.LoadProductByPageUseCase
import com.waldemartech.psstorage.ui.widget.entity.ProductPriceItemData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DealDetailViewModel @Inject constructor(
    private val loadProductByPageUseCase: LoadProductByPageUseCase,
    private val addFavoriteUseCase: AddFavoriteUseCase,
    private val addIgnoredUseCase: AddIgnoredUseCase
) : ViewModel() {

    private val dispatcher = Dispatchers.IO

    private var _currentPage = mutableIntStateOf(0)
    fun currentPage() : State<Int> = _currentPage

    private val _productList = mutableStateListOf<ProductPriceItemData>()
    fun productList(): SnapshotStateList<ProductPriceItemData> = _productList

    fun addToFavorite(storeId: String, item: ProductPriceItemData) {
        viewModelScope.launch(dispatcher) {
            addFavoriteUseCase(storeId = storeId, productId = item.productId)
        }
    }

    fun addToIgnored(storeId: String, item: ProductPriceItemData) {
        viewModelScope.launch(dispatcher) {
            addIgnoredUseCase(storeId = storeId, productId = item.productId)
        }
    }

    fun loadProductList(storeId: String, dealId: String, pageIndex: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val list = loadProductByPageUseCase(storeId = storeId, dealId = dealId, page = pageIndex)
            _productList.clear()
            _productList.addAll(list)
        }
    }

    fun loadNextPage(storeId: String, dealId: String) {
        _currentPage.intValue++
        loadProductList(storeId = storeId, dealId = dealId, pageIndex = _currentPage.intValue)
    }

    fun  loadPreviousPage(storeId: String, dealId: String) {
        _currentPage.intValue--
        loadProductList(storeId = storeId, dealId = dealId, pageIndex = _currentPage.intValue)
    }

}