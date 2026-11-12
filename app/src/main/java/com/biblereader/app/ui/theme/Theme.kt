package com.biblereader.app.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val BibleDark = darkColorScheme(
    primary = Gold,
    onPrimary = Ink,
    secondary = Gold,
    onSecondary = Ink,
    background = Ink,
    onBackground = Cream,
    surface = Surface,
    onSurface = Cream,
    surfaceVariant = SurfaceElevated,
    onSurfaceVariant = Muted,
    outline = Divider
)

@Composable
fun BibleReaderTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = BibleDark,
        typography = MaterialTheme.typography,
        content = content
    )
}
