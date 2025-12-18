package com.atfotiad.composefoundry.di

import android.content.Context
import androidx.room.Room
import com.atfotiad.composefoundry.data.local.AppDatabase
import com.atfotiad.composefoundry.sample.feature.counter.data.CounterDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "foundry_database.db"
        ).build()
    }

    @Provides
    fun provideCounterDao(database: AppDatabase): CounterDao {
        return database.counterDao()
    }
}
