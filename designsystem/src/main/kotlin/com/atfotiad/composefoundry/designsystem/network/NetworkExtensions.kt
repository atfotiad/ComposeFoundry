package com.atfotiad.composefoundry.designsystem.network

import com.atfotiad.composefoundry.designsystem.foundation.architecture.StandardUiState
import retrofit2.Response
import java.io.IOException

/**
 * ⚡ Executes a Retrofit call safely and returns a [NetworkResult].
 *
 * Usage:
 * val result = api::getCounter.safeCall()
 * OR
 * val result = { api.getCounter() }.safeCall()
 */
suspend fun <T> (suspend () -> Response<T>).safeCall(): NetworkResult<T> {
    return try {
        // 1. Trigger the network call
        val response = invoke()
        val body = response.body()

        // 2. Map success/failure
        if (response.isSuccessful && body != null) {
            NetworkResult.Success(body)
        } else {
            NetworkResult.Failure.HttpError(
                code = response.code(),
                message = response.errorBody()?.string(),
                error = null
            )
        }
    } catch (e: IOException) {
        NetworkResult.Failure.NetworkError(e)
    } catch (e: Exception) {
        e.printStackTrace()
        NetworkResult.Failure.UnknownError(e)
    }
}

/**
 * 🔄 Maps a [NetworkResult] directly to a [StandardUiState].
 *
 * @param mapper Strategy to convert network failures into user messages.
 */
fun <T> NetworkResult<T>.asUiState(
    mapper: NetworkErrorMapper = DefaultNetworkErrorMapper
): StandardUiState<T> {
    return when (this) {
        is NetworkResult.Success -> StandardUiState.Success(this.data)
        is NetworkResult.Failure -> {
            // Use the mapper to get the localized/safe text
            val uiText = mapper.map(this)

            StandardUiState.Error(uiText, this.error)
        }
    }
}

/**
 * 🔄 specialized mapper for Lists.
 * Maps empty lists to [StandardUiState.Empty] automatically.
 */
fun <T> NetworkResult<List<T>>.asUiStateList(
    mapper: NetworkErrorMapper = DefaultNetworkErrorMapper
): StandardUiState<List<T>> {
    return when (this) {
        is NetworkResult.Success -> {
            if (this.data.isEmpty()) {
                StandardUiState.Empty
            } else {
                StandardUiState.Success(this.data)
            }
        }
        is NetworkResult.Failure -> this.asUiState(mapper)
    }
}

/**
 * 🔓 Unwraps the data if the result is Success, otherwise returns null.
 *
 * Useful for simple logic checks in ViewModels:
 * val currentList = result.dataOrNull() ?: emptyList()
 */
fun <T> NetworkResult<T>.dataOrNull(): T? {
    return (this as? NetworkResult.Success)?.data
}