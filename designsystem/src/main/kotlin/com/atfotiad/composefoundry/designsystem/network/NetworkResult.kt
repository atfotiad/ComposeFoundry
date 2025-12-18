package com.atfotiad.composefoundry.designsystem.network

import java.io.IOException

/**
 * 📦 NetworkResult: A standardized wrapper for Repository responses.
 *
 * This separates the Data Layer (Success/Failure) from the UI Layer (Loading/Content/Error).
 * Repositories should return this type.
 */
sealed interface NetworkResult<out T> {

    /**
     * Operation succeeded with data.
     */
    data class Success<out T>(val data: T) : NetworkResult<T>

    /**
     * Operation failed.
     */
    sealed interface Failure : NetworkResult<Nothing> {
        val error: Throwable?

        /**
         * Server returned an error code (e.g., 404, 500).
         */
        data class HttpError(val code: Int, val message: String?, override val error: Throwable? = null) : Failure

        /**
         * Device is offline or DNS failed.
         */
        data class NetworkError(override val error: IOException) : Failure

        /**
         * Parsing failed or unknown exception.
         */
        data class UnknownError(override val error: Throwable) : Failure
    }
}
