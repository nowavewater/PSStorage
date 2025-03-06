package com.waldemartech.psstorage.ui.widget.button

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun JellyButton (
    text: String,
    modifier: Modifier = Modifier,
    onClick : () -> Unit,
) {
    TextButton(
        modifier = Modifier
            .height(50.dp)
            .width(200.dp)
            .background(Color.Cyan)
            .then(modifier),
        onClick = onClick
    ) {
        Text(text = text)
    }
}

@Composable
fun SmallOrionButton (
    text: String,
    modifier: Modifier = Modifier,
    onClick : () -> Unit,
) {
    TextButton(
        modifier = Modifier
            .height(30.dp)
            .width(100.dp)
            .background(Color.Cyan)
            .then(modifier),
        onClick = onClick
    ) {
        Text(text = text)
    }
}

@Composable
fun HeightSpacer() {
    Spacer(modifier = Modifier.height(20.dp))
}