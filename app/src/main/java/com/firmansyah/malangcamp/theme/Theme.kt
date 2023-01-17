package com.firmansyah.malangcamp.theme

import androidx.compose.material.MaterialTheme
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable

@Composable
fun MalangCampTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colors = LightColors,
        content = content
    )
}

private val LightColors = lightColors(
    primary = blue,
    primaryVariant = blueDark,
    onPrimary = white,
    secondary = teal200,
    secondaryVariant = teal700,
    onSecondary = black
)