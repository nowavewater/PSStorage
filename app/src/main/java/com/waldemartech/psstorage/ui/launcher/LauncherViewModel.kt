package com.waldemartech.psstorage.ui.launcher

import androidx.lifecycle.ViewModel
import com.waldemartech.psstorage.domain.launch.LaunchUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LauncherViewModel @Inject constructor(
    private val launchUseCase: LaunchUseCase,
) : ViewModel() {

    fun onLaunch() {
        launchUseCase()
    }


}