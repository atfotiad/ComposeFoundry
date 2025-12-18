package com.atfotiad.composefoundry.designsystem.foundation.architecture

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow

/**
 * 🛡️ collectAsSafeState: The standardized way to collect [StateFlow] in our application.
 *
 * This function enforces collecting the state ONLY when the Composable is visible (Lifecycle.State.STARTED),
 * preventing unnecessary work when the screen is in the backstack.
 *
 * @param initialValue The initial state value before collection begins.
 * @return A Compose [State] that can be used via delegation (e.g., `val state by viewModel.state.collectAsSafeState(...)`).
 */
@Composable
fun <T> StateFlow<T>.collectAsSafeState(initialValue: T): State<T> {
    val lifecycleOwner = LocalLifecycleOwner.current
    val stateFlow = remember { mutableStateOf(initialValue) }

    LaunchedEffect(key1 = this, key2 = lifecycleOwner) {
        lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
            this@collectAsSafeState.collect { value ->
                stateFlow.value = value
            }
        }
    }
    return stateFlow
}

/**
 * 💥 collectAsEffect: The standardized way to collect [SharedFlow] effects.
 *
 * This function collects one-time [UiEffect] events safely, ensuring the event listener is bound
 * to the Composable's lifecycle and the collection only happens when the view is active.
 *
 * @param action The lambda function executed for every [UiEffect] received.
 */
@SuppressLint("ComposableNaming")
@Composable
fun <T> Flow<T>.collectAsEffect(
    action: suspend (T) -> Unit
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    val flow = this

    LaunchedEffect(flow, lifecycleOwner) {
        // Collect effects only when the component is STARTED (visible)
        lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
            flow.collect { effect ->
                action(effect)
            }
        }
    }
}