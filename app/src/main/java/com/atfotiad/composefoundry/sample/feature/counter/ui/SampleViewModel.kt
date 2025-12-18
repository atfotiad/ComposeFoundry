package com.atfotiad.composefoundry.sample.feature.counter.ui

import androidx.lifecycle.viewModelScope
import com.atfotiad.composefoundry.designsystem.foundation.architecture.BaseViewModel
import com.atfotiad.composefoundry.designsystem.foundation.architecture.StandardUiState
import com.atfotiad.composefoundry.designsystem.foundation.architecture.UiEffect
import com.atfotiad.composefoundry.designsystem.foundation.architecture.UiState
import com.atfotiad.composefoundry.designsystem.network.asUiState
import com.atfotiad.composefoundry.sample.feature.counter.data.CounterRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject



@HiltViewModel
class SampleViewModel @Inject constructor(
    private val repository: CounterRepository
) : BaseViewModel<StandardUiState<Unit>, UiEffect>(
    initialState = StandardUiState.Loading
) {

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
}
