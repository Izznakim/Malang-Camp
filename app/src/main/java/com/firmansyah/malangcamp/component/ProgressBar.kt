package com.firmansyah.malangcamp.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha

@Composable
fun LinearProgressBar(alpha: Float) {
    LinearProgressIndicator(
        modifier = Modifier
            .wrapContentHeight()
            .fillMaxWidth()
            .alpha(alpha)
    )
}