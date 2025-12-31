package com.atfotiad.composefoundry.designsystem.foundation.storage

import kotlinx.serialization.Serializable

// 1. Define ALL your app data here. Everything will be encrypted.
@Serializable
data class AppSettings(
    val authToken: String? = null,
    val refreshToken: String? = null,
    val themeMode: String = "system",
    val hasSeenOnboarding: Boolean = false,
    val lastSyncTimestamp: Long = 0,
    val apiEnvironment: String = "production"
)

// 2. Create the specific serializer using the Library's tool
class AppSettingsSerializer : EncryptedSerializer<AppSettings>(
    serializer = AppSettings.serializer(),
    defaultValue = AppSettings()
)
