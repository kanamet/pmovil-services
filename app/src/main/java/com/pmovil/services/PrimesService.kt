package com.pmovil.services

import android.app.Service
import android.content.Intent
import android.os.IBinder

class PrimesService : Service() {

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        AppUtils.sendServiceMessageBroadcast(
            this,
            AppConstants.SERVICE_PRIMES,
            AppConstants.SERVICE_STATUS_RUNNING
        )

        if (intent?.hasExtra(AppConstants.EXTRA_MAX_VALUE) == true) {
            val maxValue = intent.getIntExtra(AppConstants.EXTRA_MAX_VALUE, 0)
            val useThread = intent.getBooleanExtra(AppConstants.EXTRA_USE_THREAD, false)

            var primesCount = 0
            if (useThread) {
                val runnable = Runnable {
                    for (i in 1..maxValue) {
                        if (AppUtils.isPrime(i)) {
                            primesCount++
                        }
                    }

                    AppUtils.showNotification(
                        baseContext,
                        "Primes Service - Thread",
                        "There are $primesCount prime numbers below $maxValue",
                        301
                    )

                    stopSelf()
                }
                Thread(runnable).start()
            } else {
                for (i in 1..maxValue) {
                    if (AppUtils.isPrime(i)) {
                        primesCount++
                    }
                }

                AppUtils.showNotification(
                    baseContext,
                    "Primes Service",
                    "There are $primesCount prime numbers below $maxValue",
                    300
                )

                stopSelf()
            }
        }

        return START_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        AppUtils.sendServiceMessageBroadcast(
            this,
            AppConstants.SERVICE_PRIMES,
            AppConstants.SERVICE_STATUS_CREATED
        )
    }

    override fun onDestroy() {
        AppUtils.sendServiceMessageBroadcast(
            this,
            AppConstants.SERVICE_PRIMES,
            AppConstants.SERVICE_STATUS_DESTROYED
        )
        super.onDestroy()
    }
}