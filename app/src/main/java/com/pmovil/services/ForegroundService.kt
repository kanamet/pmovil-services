package com.pmovil.services

import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.IBinder

class ForegroundService : Service() {

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val notificationBuilder =
            AppUtils.getNotificationBuilder(this, "Foreground Service", "Message")

        val openAppIntent = Intent(this, MainActivity::class.java)
        val openAppPendingIntent = PendingIntent.getActivity(this, 900, openAppIntent, 0)

        notificationBuilder?.setContentIntent(openAppPendingIntent)

        startForeground(100, notificationBuilder?.build())

        if (intent?.hasExtra(AppConstants.EXTRA_DELAY_TIME) == true) {
            val delayTime = intent.getIntExtra(AppConstants.EXTRA_DELAY_TIME, 0)

            Thread(Runnable {
                Thread.sleep(delayTime * 1000L)
                stopForeground(true)
            }).start()
        }
        return START_STICKY
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }
}
