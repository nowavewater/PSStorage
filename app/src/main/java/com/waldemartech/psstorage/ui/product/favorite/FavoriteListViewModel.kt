package com.waldemartech.psstorage.ui.product.favorite

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.waldemartech.psstorage.domain.product.AddIgnoredUseCase
import com.waldemartech.psstorage.domain.product.DeleteFavoriteUseCase
import com.waldemartech.psstorage.domain.product.LoadFavoriteByPageUseCase
import com.waldemartech.psstorage.domain.product.LoadTotalFavoriteCountUseCase
import com.waldemartech.psstorage.ui.widget.entity.ProductItemData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoriteListViewModel @Inject constructor(
    private val addIgnoredUseCase: AddIgnoredUseCase,
    private val deleteFavoriteUseCase: DeleteFavoriteUseCase,
    private val loadFavoriteByPageUseCase: LoadFavoriteByPageUseCase,
    private val loadFavoriteCountUseCase: LoadTotalFavoriteCountUseCase
) : ViewModel() {

    private val dispatcher = Dispatchers.IO

    private var _currentPage = mutableIntStateOf(0)
    fun currentPage() : State<Int> = _currentPage

    private var _totalItemCount = mutableIntStateOf(0)
    fun totalItemCount(): State<Int> = _totalItemCount

    private val _productList = mutableStateListOf<ProductItemData>()
    fun productList(): SnapshotStateList<ProductItemData> = _productList

    suspend fun loadFavoriteCount(storeId: String) {
        _totalItemCount.intValue = loadFavoriteCountUseCase(storeId = storeId)
    }


    fun loadFavoriteList(storeId: String, pageIndex: Int) {
        viewModelScope.launch(dispatcher) {
            val list = loadFavoriteByPageUseCase(storeId = storeId, page = pageIndex)
            _productList.clear()
            _productList.addAll(list)
        }
    }

    fun loadNextPage(storeId: String) {
        _currentPage.intValue++
        loadFavoriteList(storeId = storeId, pageIndex = _currentPage.intValue)
    }

    fun  loadPreviousPage(storeId: String) {
        _currentPage.intValue--
        loadFavoriteList(storeId = storeId, pageIndex = _currentPage.intValue)
    }

    fun addToIgnored(storeId: String, item: ProductItemData) {
        viewModelScope.launch(dispatcher) {
            deleteFavoriteUseCase(productId = item.productId)
            addIgnoredUseCase(productId = item.productId, storeId = storeId)
        }
    }

}