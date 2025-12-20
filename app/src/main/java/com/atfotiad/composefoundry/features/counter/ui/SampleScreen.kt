package com.atfotiad.composefoundry.features.counter.ui

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.atfotiad.composefoundry.annotations.Destination
import com.atfotiad.composefoundry.designsystem.components.feedback.FoundryStateWrapper
import com.atfotiad.composefoundry.designsystem.foundation.architecture.StandardUiState
import com.atfotiad.composefoundry.designsystem.foundation.architecture.collectAsEffect
import com.atfotiad.composefoundry.designsystem.foundation.architecture.collectAsSafeState
import com.atfotiad.composefoundry.designsystem.foundation.theme.spacing

@Destination(route = "sample_screen", isTopLevel = true)
@Composable
fun SampleScreen(
    viewModel: SampleViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val state by viewModel.state.collectAsSafeState(
        initialValue = StandardUiState.Loading
    )

    viewModel.effect.collectAsEffect { effect ->
        when (effect) {
            is CounterEffect.ShowToast -> {
                Toast.makeText(
                    context,
                    effect.message.asString(context),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    FoundryStateWrapper(
        state = state
    ) {
        val isRefreshing = (state as? StandardUiState.Success)?.isRefreshing ?: false

        SampleScreenContent(
            isRefreshing = isRefreshing,
            onSyncClick = viewModel::performSilentSync
        )
    }
}

@Composable
private fun SampleScreenContent(
    isRefreshing: Boolean = false,
    onSyncClick: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(spacing.medium),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Foundry Infrastructure Demo",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(spacing.medium))

        // Silent Loader (Visible only during background sync)
        if (isRefreshing) {
            CircularProgressIndicator(modifier = Modifier.size(24.dp))
            Spacer(modifier = Modifier.height(spacing.small))
        }

        Button(
            onClick = onSyncClick,
            enabled = !isRefreshing,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(if (isRefreshing) "Syncing..." else "Test Silent Sync & Notify")
        }

        Spacer(modifier = Modifier.height(spacing.small))

        Text(
            text = "Long-press the Top Bar (Offline Banner area) to open Debug Menu",
            style = MaterialTheme.typography.bodySmall,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.outline
        )
    }
}
