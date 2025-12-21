package com.atfotiad.composefoundry.features.blackjack.data.api.model

import kotlinx.serialization.Serializable

@Serializable
data class PlayerResponse(
    val id: Int,
    val name: String,
    val email: String,
    val website: String // We'll use this for a fake "Balance" or "Avatar"
)
