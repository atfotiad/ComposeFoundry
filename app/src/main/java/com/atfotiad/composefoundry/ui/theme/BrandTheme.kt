package com.atfotiad.composefoundry.ui.theme

import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color

// Define some custom brand colors
val TealBrand = Color(0xFF009688)
val OrangeAction = Color(0xFFFF5722)

// Create a Light Color Scheme
val BrandLightScheme = lightColorScheme(
    primary = TealBrand,
    onPrimary = Color.White,
    secondary = OrangeAction,
    onSecondary = Color.White,
    tertiary = Color.Black
    // Add others as needed...
)
