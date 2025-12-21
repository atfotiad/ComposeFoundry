package com.atfotiad.composefoundry.features.login.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.atfotiad.composefoundry.annotations.Destination
import com.atfotiad.composefoundry.designsystem.components.inputs.FoundryTextField
import com.atfotiad.composefoundry.designsystem.foundation.architecture.collectAsEffect
import com.atfotiad.composefoundry.designsystem.foundation.architecture.collectAsSimpleState

@Destination(route = "login", isTopLevel = true)
@Composable
fun LoginScreen(
    viewModel: LoginViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsSimpleState()

    // Handle login effects
    viewModel.effect.collectAsEffect { effect ->
        when (effect) {
            is LoginEffect.NavigateToGame -> {
                 //navController.navigate("blackjack")
            }
            is LoginEffect.ShowError -> {
                // Show Toast
            }

            else -> {}
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Welcome Back",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 32.dp)
        )

        FoundryTextField(
            value = state.email,
            onValueChange = viewModel::onEmailChanged,
            label = "Email",
            error = state.emailError,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        FoundryTextField(
            value = state.password,
            onValueChange = viewModel::onPasswordChanged,
            label = "Password",
            visualTransformation = PasswordVisualTransformation(),
            error = state.passwordError,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(32.dp))

        if (state.isLoggingIn) {
            CircularProgressIndicator()
        } else {
            Button(
                onClick = viewModel::login,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
            ) {
                Text("Login")
            }
        }
    }
}
