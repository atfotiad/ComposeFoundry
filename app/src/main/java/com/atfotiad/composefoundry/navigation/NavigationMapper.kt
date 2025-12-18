package com.atfotiad.composefoundry.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Countertops
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Star
import androidx.compose.ui.graphics.vector.ImageVector

/**
 * 🎨 Maps generated Destinations to UI assets.
 * This keeps the Library (DesignSystem) and KSP clean of Resource IDs.
 */
fun Destinations.getIcon(): ImageVector {
    return when (this) {
        is Destinations.SampleScreen -> Icons.Default.Home
        is Destinations.CounterScreen -> Icons.Default.Countertops
        // Add more as you create screens
        else -> Icons.Default.Star
    }
}

fun Destinations.getLabel(): String {
    return when (this) {
        is Destinations.SampleScreen -> "Sample"
        is Destinations.CounterScreen -> "Counter"
        // Add more as you create screens
        else -> ""
    }
}
