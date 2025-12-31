package com.atfotiad.composefoundry.ui.debug

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.atfotiad.composefoundry.designsystem.foundation.storage.StorageManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DebugViewModel @Inject constructor(
    private val storageManager: StorageManager
) : ViewModel() {

    // Observe current env and token for the UI
    val uiState = combine(
        storageManager.apiEnvironment,
        storageManager.authToken
    ) { env, token ->
        DebugState(env, token)
    }.stateIn(viewModelScope,
        SharingStarted.WhileSubscribed(5000), DebugState())

    fun updateEnvironment(env: String) {
        viewModelScope.launch { storageManager.updateApiEnvironment(env) }
    }

    fun wipeEverything() {
        viewModelScope.launch { storageManager.resetAll() }
    }
}

@Immutable
data class DebugState(val env: String = "", val token: String? = null)