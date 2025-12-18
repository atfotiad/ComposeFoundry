package com.atfotiad.composefoundry.designsystem.network


import com.atfotiad.composefoundry.designsystem.foundation.resources.Res
import com.atfotiad.composefoundry.designsystem.foundation.resources.UiText

/**
 * 🗺️ Maps raw network errors into user-friendly UI messages.
 */
interface NetworkErrorMapper {
    fun map(failure: NetworkResult.Failure): UiText
}

object DefaultNetworkErrorMapper : NetworkErrorMapper {
    override fun map(failure: NetworkResult.Failure): UiText {
        return when (failure) {
            is NetworkResult.Failure.NetworkError -> Res.Strings.NetworkError

            is NetworkResult.Failure.HttpError ->
                // Here we keep dynamic string for backend messages, or you can map specific codes
                UiText.DynamicString(failure.message ?: "Server Error (${failure.code})")

            else -> Res.Strings.GenericError
        }
    }
}
