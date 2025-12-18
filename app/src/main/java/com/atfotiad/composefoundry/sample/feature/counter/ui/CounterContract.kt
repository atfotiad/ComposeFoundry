package com.atfotiad.composefoundry.sample.feature.counter.ui

import com.atfotiad.composefoundry.designsystem.foundation.architecture.StandardUiState
import com.atfotiad.composefoundry.designsystem.foundation.architecture.UiEffect

// Uses new StandardUiState!
typealias CounterState = StandardUiState<Int>

sealed interface CounterEffect : UiEffect {
    data object NavigateToDetails : CounterEffect
    data class ShowToast(val message: String) : CounterEffect
}
