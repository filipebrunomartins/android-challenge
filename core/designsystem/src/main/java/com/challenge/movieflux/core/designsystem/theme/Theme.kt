package com.challenge.movieflux.core.designsystem.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

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
 * A composition local for [ColorScheme].
 */
val LocalMovieFluxColors = staticCompositionLocalOf { LightColorScheme }

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

    val gradientColors = if (darkTheme) {
        GradientColors(container = colorScheme.background)
    } else {
        GradientColors(
            top = colorScheme.primaryContainer,
            bottom = colorScheme.surface,
            container = colorScheme.surface,
        )
    }

    val backgroundTheme = BackgroundTheme(
        color = colorScheme.background,
        tonalElevation = 2.dp,
    )

    val tintTheme = TintTheme(
        iconTint = colorScheme.primary
    )

    CompositionLocalProvider(
        LocalMovieFluxColors provides colorScheme,
        LocalMovieFluxTypography provides MovieFluxTypography,
        LocalGradientColors provides gradientColors,
        LocalBackgroundTheme provides backgroundTheme,
        LocalTintTheme provides tintTheme,
    ) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = MovieFluxTypography,
            content = content,
        )
    }
}

/**
 * MovieFlux theme object to access design system tokens.
 */
object MovieFluxTheme {
    val colorScheme: ColorScheme
        @Composable
        get() = LocalMovieFluxColors.current

    val typography: Typography
        @Composable
        get() = LocalMovieFluxTypography.current

    val gradientColors: GradientColors
        @Composable
        get() = LocalGradientColors.current

    val backgroundTheme: BackgroundTheme
        @Composable
        get() = LocalBackgroundTheme.current

    val tintTheme: TintTheme
        @Composable
        get() = LocalTintTheme.current
}
