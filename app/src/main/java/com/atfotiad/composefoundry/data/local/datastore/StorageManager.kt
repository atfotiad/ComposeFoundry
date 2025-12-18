package com.atfotiad.composefoundry.data.local.datastore

import android.content.Context
import androidx.datastore.dataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StorageManager @Inject constructor(
    @param:ApplicationContext private val context: Context
) {
    // Single, Encrypted, Type-Safe DataStore
    private val Context.dataStore by dataStore(
        fileName = "app_settings.json",
        serializer = AppSettingsSerializer()
    )

    // Expose the raw object flow
    val appSettings: Flow<AppSettings> = context.dataStore.data

    // Expose specific flows helper
    val authToken: Flow<String?> = appSettings.map { it.authToken }
    val themeMode: Flow<String> = appSettings.map { it.themeMode }

    // Unified update function
    suspend fun update(action: (AppSettings) -> AppSettings) {
        context.dataStore.updateData(action)
    }

    // Helper functions
    suspend fun saveToken(token: String) {
        update { it.copy(authToken = token) }
    }

    suspend fun saveTheme(mode: String) {
        update { it.copy(themeMode = mode) }
    }

    suspend fun clearSession() {
        update { it.copy(authToken = null, refreshToken = null) }
    }
}
