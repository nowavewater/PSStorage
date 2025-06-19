package com.waldemartech.psstorage.ui.store.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.waldemartech.psstorage.domain.store.SyncDealUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class StoreDetailViewModel @Inject constructor(
    private val syncDealUseCase: SyncDealUseCase
) : ViewModel() {

    fun syncDeal(storeId: String) {
        viewModelScope.launch {
            syncDealUseCase(storeId)
        }
    }

}