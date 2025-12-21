package com.atfotiad.composefoundry.features.blackjack.ui

import com.atfotiad.composefoundry.designsystem.foundation.architecture.UiEffect
import com.atfotiad.composefoundry.designsystem.foundation.architecture.UiState
import com.atfotiad.composefoundry.designsystem.foundation.resources.UiText
import com.atfotiad.composefoundry.features.blackjack.domain.Card

data class BlackjackState(
    val playerName: String = "",
    val playerBalance: Int = 1000,
    val playerHand: List<Card> = emptyList(),
    val dealerHand: List<Card> = emptyList(),
    val gameStatus: GameStatus = GameStatus.WAITING,
    val playerScore: Int = 0,
    val dealerScore: Int = 0
) : UiState

enum class GameStatus { WAITING, PLAYING, PLAYER_WON, DEALER_WON, PUSH, BUSTED }

sealed interface BlackjackEffect :
    UiEffect {
    data class ShowResult(val message: UiText) : BlackjackEffect
    data object PlayDealSound : BlackjackEffect
}
