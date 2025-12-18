package com.atfotiad.composefoundry.designsystem.components.feedback

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.atfotiad.composefoundry.designsystem.foundation.architecture.StandardUiState
import com.atfotiad.composefoundry.designsystem.components.dialogs.CoreInfoDialog

/**
 * 🪄 Magic Wrapper: Automatically handles Loading and Error states.
 *
 * @param state The UI State from the ViewModel.
 * @param content The success content to show.
 * @param onErrorRetry (Optional) Action to take on error dialog dismiss/button.
 * @param emptyContent (Optional) Custom view for empty state. Defaults to [FoundryEmptyView].
 */
@Composable
fun <T> FoundryStateWrapper(
    state: StandardUiState<T>,
    modifier: Modifier = Modifier,
    onErrorRetry: (() -> Unit)? = null,
    emptyContent: @Composable () -> Unit = { FoundryEmptyView()},
    loadingContent: @Composable () -> Unit = {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    },
    content: @Composable (data: T) -> Unit
) {
    Box(modifier = modifier) {
        when (state) {
            is StandardUiState.Loading -> {
                loadingContent()
            }

            is StandardUiState.Empty -> {
                emptyContent()
            }

            is StandardUiState.Success -> {
                content(state.data)
            }

            is StandardUiState.Error -> {
                // Show the error dialog from your CoreDialogComponents!
                CoreInfoDialog(
                    onDismissRequest = { onErrorRetry?.invoke() },
                    title = "Error",
                    message = state.message.asString(),
                    buttonText = "Retry"
                )
            }
        }
    }
}
