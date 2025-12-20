package com.atfotiad.composefoundry.core.di

import com.atfotiad.composefoundry.designsystem.notifications.Notifier
import com.atfotiad.composefoundry.core.notifications.FoundryNotifier
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class NotificationModule {
    @Binds
    @Singleton
    abstract fun bindNotifier(impl: FoundryNotifier): Notifier
}