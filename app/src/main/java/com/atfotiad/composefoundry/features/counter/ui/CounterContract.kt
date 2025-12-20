package com.atfotiad.composefoundry.features.counter.ui

import com.atfotiad.composefoundry.designsystem.foundation.architecture.StandardUiState
import com.atfotiad.composefoundry.designsystem.foundation.architecture.UiEffect
import com.atfotiad.composefoundry.designsystem.foundation.resources.UiText

// Uses new StandardUiState!
typealias CounterState = StandardUiState<Int>

sealed interface CounterEffect : UiEffect {
    data object NavigateToDetails : CounterEffect
    data class ShowToast(val message: UiText) : CounterEffect
}
