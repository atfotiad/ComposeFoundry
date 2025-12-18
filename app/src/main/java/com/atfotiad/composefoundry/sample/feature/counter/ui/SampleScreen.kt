package com.atfotiad.composefoundry.sample.feature.counter.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.atfotiad.composefoundry.annotations.Destination
import com.atfotiad.composefoundry.designsystem.components.feedback.FoundryStateWrapper
import com.atfotiad.composefoundry.designsystem.foundation.architecture.StandardUiState
import com.atfotiad.composefoundry.designsystem.foundation.architecture.collectAsSafeState
import com.atfotiad.composefoundry.designsystem.foundation.theme.spacing

@Destination(route = "sample_screen", isTopLevel = true)
@Composable
fun SampleScreen(
    viewModel: SampleViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsSafeState(
        initialValue = StandardUiState.Loading
    )

    FoundryStateWrapper(
        state = state
    ) { data ->
        SampleScreenContent(data = data)
    }
}

@Composable
private fun SampleScreenContent(
    data: Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(spacing.medium)
    ) {
        // UI Here
    }
}
