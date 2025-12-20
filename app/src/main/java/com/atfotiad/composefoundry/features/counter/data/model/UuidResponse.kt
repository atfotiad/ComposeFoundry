package com.atfotiad.composefoundry.features.counter.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UuidResponse(
    @SerialName("uuid") val uuid: String
)