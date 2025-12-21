package com.atfotiad.composefoundry.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Casino
import androidx.compose.material.icons.filled.Countertops
import androidx.compose.material.icons.filled.Star
import androidx.compose.ui.graphics.vector.ImageVector

/**
 * 🎨 Maps generated Destinations to UI assets.
 * This keeps the Library (DesignSystem) and KSP clean of Resource IDs.
 */
fun Destinations.getIcon(): ImageVector {
    return when (this) {
        is Destinations.CounterScreen -> Icons.Default.Countertops
        is Destinations.BlackjackScreen -> Icons.Default.Casino
        // Add more as you create screens
        else -> Icons.Default.Star
    }
}

fun Destinations.getLabel(): String {
    return when (this) {
        is Destinations.CounterScreen -> "Counter"
        is Destinations.BlackjackScreen -> "Blackjack"
        // Add more as you create screens
        else -> ""
    }
}
