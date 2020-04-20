package com.pmovil.services

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import androidx.core.app.NotificationManagerCompat
import java.util.*

class BindService : Service() {

    private val mBinder = LocalBinder()

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return START_STICKY
    }

    fun calculatePrimes(maxValue: Int) {
        var primesCount = 0
        val runnable = Runnable {
            for (i in 1..maxValue) {
                if (AppUtils.isPrime(i)) {
                    primesCount++
                }
            }

            AppUtils.showNotification(
                baseContext,
                "Bind Service",
                "There are $primesCount prime numbers below $maxValue",
                301
            )
        }

        Thread(runnable).start()
    }

    fun downloadFile(fileName: String) {
        val maxProgress = 100
        var progress = 1

        val runnable = Runnable {
            val notificationBuilder =
                AppUtils.getNotificationBuilder(this, "Bind Service", "Downloading file $fileName")

            while (progress < maxProgress) {
                Thread.sleep(100)
                progress++

                notificationBuilder?.let {
                    notificationBuilder.setProgress(maxProgress, progress, false)
                    NotificationManagerCompat.from(this).notify(800, notificationBuilder.build())
                }
            }
        }

        Thread(runnable).start()
    }

    fun getRandomNumber(): Int {
        return Random().nextInt(100)
    }

    inner class LocalBinder : Binder() {
        fun calculatePrimes(maxValue: Int) {
            this@BindService.calculatePrimes(maxValue)
        }

        fun getRandomNumber(): Int {
            return this@BindService.getRandomNumber()
        }
    }

    override fun onBind(intent: Intent): IBinder {
        return mBinder
    }
}
