package com.waldemartech.psstorage.ui.launcher

import android.content.Intent
import androidx.activity.ComponentActivity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.waldemartech.psstorage.ui.MainActivity
import kotlinx.coroutines.delay

@Composable
fun LauncherScreen(
    launcherViewModel: LauncherViewModel = hiltViewModel()
) {
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        launcherViewModel.onLaunch()
    }

    LaunchedEffect(Unit) {
        context.startActivity(Intent(context, MainActivity::class.java))
        (context as ComponentActivity).finish()
    }

}