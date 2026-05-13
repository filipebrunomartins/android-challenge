package com.challenge.movieflux.core.designsystem.theme

import androidx.annotation.VisibleForTesting
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

/**
 * Light theme color scheme
 */
val LightColorScheme = lightColorScheme(
    primary = TealGreen40,
    onPrimary = Color.White,
    primaryContainer = TealGreen90,
    onPrimaryContainer = TealGreen10,
    secondary = Slate40,
    onSecondary = Color.White,
    background = BackgroundLight,
    onBackground = Color.Black,
    surface = SurfaceLight,
    onSurface = Color.Black,
    error = Red40,
    onError = Color.White
)

/**
 * Dark theme color scheme
 */
val DarkColorScheme = darkColorScheme(
    primary = TealGreen80,
    onPrimary = Color.Black,
    primaryContainer = TealGreen10,
    onPrimaryContainer = TealGreen90,
    secondary = Slate80,
    onSecondary = Color.Black,
    background = BackgroundDark,
    onBackground = Color.White,
    surface = SurfaceDark,
    onSurface = Color.White,
    error = Red80,
    onError = Color.Black
)

/**
 * MovieFlux Android theme.
 *
 * @param darkTheme Whether the theme should use a dark color scheme (follows system by default).
 */
@Composable
fun MovieFluxTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {

    val colorScheme = if (darkTheme) {
        DarkColorScheme
    } else {
        LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = MovieFluxTypography,
        content = content,
    )
}