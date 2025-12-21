package com.atfotiad.composefoundry.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost

@Composable
fun AppNavHost(
    navController: NavHostController,
    startRoute: String
) {
    NavHost(
        navController = navController,
        startDestination = startRoute
    ) {
        // The magic KSP generation
        addGeneratedDestinations()
    }
}
