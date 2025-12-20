package com.atfotiad.composefoundry.features.login.ui

import androidx.lifecycle.viewModelScope
import com.atfotiad.composefoundry.designsystem.foundation.architecture.BaseViewModel
import com.atfotiad.composefoundry.designsystem.foundation.validation.EmailValidator
import com.atfotiad.composefoundry.designsystem.foundation.validation.MinLengthValidator
import com.atfotiad.composefoundry.designsystem.foundation.validation.ValidationResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor() : BaseViewModel<LoginState, LoginEffect>(
    initialState = LoginState()
) {
    private val emailValidator = EmailValidator()
    private val passwordValidator = MinLengthValidator(6)

    fun onEmailChanged(email: String) {
        val validation = emailValidator.validate(email)
        setState {
            copy(
                email = email,
                emailError = if (validation is ValidationResult.Invalid) validation.error else null
            )
        }
    }

    fun onPasswordChanged(password: String) {
        val validation = passwordValidator.validate(password)
        setState {
            copy(
                password = password,
                passwordError = if (validation is ValidationResult.Invalid) validation.error else null
            )
        }
    }

    fun login() {
        val emailValid = emailValidator.validate(currentState.email)
        val passwordValid = passwordValidator.validate(currentState.password)

        if (emailValid is ValidationResult.Valid && passwordValid is ValidationResult.Valid) {
            viewModelScope.launch {
                setState { copy(isLoggingIn = true) }
                delay(1500) // Simulate network
                sendEffect(LoginEffect.NavigateToHome)
            }
        }
    }
}
