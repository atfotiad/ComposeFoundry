package com.atfotiad.composefoundry.features.blackjack.ui

import androidx.lifecycle.viewModelScope
import com.atfotiad.composefoundry.designsystem.foundation.architecture.StandardViewModel
import com.atfotiad.composefoundry.designsystem.foundation.resources.UiText
import com.atfotiad.composefoundry.designsystem.network.dataOrNull
import com.atfotiad.composefoundry.features.blackjack.data.repository.BlackjackRepository
import com.atfotiad.composefoundry.features.blackjack.domain.Card
import com.atfotiad.composefoundry.features.blackjack.domain.Rank
import com.atfotiad.composefoundry.features.blackjack.domain.Suit
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BlackjackViewModel @Inject constructor(
    private val repository: BlackjackRepository
) : StandardViewModel<BlackjackState ,BlackjackEffect>(
    initialData = BlackjackState() // Start in Success state with default data
) {

    private var deck :MutableList<Card> = mutableListOf()

    private fun generateShuffledDeck(): MutableList<Card> {
        val newDeck = mutableListOf<Card>()
        Suit.entries.forEach { suit ->
            Rank.entries.forEach { rank ->
                newDeck.add(Card(rank, suit))
            }
        }
        // Fisher-Yates Shuffle
        newDeck.shuffle()
        return newDeck
    }

    init {
        fetchPlayerProfile()
    }

    private fun fetchPlayerProfile() {
        viewModelScope.launch {
            setRefreshing(true)
            val result = repository.getPlayerProfile(1)
            result.dataOrNull()?.let { player ->
                updateData { copy(playerName = player.name) }
            }
            setRefreshing(false)
        }
    }

    fun startNewGame() {

        deck = generateShuffledDeck()
        updateData {
            copy(
                playerHand = emptyList(),
                dealerHand = emptyList(),
                gameStatus = GameStatus.PLAYING,
                playerScore = 0,
                dealerScore = 0
            )
        }
        dealInitialCards()
    }

    private fun dealInitialCards() {
        viewModelScope.launch {
            hitPlayer()
            delay(300)
            hitPlayer()
            if (currentState.dataOrNull?.playerScore == 21) {determineWinner()}
            sendEffect(BlackjackEffect.PlayDealSound)
        }
    }

    fun hitPlayer() {
        updateData {
            val nextCard = drawCard()
            val newHand = playerHand + nextCard
            val newScore = calculateScore(newHand)

            copy(
                playerHand = newHand,
                playerScore = newScore,
                gameStatus = if (newScore > 21) GameStatus.BUSTED else GameStatus.PLAYING
            )
        }

        val score = currentState.dataOrNull?.playerScore ?: 0
        if (score == 21) {
            determineWinner()
        } else if (score > 21) {
            determineWinner()
        }
    }

    private fun drawCard(): Card {
        // If deck gets low, reshuffle (simulating a "shoe" change)
        if (deck.isEmpty()) {
            deck = generateShuffledDeck()
        }
        return deck.removeAt(0)
    }

    private fun calculateScore(hand: List<Card>): Int {
        var score = hand.sumOf { it.rank.value }
        var aces = hand.count { it.rank == Rank.ACE }
        while (score > 21 && aces > 0) {
            score -= 10
            aces--
        }
        return score
    }

    fun stand() {
        viewModelScope.launch {
            updateData { copy(gameStatus = GameStatus.PLAYING) }

            // Dealer's Turn Logic
            var currentDealerScore = currentState.dataOrNull?.dealerScore ?: 0

            while (currentDealerScore < 17) {
                delay(800) // Simulate "thinking" time
                dealCardToDealer()
                currentDealerScore = currentState.dataOrNull?.dealerScore ?: 0
            }

            determineWinner()
        }
    }

    private fun dealCardToDealer() {
        updateData {
            val card = Card(Rank.entries.random(), Suit.entries.random())
            val newHand = dealerHand + card
            copy(
                dealerHand = newHand,
                dealerScore = calculateScore(newHand)
            )
        }
    }

    private fun determineWinner() {
        updateData {
            val pScore = playerScore
            val dScore = dealerScore

            val finalStatus = when {
                pScore > 21 -> GameStatus.BUSTED
                dScore > 21 -> GameStatus.PLAYER_WON

                //  Scores are equal -> PUSH (Tie)
                pScore == dScore -> GameStatus.PUSH

                // Higher score wins
                pScore > dScore -> GameStatus.PLAYER_WON
                else -> GameStatus.DEALER_WON
            }

            // Send effect to UI
            val resultMessage = when(finalStatus) {
                GameStatus.PLAYER_WON -> {
                    if (pScore == 21) {
                        "Blackjack! 🎉"
                    } else {
                        "You Won! 🎉"
                    }
                }
                GameStatus.DEALER_WON -> "Dealer Wins 🏦"
                GameStatus.PUSH -> "It's a Tie! 🤝"
                GameStatus.BUSTED -> "Busted! 💥"
                else -> ""
            }
            sendEffect(BlackjackEffect.ShowResult(UiText.DynamicString(resultMessage)))

            copy(gameStatus = finalStatus)
        }
    }
}
