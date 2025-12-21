package com.atfotiad.composefoundry.features.blackjack.data.repository

import com.atfotiad.composefoundry.designsystem.network.NetworkResult
import com.atfotiad.composefoundry.designsystem.network.safeCall
import com.atfotiad.composefoundry.features.blackjack.data.api.BlackjackApi
import com.atfotiad.composefoundry.features.blackjack.data.api.model.PlayerResponse
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BlackjackRepository @Inject constructor(
    private val api: BlackjackApi
) {
    /**
     * Uses the Foundry 'safeCall' extension to bridge Retrofit to NetworkResult.
     */
    suspend fun getPlayerProfile(id: Int): NetworkResult<PlayerResponse> {
        return suspend { api.getPlayer(id) }.safeCall()
    }
}
