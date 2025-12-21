package com.atfotiad.composefoundry.features.blackjack.data.api

import com.atfotiad.composefoundry.features.blackjack.data.api.model.PlayerResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface BlackjackApi {
    /**
     * We use JSONPlaceholder users as our "Players"
     */
    @GET("users/{id}")
    suspend fun getPlayer(@Path("id") id: Int): Response<PlayerResponse>
}
