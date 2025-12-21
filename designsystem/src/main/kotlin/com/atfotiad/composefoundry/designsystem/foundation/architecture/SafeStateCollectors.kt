package com.atfotiad.composefoundry.designsystem.foundation.architecture

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow

/**
 * collectAsSafeState: The standardized way to collect [StateFlow] in our application.
 *
 * This function enforces collecting the state ONLY when the Composable is visible (Lifecycle.State.STARTED),
 * preventing unnecessary work when the screen is in the backstack.
 *Automatically uses the current value of the StateFlow as the initial value.
 * @param minActiveState The minimum active state for collection. Defaults to [Lifecycle.State.STARTED].
 * @return A Compose [State] that can be used via delegation (e.g., `val state by viewModel.state.collectAsSafeState(...)`).
 */
@Composable
fun <T> StateFlow<StandardUiState<T>>.collectAsSafeState(
    minActiveState: Lifecycle.State = Lifecycle.State.STARTED
): State<StandardUiState<T>> {
    return this.collectAsStateWithLifecycle(
        initialValue = this.value,
        minActiveState = minActiveState
    )
}

/**
 * collectAsEffect: The standardized way to collect [SharedFlow] effects.
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

/**
 * collectAsSimpleState: For primitive flows or custom UiStates (like MainUiState).
 */
@Composable
fun <T> StateFlow<T>.collectAsSimpleState(
    minActiveState: Lifecycle.State = Lifecycle.State.STARTED
): State<T> {
    return this.collectAsStateWithLifecycle(
        initialValue = this.value,
        minActiveState = minActiveState
    )
}