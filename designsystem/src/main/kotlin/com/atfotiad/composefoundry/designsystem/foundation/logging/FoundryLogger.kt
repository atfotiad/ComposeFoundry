package com.atfotiad.composefoundry.designsystem.foundation.logging

import android.util.Log

/**
 * Standardized Logger for the Foundry.
 * Automatically handles build-type logic (no logs in release).
 */
object FoundryLogger {
    private var isDebug = false

    fun init(debug: Boolean) {
        isDebug = debug
    }

    fun d(tag: String, message: String) {
        if (isDebug) Log.d(tag, message)
    }

    fun e(tag: String, message: String, throwable: Throwable? = null) {
        if (isDebug) Log.e(tag, message, throwable)
    }
}
