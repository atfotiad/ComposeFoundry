package com.atfotiad.composefoundry.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.getValue
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.atfotiad.composefoundry.annotations.ComposeFoundryNavigation
import com.atfotiad.composefoundry.annotations.NavigationStrategy
import com.atfotiad.composefoundry.designsystem.network.NetworkMonitor
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
@ComposeFoundryNavigation(strategy = NavigationStrategy.JetpackCompose)
class MainActivity : ComponentActivity() {
    val viewModel: MainViewModel by viewModels()

    @Inject
    lateinit var networkMonitor: NetworkMonitor

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        splashScreen.setKeepOnScreenCondition {
            viewModel.uiState.value is MainUiState.Loading
        }

        setContent {
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()
            AppContent(uiState, networkMonitor)
        }
    }
}