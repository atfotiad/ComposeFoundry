package com.atfotiad.composefoundry.features.blackjack.ui

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.atfotiad.composefoundry.annotations.Destination
import com.atfotiad.composefoundry.designsystem.components.feedback.FoundryStateWrapper
import com.atfotiad.composefoundry.designsystem.foundation.architecture.StandardUiState
import com.atfotiad.composefoundry.designsystem.foundation.architecture.collectAsEffect
import com.atfotiad.composefoundry.designsystem.foundation.architecture.collectAsSafeState
import com.atfotiad.composefoundry.designsystem.foundation.theme.spacing
import com.atfotiad.composefoundry.features.blackjack.domain.Card
import com.atfotiad.composefoundry.features.blackjack.domain.Suit

@Destination(route = "blackjack", isTopLevel = true)
@Composable
fun BlackjackScreen(
    viewModel: BlackjackViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val state by viewModel.state.collectAsSafeState()

    // Handle game sounds or alerts
    viewModel.effect.collectAsEffect { effect ->
        when (effect) {
            is BlackjackEffect.PlayDealSound -> { /* Trigger haptic or sound */
            }

            is BlackjackEffect.ShowResult -> {
                Toast.makeText(context, effect.message.asString(context), Toast.LENGTH_SHORT).show()
            }
        }
    }

    FoundryStateWrapper(state = state) { blackjackData ->
        val isRefreshing = (state as StandardUiState.Success).isRefreshing

        BlackjackContent(
            state = blackjackData,
            isRefreshing = isRefreshing,
            onHit = viewModel::hitPlayer,
            onStand = viewModel::stand,
            onNewGame = viewModel::startNewGame
        )
    }
}

@Composable
private fun BlackjackContent(
    state: BlackjackState,
    isRefreshing: Boolean = false,
    onHit: () -> Unit,
    onStand: () -> Unit,
    onNewGame: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(spacing.medium),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        // Dealer Section
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            // Show silent loader if player profile is syncing
            if (isRefreshing) {
                LinearProgressIndicator(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(2.dp)
                )
            }
            Text("Dealer Score: ${state.dealerScore}", style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(spacing.small))
            HandDisplay(cards = state.dealerHand)
        }

        // Game Status Section
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = state.gameStatus.name,
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                color = when (state.gameStatus) {
                    GameStatus.PLAYER_WON -> Color(0xFF4CAF50)
                    GameStatus.DEALER_WON, GameStatus.BUSTED -> MaterialTheme.colorScheme.error
                    else -> MaterialTheme.colorScheme.primary
                }
            )
            if (state.gameStatus != GameStatus.PLAYING) {
                Button(onClick = onNewGame, modifier = Modifier.padding(top = 8.dp)) {
                    Text("New Game")
                }
            }
        }

        // Player Section
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            HandDisplay(cards = state.playerHand)
            Text(
                text = "${state.playerName}: ${state.playerScore}",
                style = MaterialTheme.typography.titleLarge
            )

            Row(
                modifier = Modifier.padding(top = spacing.medium),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Button(
                    onClick = onHit,
                    enabled = state.gameStatus == GameStatus.PLAYING
                ) { Text("Hit") }

                OutlinedButton(
                    onClick = onStand,
                    enabled = state.gameStatus == GameStatus.PLAYING
                ) { Text("Stand") }
            }
        }
    }
}

@Composable
private fun HandDisplay(cards: List<Card>) {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.height(120.dp)
    ) {
        items(cards) { card ->
            CardItem(card)
        }
    }
}

@Composable
private fun CardItem(card: Card) {
    val contentColor = when (card.suit) {
        Suit.HEARTS, Suit.DIAMONDS -> Color.Red
        else -> Color.Black
    }

    Surface(
        modifier = Modifier.size(80.dp, 120.dp),
        shape = MaterialTheme.shapes.medium,
        tonalElevation = 4.dp,
        border = BorderStroke(1.dp, Color.LightGray)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(4.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = card.rank.display,
                style = MaterialTheme.typography.titleLarge,
                color = contentColor
            )
            Text(
                text = card.suit.symbol,
                style = MaterialTheme.typography.titleLarge,
                color = contentColor
            )
        }
    }
}
