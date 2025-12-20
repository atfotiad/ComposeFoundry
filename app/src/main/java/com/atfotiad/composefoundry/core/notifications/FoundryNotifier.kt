package com.atfotiad.composefoundry.core.notifications

import android.R
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.TaskStackBuilder
import androidx.core.net.toUri
import com.atfotiad.composefoundry.designsystem.foundation.resources.UiText
import com.atfotiad.composefoundry.designsystem.notifications.Notifier
import com.atfotiad.composefoundry.ui.MainActivity
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FoundryNotifier @Inject constructor(
    @param:ApplicationContext private val context: Context
) : Notifier {

    private val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    override fun createChannel(id: String, name: String, description: String?, importance: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(id, name, importance).apply {
                this.description = description
            }
            notificationManager.createNotificationChannel(channel)
        }
    }

    override fun showNotification(id: Int, title: UiText, message: UiText, channelId: String, deepLink: String?) {
        // Build the intent for the deep link
        val intent = Intent(
            Intent.ACTION_VIEW,
            deepLink?.toUri() ?: Uri.EMPTY,
            context,
            MainActivity::class.java
        )

        // Use TaskStackBuilder to ensure the 'Back' button works properly from the notification
        val pendingIntent = TaskStackBuilder.create(context).run {
            addNextIntentWithParentStack(intent)
            getPendingIntent(0, PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT)
        }

        val builder = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.ic_dialog_info) // TODO: Use Foundry Brand Icon
            .setContentTitle(title.asString(context))
            .setContentText(message.asString(context))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        notificationManager.notify(id, builder.build())
    }
}
