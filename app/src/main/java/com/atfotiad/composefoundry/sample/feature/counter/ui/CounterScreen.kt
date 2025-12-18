package com.atfotiad.composefoundry.sample.feature.counter.ui

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.atfotiad.composefoundry.annotations.Destination
import com.atfotiad.composefoundry.designsystem.foundation.architecture.StandardUiState
import com.atfotiad.composefoundry.designsystem.components.feedback.FoundryStateWrapper
import com.atfotiad.composefoundry.designsystem.foundation.theme.spacing
import com.atfotiad.composefoundry.designsystem.foundation.architecture.collectAsEffect
import com.atfotiad.composefoundry.designsystem.foundation.architecture.collectAsSafeState
import com.atfotiad.composefoundry.navigation.Destinations
import com.atfotiad.composefoundry.ui.LocalNavController

@Destination(route = "counter_screen", isStartDestination = true, isTopLevel = true)
@Composable
fun CounterScreen(
    // Hilt automatically injects the correct ViewModel here
    viewModel: CounterViewModel = hiltViewModel()
) {
    // 1. Collect State Safely (Lifecycle aware)
    val state by viewModel.state.collectAsSafeState(StandardUiState.Loading)

    val context = LocalContext.current
    val navController = LocalNavController.current


    // 2. Collect Effects Safely (One-off events like Toasts)
    viewModel.effect.collectAsEffect { effect ->
        when (effect) {
            is CounterEffect.ShowToast -> {
                Toast.makeText(context, effect.message, Toast.LENGTH_SHORT).show()
            }

            CounterEffect.NavigateToDetails -> {
                // Handle navigation here later
                navController.navigate(Destinations.CounterDetailScreen.createRoute("5"))
            }
        }
    }

    // 3. Render UI based on State
    FoundryStateWrapper(
        state = state,
        // Optional: Retry logic passed to the error dialog
        onErrorRetry = { viewModel.refreshData() }
    ) { data ->
        // 👇 2. This block ONLY runs on Success. 'data' is the unwrapped Int.
        CounterContent(
            count = data,
            onIncrement = viewModel::increment
        )
    }
}

// Stateless Composable for Previewing easily
@Composable
private fun CounterContent(
    count: Int,
    onIncrement: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(spacing.screenPadding),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Count: $count",
            style = MaterialTheme.typography.displayMedium
        )
        Spacer(modifier = Modifier.height(spacing.large))
        Button(onClick = onIncrement){
            Text("Increment")
        }
    }
}
