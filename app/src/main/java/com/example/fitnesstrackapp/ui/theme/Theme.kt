package com.example.fitnesstrackapp.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

/**
 * Premium dark fitness colour scheme.
 *
 * Dynamic colour is intentionally disabled so the hand-crafted palette is
 * always applied consistently, regardless of the device's Android version or
 * wallpaper colour.
 */
private val FitnessDarkColorScheme = darkColorScheme(
    // Core
    primary                = PrimaryBlue,
    onPrimary              = OnPrimary,
    primaryContainer       = SecondaryContainer,
    onPrimaryContainer     = OnSecondaryContainer,

    secondary              = AccentTeal,
    onSecondary            = OnPrimary,
    secondaryContainer     = SecondaryContainer,
    onSecondaryContainer   = OnSecondaryContainer,

    tertiary               = SuccessGreen,
    onTertiary             = OnPrimary,

    // Backgrounds / surfaces
    background             = DarkBackground,
    onBackground           = TextPrimary,
    surface                = DarkSurface,
    onSurface              = TextPrimary,
    surfaceVariant         = DarkSurfaceVar,
    onSurfaceVariant       = TextSecondary,

    // Error
    error                  = ErrorRed,
    onError                = OnPrimary,
    errorContainer         = Color(0xFF3B0A0A),
    onErrorContainer       = Color(0xFFFFCDD2),

    // Outlines
    outline                = BorderSubtle,
    outlineVariant         = Color(0xFF1E2235),
)

@Composable
fun FitnessTrackAppTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = FitnessDarkColorScheme,
        typography  = Typography,
        content     = content
    )
}