package com.waldemartech.psstorage.ui.store.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.waldemartech.psstorage.data.store.StoreData
import com.waldemartech.psstorage.domain.launch.LaunchUseCase
import com.waldemartech.psstorage.domain.store.SyncDealUseCase
import com.waldemartech.psstorage.domain.store.UpdateDealUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class StoreListViewModel @Inject constructor(
    private val syncDealUseCase: SyncDealUseCase
) : ViewModel() {

    fun syncDeals() {
        viewModelScope.launch {
            syncDealUseCase()
        }
    }

}