package com.atfotiad.composefoundry.designsystem.foundation.architecture

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.atfotiad.composefoundry.designsystem.foundation.resources.UiText
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * 🧱 UiState: The "One StateUI Class" marker interface.
 *
 * This contract enforces that every data class defining the state of a screen (e.g.,
 * LoginState, ProfileViewState) must implement this interface. This is the single,
 * immutable source of truth for the UI.
 */
interface UiState

/**
 * 💥 UiEffect: Marker interface for all one-time UI Side Effects.
 *
 * This contract is essential for MVVM cleanup, ensuring transient actions (navigation,
 * toasts, snackbars, dialogs) are handled safely via a SharedFlow, preventing
 * state-related re-triggering on recomposition.
 */
interface UiEffect

/**
 * 🧠 BaseViewModel: The MVVM Standardization Contract.
 *
 * All ViewModels in the application MUST extend this class. It enforces the exposure
 * of the two necessary streams for a Reactive MVVM/UDF (Unidirectional Data Flow) architecture.
 *
 * @param S The specific UiState implementation for the feature.
 * @param E The specific UiEffect implementation for the feature.
 */
abstract class BaseViewModel<S : UiState, E : UiEffect>(
    initialState: S
) : ViewModel() {

    private val _state = MutableStateFlow(initialState)

    /**
     * MANDATORY: The observable, immutable state stream.
     * The Compose View collects this StateFlow to render the screen.
     */
    val state = _state.asStateFlow()

    private val _effect = Channel<E>()


    /**
     * MANDATORY: The observable, non-sticky stream for one-time side effects.
     * The Compose View collects this SharedFlow (or equivalent Channel) to perform transient actions.
     */
    val effect: Flow<E> = _effect.receiveAsFlow()

    /**
     * 🔄 Helper to update the UI State safely and atomically.
     *
     * Usage:     * setState { copy(isLoading = true) }
     */
    protected fun setState(reducer: S.() -> S) {
        _state.update(reducer)
    }

    /**
     * 🚀 Helper to fire a Side Effect (Navigation, Toast, etc).
     *
     * Usage:
     * sendEffect(LoginEffect.NavigateHome)
     */
    protected fun sendEffect(effect: E) {
        viewModelScope.launch {
            _effect.send(effect)
        }
    }

    /**
     * Helper to access the current state value synchronously.
     */
    protected val currentState: S
        get() = state.value

}

/**
 * 🏗️ StandardUiState: A specific UiState implementation for screens
 * that follow the strict Loading -> Content -> Error pattern.
 *
 * This uses a Sealed Interface to enforce mutually exclusive states.
 */
sealed interface StandardUiState<out T> : UiState {

    /**
     * ⏳ Loading: The screen is initializing or fetching data.
     */
    data object Loading : StandardUiState<Nothing>

    /**
     * ✅ Success: The screen is showing content.
     * @param data The main data model for the screen.
     */
    data class Success<out T>(
        val data: T,
        val isRefreshing: Boolean = false
    ) : StandardUiState<T>

    /**
     * ❌ Error: The operation failed.
     */
    data class Error(
        val message: UiText,
        val cause: Throwable? = null
    ) : StandardUiState<Nothing>

    data object Empty : StandardUiState<Nothing>


    val dataOrNull: T?
        get() = (this as? Success<T>)?.data
}
