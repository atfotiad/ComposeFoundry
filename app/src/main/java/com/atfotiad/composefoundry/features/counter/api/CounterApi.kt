package com.atfotiad.composefoundry.features.counter.api

import com.atfotiad.composefoundry.features.counter.data.model.UuidResponse
import retrofit2.Response
import retrofit2.http.GET

interface CounterApi {
    @GET("uuid")
    suspend fun getRandomUuid(): Response<UuidResponse>
}