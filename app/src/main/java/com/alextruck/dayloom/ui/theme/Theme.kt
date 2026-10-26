package com.alextruck.dayloom.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val LightColorScheme = lightColorScheme(
    primary = DayloomTerracotta,
    secondary = DayloomSage,
    background = DayloomCream,
    surface = DayloomLinen,
    onPrimary = Color.White,
    onSecondary = Color.White,
    onBackground = DayloomCharcoal,
    onSurface = DayloomCharcoal
)

private val DarkColorScheme = darkColorScheme(
    primary = DayloomDarkTerracotta,
    secondary = DayloomDarkSage,
    background = DayloomDarkBackground,
    surface = DayloomDarkSurface,
    onPrimary = Color.White,
    onSecondary = Color.White,
    onBackground = DayloomDarkCream,
    onSurface = DayloomDarkCream
)

@Composable
fun DayloomTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.background.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
