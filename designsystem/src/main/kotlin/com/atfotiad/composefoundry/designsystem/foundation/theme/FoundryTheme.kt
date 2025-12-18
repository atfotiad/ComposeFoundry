package com.atfotiad.composefoundry.designsystem.foundation.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.platform.LocalContext

// Define standard colors (or import from Color.kt)
private val DarkColorScheme = darkColorScheme(
    primary = Purple80,
    secondary = PurpleGrey80,
    tertiary = Pink80
)

private val LightColorScheme = lightColorScheme(
    primary = Purple40,
    secondary = PurpleGrey40,
    tertiary = Pink40
)

/**
 * 🎨 FoundryTheme: The Single Source of Truth for App Design.
 * Wraps MaterialTheme and injects our custom systems (Spacing, etc).
 *
 * @param colorScheme Override this to provide your App's specific branding.
 * @param typography Override this to provide custom fonts.
 */
@Composable
fun FoundryTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    colorScheme: ColorScheme? = null,
    typography: Typography = Typography,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        colorScheme != null -> colorScheme
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    // 👇 This is the Key: We wrap MaterialTheme with OUR Providers
    CompositionLocalProvider(
        // Inject our Spacing System here so it's globally available
        LocalSpacing provides Spacing()
    ) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = typography, // Ensure Typography.kt exists in this package
            content = content
        )
    }
}
