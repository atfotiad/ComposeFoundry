package com.atfotiad.composefoundry

import android.app.Application
import com.atfotiad.composefoundry.designsystem.foundation.logging.FoundryLogger
import com.atfotiad.composefoundry.designsystem.notifications.Notifier
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class ComposeFoundryApplication : Application() {

    @Inject
    lateinit var notifier: Notifier

    override fun onCreate() {
        super.onCreate()

        // Initialize Logger
        FoundryLogger.init(BuildConfig.DEBUG)

        // Initialize standard channels
        notifier.createChannel(
            id = "sync_channel",
            name = "Data Sync",
            description = "Notifications about background data synchronization",
            importance = 3 // NotificationManager.IMPORTANCE_DEFAULT
        )
    }
}
