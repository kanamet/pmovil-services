package com.pmovil.services

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

class AppUtils {
    companion object {
        private const val NOTIFICATION_CHANNEL_MAIN = "NOTIFICATION_CHANNEL_MAIN"

        fun showNotification(context: Context?, title: String, message: String, id: Int) {
            if (context != null) {
                initNotificationChannels(context)
                val notification = getNotificationBuilder(context, title, message)
                notification?.let {
                    NotificationManagerCompat.from(context)
                        .notify(id, notification.build())
                }
            }
        }

        fun getNotificationBuilder(
            context: Context?,
            title: String,
            message: String
        ): NotificationCompat.Builder? {
            return if (context != null) {
                initNotificationChannels(context)
                NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_MAIN)
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setSmallIcon(R.drawable.ic_launcher_foreground)
                    .setContentTitle(title)
                    .setStyle(
                        NotificationCompat.BigTextStyle()
                            .bigText(message)
                    )
            } else {
                null
            }
        }

        private fun initNotificationChannels(context: Context) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                createNotificationChannel(
                    context,
                    NOTIFICATION_CHANNEL_MAIN,
                    "MAIN CHANNEL",
                    NotificationManager.IMPORTANCE_DEFAULT,
                    "All notifications use this channel"
                )
            }
        }

        private fun createNotificationChannel(
            context: Context,
            id: String,
            name: CharSequence,
            importance: Int,
            description: String
        ) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val notificationChannel =
                    NotificationChannel(id, name, importance)
                notificationChannel.description = description
                NotificationManagerCompat.from(context)
                    .createNotificationChannel(notificationChannel)
            }
        }

        fun isPrime(num: Int): Boolean {
            return if (num <= 3) {
                num > 1
            } else {
                if (num % 2 == 0 || num % 3 == 0) false
                else {
                    for (i in 5..(num / 2) step 6) {
                        if (num % i == 0 || num % (i + 2) == 0) return false
                    }
                    true
                }
            }
        }

        fun sendServiceMessageBroadcast(context: Context, name: String, status: String) {
            Intent(AppConstants.ACTION_SERVICE_MESSAGE).apply {
                putExtra(AppConstants.EXTRA_SERVICE_NAME, name)
                putExtra(AppConstants.EXTRA_SERVICE_STATUS, status)

                context.sendBroadcast(this)
            }
        }
    }
}