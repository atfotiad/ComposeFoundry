package com.atfotiad.composefoundry.core.data.local.datastore

import androidx.datastore.core.DataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StorageManager @Inject constructor(
    private val dataStore: DataStore<AppSettings>
) {

    // Expose the raw object flow
    val appSettings: Flow<AppSettings> = dataStore.data

    // Expose specific flows helper
    val authToken: Flow<String?> = appSettings.map { it.authToken }
    val themeMode: Flow<String> = appSettings.map { it.themeMode }
    val apiEnvironment: Flow<String> = appSettings.map { it.apiEnvironment }

    val mutex = Mutex()

    // Unified update function
    suspend fun update(action: (AppSettings) -> AppSettings) {
        mutex.withLock {
            dataStore.updateData(action)
        }
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

    suspend fun updateApiEnvironment(environment: String) {
        update { it.copy(apiEnvironment = environment) }
    }

    // Global reset for the Debug Menu
    suspend fun resetAll() {
        update { AppSettings() }
    }

}