package com.atfotiad.composefoundry.ui

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.atfotiad.composefoundry.BuildConfig
import com.atfotiad.composefoundry.core.notifications.NotificationPermissionEffect
import com.atfotiad.composefoundry.designsystem.components.feedback.FoundryConnectivityBanner
import com.atfotiad.composefoundry.designsystem.components.feedback.FoundryOfflineBanner
import com.atfotiad.composefoundry.designsystem.components.navigation.FoundryAdaptiveScaffold
import com.atfotiad.composefoundry.designsystem.components.navigation.TopLevelNavConfig
import com.atfotiad.composefoundry.designsystem.foundation.theme.FoundryTheme
import com.atfotiad.composefoundry.designsystem.network.NetworkMonitor
import com.atfotiad.composefoundry.navigation.AppNavHost
import com.atfotiad.composefoundry.navigation.Destinations
import com.atfotiad.composefoundry.navigation.getIcon
import com.atfotiad.composefoundry.navigation.getLabel
import com.atfotiad.composefoundry.ui.debug.DebugOverlay
import com.atfotiad.composefoundry.ui.debug.DebugViewModel
import com.atfotiad.composefoundry.ui.theme.BrandLightScheme
import com.atfotiad.composefoundry.ui.theme.BrandTypography

@Composable
fun AppContent(
    uiState: MainUiState,
    networkMonitor: NetworkMonitor,
    debugViewModel: DebugViewModel = hiltViewModel()
) {
    val isOnline by networkMonitor.isOnline.collectAsStateWithLifecycle(true)
    val debugState by debugViewModel.uiState.collectAsStateWithLifecycle()
    var showDebug by remember { mutableStateOf(false) }


    FoundryTheme(
        colorScheme = BrandLightScheme,
        typography = BrandTypography,
        dynamicColor = false,
        darkTheme = (uiState as? MainUiState.Success)?.isDarkTheme ?: false
    ) {
        NotificationPermissionEffect()

        val navController = rememberNavController()
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route

        FoundryAdaptiveScaffold(
            navConfigs = Destinations.topLevelItems.map {
                TopLevelNavConfig(it.route, it.getIcon(), it.getLabel())
            },
            currentRoute = currentRoute,
            onNavigate = { route ->
                navController.navigate(route) {
                    popUpTo(navController.graph.findStartDestination().id) {
                        saveState = true
                    }
                    launchSingleTop = true
                    restoreState = true
                }
            }
        ) {
            Scaffold(
                topBar = {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(IntrinsicSize.Min)
                            .pointerInput(Unit) {
                                detectTapGestures(
                                    onLongPress = {
                                        if (BuildConfig.DEBUG) showDebug = true
                                    }
                                )
                            }
                            .windowInsetsPadding(WindowInsets.statusBars)
                    ) {
                        FoundryConnectivityBanner(
                            isVisible = !isOnline,
                            backgroundColor = BrandLightScheme.errorContainer
                        ) {
                            // Use the Default Foundry Banner
                            FoundryOfflineBanner()
                            // Or OfflineBanner() if you want your own banner from app module
                        }
                    }
                }
            ) { paddingValues ->
                if (showDebug) {
                    DebugOverlay(
                        state = debugState,
                        onEnvChange = {
                            debugViewModel.updateEnvironment(it)
                            showDebug = false
                        },
                        onWipe = {
                            debugViewModel.wipeEverything()
                            showDebug = false
                        },
                        onDismiss = { showDebug = false }
                    )
                }

                Box(Modifier.padding(paddingValues)) {
                    when (uiState) {
                        is MainUiState.Loading -> {
                            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                                CircularProgressIndicator()
                            }
                        }

                        is MainUiState.Success -> {
                            CompositionLocalProvider(LocalNavController provides navController) {
                                AppNavHost(
                                    navController = navController,
                                    startRoute = uiState.startDestination
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}