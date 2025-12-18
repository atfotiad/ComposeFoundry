package com.atfotiad.composefoundry.sample.feature.counter.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UuidResponse(
    @SerialName("uuid") val uuid: String
)