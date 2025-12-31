package com.atfotiad.composefoundry.core.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import com.atfotiad.composefoundry.designsystem.foundation.storage.AppSettings
import com.atfotiad.composefoundry.designsystem.foundation.storage.AppSettingsSerializer
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

// Define the delegate outside the class to ensure a single instance app-wide
private val Context.appDataStore: DataStore<AppSettings> by dataStore(
    fileName = "app_settings.json",
    serializer = AppSettingsSerializer()
)

@Module
@InstallIn(SingletonComponent::class)
object DataStoreModule {

    @Provides
    @Singleton
    fun provideDataStore(@ApplicationContext context: Context): DataStore<AppSettings> {
        return context.appDataStore
    }
}
