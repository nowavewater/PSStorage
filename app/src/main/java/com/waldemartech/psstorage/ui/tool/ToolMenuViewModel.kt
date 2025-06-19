package com.waldemartech.psstorage.ui.tool

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.waldemartech.psstorage.data.store.StoreConstants.HK_STORE_ID
import com.waldemartech.psstorage.data.store.StoreConstants.US_STORE_ID
import com.waldemartech.psstorage.domain.tool.ExportUseCase
import com.waldemartech.psstorage.domain.tool.ImportUseCase
import com.waldemartech.psstorage.domain.tool.RemoveDuplicatedUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ToolMenuViewModel @Inject constructor(
    private val removeDuplicatedUseCase: RemoveDuplicatedUseCase,
    private val exportUseCase: ExportUseCase,
    private val importUseCase: ImportUseCase
) : ViewModel() {

    fun removeDuplicated() {
        viewModelScope.launch(Dispatchers.IO) {
            removeDuplicatedUseCase(HK_STORE_ID)
            removeDuplicatedUseCase(US_STORE_ID)
        }
    }

    fun exportData(uri: Uri) {
        viewModelScope.launch(Dispatchers.IO) {
            exportUseCase(uri = uri)
        }
    }

    fun importData(uri: Uri) {
        viewModelScope.launch(Dispatchers.IO) {
            importUseCase(uri = uri)
        }
    }

}