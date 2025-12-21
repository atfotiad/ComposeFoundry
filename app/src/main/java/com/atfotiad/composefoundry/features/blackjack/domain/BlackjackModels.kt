package com.atfotiad.composefoundry.features.blackjack.domain

enum class Suit(val symbol: String) {
    HEARTS("❤️"), DIAMONDS("💎"), CLUBS("♣️"), SPADES("♠️")
}

enum class Rank(val value: Int, val display: String) {
    TWO(2, "2"), THREE(3, "3"), FOUR(4, "4"), FIVE(5, "5"),
    SIX(6, "6"), SEVEN(7, "7"), EIGHT(8, "8"), NINE(9, "9"),
    TEN(10, "10"), JACK(10, "J"), QUEEN(10, "Q"), KING(10, "K"),
    ACE(11, "A")
}

data class Card(
    val rank: Rank,
    val suit: Suit,
    val isFaceUp: Boolean = true
)

