package com.atfotiad.composefoundry.features.counter.ui

import androidx.lifecycle.viewModelScope
import com.atfotiad.composefoundry.designsystem.foundation.architecture.BaseViewModel
import com.atfotiad.composefoundry.designsystem.foundation.architecture.StandardUiState
import com.atfotiad.composefoundry.designsystem.network.NetworkResult
import com.atfotiad.composefoundry.features.counter.repository.CounterRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CounterViewModel @Inject constructor(
    private val repository: CounterRepository
) : BaseViewModel<CounterState, CounterEffect>(
    initialState = StandardUiState.Loading
) {
    init {
        observeDatabase()
        refreshData()
    }

    private fun observeDatabase() {
        viewModelScope.launch {
            repository.getCounterStream().collect { count ->
                // Every time DB changes, we update the UI State
                setState { StandardUiState.Success(count) }
            }
        }
    }

    fun refreshData() {
        viewModelScope.launch {
            // We don't set Loading here because we want to show cached data while refreshing!
            // This is "Stale-While-Revalidate" pattern.

            val result = repository.syncCounterFromNetwork()

            if (result is NetworkResult.Failure) {
                // If network fails, show an effect (Toast) but keep showing old DB data
                sendEffect(CounterEffect.ShowToast("Offline Mode: Could not sync"))
            }
        }
    }
    fun increment() {
        viewModelScope.launch {
            repository.incrementLocally()
            // No need to setState! The DB update triggers observeDatabase() -> setState

            // Logic check for navigation (using the optimistic future value)
            // Note: In a real app, you might want to check the actual emitted value
            // but for this simple Logic, we can assume:
            val current = currentState.dataOrNull ?: 0
            if (current + 1 >= 5) {
                sendEffect(CounterEffect.NavigateToDetails)
            }
        }
    }
}