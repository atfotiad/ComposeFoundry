package com.atfotiad.composefoundry.designsystem.foundation.resources

import com.atfotiad.composefoundry.designsystem.R
import com.atfotiad.composefoundry.designsystem.foundation.resources.UiText

/**
 * 💎 Res: Centralized, type-safe access to Logic Strings.
 * Defines the standard messages used by ViewModels/Repositories.
 */
object Res {
    object Strings {
        // Points to res/values/strings.xml
        val NetworkError = UiText.StringResource(R.string.error_network)
        val GenericError = UiText.StringResource(R.string.error_generic)
    }
}