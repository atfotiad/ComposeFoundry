package com.atfotiad.composefoundry.features.counter.ui

import androidx.lifecycle.viewModelScope
import com.atfotiad.composefoundry.designsystem.foundation.architecture.StandardUiState
import com.atfotiad.composefoundry.designsystem.foundation.architecture.StandardViewModel
import com.atfotiad.composefoundry.designsystem.foundation.architecture.UiEffect
import com.atfotiad.composefoundry.designsystem.foundation.resources.UiText
import com.atfotiad.composefoundry.designsystem.network.NetworkResult
import com.atfotiad.composefoundry.designsystem.network.asUiState
import com.atfotiad.composefoundry.designsystem.notifications.Notifier
import com.atfotiad.composefoundry.navigation.Destinations
import com.atfotiad.composefoundry.features.counter.repository.CounterRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class SampleViewModel @Inject constructor(
    private val repository: CounterRepository,
    private val notifier: Notifier
) : StandardViewModel<Unit, UiEffect>() {

    init {
        loadData()
    }

    private fun loadData() {
        viewModelScope.launch {
            setState { StandardUiState.Loading }
            val result = repository.syncCounterFromNetwork()
            setState { result.asUiState() }
        }
    }

    /**
     * Example: Background sync without hiding the UI
     */
    fun performSilentSync() {
        viewModelScope.launch {
            setRefreshing(true)
            val result =  repository.syncCounterFromNetwork()
            setRefreshing(false)

            when(result) {
                is NetworkResult.Success -> {
                    notifier.showNotification(
                        id = 1,
                        title = UiText.DynamicString("Sync Complete"),
                        message = UiText.DynamicString("Your data is now up to date."),
                        channelId = "sync_channel",
                        deepLink = Destinations.SampleScreen.route //Deep link back home!
                    )
                }
                is NetworkResult.Failure -> {
                    val uiState = result.asUiState()
                    val message = uiState.errorMessage ?: UiText.DynamicString("Unknown Error")
                    sendEffect(CounterEffect.ShowToast(message))
                }

            }
        }
    }
}
