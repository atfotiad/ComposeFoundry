package com.atfotiad.composefoundry.designsystem.notifications

import com.atfotiad.composefoundry.designsystem.foundation.resources.UiText

interface Notifier {
    /**
     * Shows a system notification.
     * @param id Unique ID for this notification.
     * @param title Title of the notification (UiText for localization).
     * @param message Body text of the notification.
     * @param channelId The ID of the channel to post to.
     * @param deepLink Optional route (KSP generated) to navigate to when tapped.
     */
    fun showNotification(
        id: Int,
        title: UiText,
        message: UiText,
        channelId: String,
        deepLink: String? = null
    )

    /**
     * Creates a notification channel (Required for Android 8.0+).
     */
    fun createChannel(
        id: String,
        name: String,
        description: String? = null,
        importance: Int = 3
    )
}
