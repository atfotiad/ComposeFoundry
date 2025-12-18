package com.atfotiad.composefoundry.sample.feature.login.ui

import com.atfotiad.composefoundry.designsystem.foundation.architecture.UiEffect
import com.atfotiad.composefoundry.designsystem.foundation.architecture.UiState
import com.atfotiad.composefoundry.designsystem.foundation.resources.UiText

data class LoginState(
    val email: String = "",
    val emailError: UiText? = null,
    val password: String = "",
    val passwordError: UiText? = null,
    val isLoggingIn: Boolean = false
) : UiState

sealed interface LoginEffect : UiEffect {
    data object NavigateToHome : LoginEffect
    data class ShowError(val message: String) : LoginEffect
}
