package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val RetroDarkColorScheme = darkColorScheme(
    primary = NeonPink,
    secondary = NeonCyan,
    tertiary = RetroYellow,
    background = CyberDarkBg,
    surface = CyberSurface,
    surfaceVariant = CyberSurfaceVariant,
    onPrimary = Color.White,
    onSecondary = Color.Black,
    onTertiary = Color.Black,
    onBackground = SoftWhite,
    onSurface = SoftWhite,
    onSurfaceVariant = SoftWhite,
    outline = NeonCyan
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = true, // We strictly use the dark theme for the retro-futurism aesthetic
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = RetroDarkColorScheme,
        typography = Typography,
        content = content
    )
}
