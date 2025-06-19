package com.waldemartech.psstorage.ui.deal.list

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.waldemartech.psstorage.data.store.StoreId
import com.waldemartech.psstorage.domain.store.LoadDealUseCase
import com.waldemartech.psstorage.ui.widget.entity.DealWidgetData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject


@HiltViewModel
class DealListViewModel @Inject constructor(
    private val loadDealUseCase: LoadDealUseCase
) : ViewModel() {

    private val _dealList = mutableStateListOf<DealWidgetData>()
    fun dealList(): SnapshotStateList<DealWidgetData> = _dealList

    suspend fun loadDeals(storeId: StoreId) {
        viewModelScope.launch(Dispatchers.IO) {
            _dealList.clear()
            Timber.i("on load deal")
            _dealList.addAll(
                loadDealUseCase(storeId)
            )
        }
    }

}