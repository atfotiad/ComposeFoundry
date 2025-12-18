package com.atfotiad.composefoundry.sample.feature.counter.api

import com.atfotiad.composefoundry.sample.feature.counter.data.UuidResponse
import retrofit2.Response
import retrofit2.http.GET

interface CounterApi {
    @GET("uuid")
    suspend fun getRandomUuid(): Response<UuidResponse>
}