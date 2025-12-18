package com.atfotiad.composefoundry.designsystem.foundation.resources

import android.content.Context
import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource

/**
 * 🅰️ UiText: A wrapper for strings that resolves later in the UI.
 *
 * This allows ViewModels to return text without needing a Context, supporting
 * both dynamic strings (from API) and static resources (localized).
 */
sealed interface UiText {

    /**
     * A raw string (e.g., "Hello User" from JSON).
     */
    data class DynamicString(val value: String) : UiText

    /**
     * A localized resource ID (e.g., R.string.error_generic).
     */
    class StringResource(
        @param:StringRes val resId: Int,
        vararg val args: Any
    ) : UiText

    /**
     * Resolves the text inside a Composable function.
     */
    @Composable
    fun asString(): String {
        return when (this) {
            is DynamicString -> value
            is StringResource -> stringResource(resId, *args)
        }
    }

    /**
     * Resolves the text using a standard Context (for Toasts/logging).
     */
    fun asString(context: Context): String {
        return when (this) {
            is DynamicString -> value
            is StringResource -> context.getString(resId, *args)
        }
    }
}