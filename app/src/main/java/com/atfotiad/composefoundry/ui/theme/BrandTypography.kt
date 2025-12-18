package com.atfotiad.composefoundry.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

// Example: Define a custom typography that uses Monospace for headlines
// In a real app, you would load a custom Google Font here.
val BrandTypography = Typography(
    headlineLarge = TextStyle(
        fontFamily = FontFamily.Monospace, // Visually obvious change
        fontWeight = FontWeight.Bold,
        fontSize = 32.sp,
        lineHeight = 40.sp,
        letterSpacing = 0.sp
    ),
    headlineMedium = TextStyle(
        fontFamily = FontFamily.Monospace,
        fontWeight = FontWeight.Bold,
        fontSize = 28.sp
    ),
    // Override the button text style
    labelLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.ExtraBold,
        fontSize = 14.sp
    )

    // You can override bodyLarge, titleMedium, etc. as needed
)
