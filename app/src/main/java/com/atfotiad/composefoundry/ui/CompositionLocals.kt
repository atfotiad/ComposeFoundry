package com.atfotiad.composefoundry.ui

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.navigation.NavController

/**
 * 🌍 Global Access to Navigation
 * This allows any screen to get the controller without passing it down as a parameter.
 */
val LocalNavController = staticCompositionLocalOf<NavController> {
    error("No NavController provided! Wrap your app in CompositionLocalProvider.")
}


