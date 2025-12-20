package com.atfotiad.composefoundry.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.atfotiad.composefoundry.core.data.local.datastore.StorageManager
import com.atfotiad.composefoundry.navigation.Destinations
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

// 1. Define the possible states of the App Shell
sealed interface MainUiState {
    data object Loading : MainUiState
    data class Success(val isDarkTheme: Boolean, val startDestination: String) : MainUiState
}

@HiltViewModel
class MainViewModel @Inject constructor(
    storageManager: StorageManager
) : ViewModel() {

    // 2. Transform the Settings Flow into a UI State
    val uiState: StateFlow<MainUiState> = storageManager.appSettings
        .map { settings ->
            // Resolve Theme
            val isDark = settings.themeMode == "dark"
            // Resolve Start Destination
            val startRoute = if (settings.authToken != null) {
                 Destinations.CounterScreen.route
            } else {
                "counter_screen" // Placeholder for Login
            }

            MainUiState.Success(isDarkTheme = isDark, startDestination = startRoute)
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = MainUiState.Loading
        )
}
