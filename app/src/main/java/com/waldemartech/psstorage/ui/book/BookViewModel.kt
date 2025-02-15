/*
 * Copyright (C) 2022 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.waldemartech.psstorage.ui.book

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import com.waldemartech.psstorage.data.BookRepository
import com.waldemartech.psstorage.ui.book.BookUiState.Error
import com.waldemartech.psstorage.ui.book.BookUiState.Loading
import com.waldemartech.psstorage.ui.book.BookUiState.Success
import javax.inject.Inject

@HiltViewModel
class BookViewModel @Inject constructor(
    private val bookRepository: BookRepository
) : ViewModel() {

    val uiState: StateFlow<BookUiState> = bookRepository
        .books.map<List<String>, BookUiState>(::Success)
        .catch { emit(Error(it)) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), Loading)

    fun addBook(name: String) {
        viewModelScope.launch {
            bookRepository.add(name)
        }
    }
}

sealed interface BookUiState {
    object Loading : BookUiState
    data class Error(val throwable: Throwable) : BookUiState
    data class Success(val data: List<String>) : BookUiState
}
