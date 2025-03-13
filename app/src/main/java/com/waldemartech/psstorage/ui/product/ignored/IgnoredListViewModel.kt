package com.waldemartech.psstorage.ui.product.ignored

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.waldemartech.psstorage.domain.product.LoadIgnoredByPageUseCase
import com.waldemartech.psstorage.ui.widget.entity.ProductItemData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class IgnoredListViewModel @Inject constructor(
    private val loadIgnoredByPageUseCase: LoadIgnoredByPageUseCase,
) : ViewModel() {

    private val dispatcher = Dispatchers.IO

    private var _currentPage = mutableIntStateOf(0)
    fun currentPage() : State<Int> = _currentPage

    private val _productList = mutableStateListOf<ProductItemData>()
    fun productList(): SnapshotStateList<ProductItemData> = _productList

    fun loadIgnoredList(storeId: String, pageIndex: Int) {
        viewModelScope.launch(dispatcher) {
            val list = loadIgnoredByPageUseCase(storeId = storeId, page = pageIndex)
            _productList.clear()
            _productList.addAll(list)
        }
    }

    fun loadNextPage(storeId: String) {
        _currentPage.intValue++
        loadIgnoredList(storeId = storeId, pageIndex = _currentPage.intValue)
    }

    fun  loadPreviousPage(storeId: String) {
        _currentPage.intValue--
        loadIgnoredList(storeId = storeId, pageIndex = _currentPage.intValue)
    }

}