package com.atfotiad.composefoundry.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.atfotiad.composefoundry.navigation.addGeneratedDestinations

@Composable
fun AppNavHost(
    navController: NavHostController,
    startRoute: String
) {
    // Inject the controller into our Local provider
    CompositionLocalProvider(LocalNavController provides navController) {
        NavHost(
            navController = navController,
            startDestination = startRoute
        ) {
            // The magic KSP generation
            addGeneratedDestinations()
        }
    }
}
