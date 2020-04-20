package com.pmovil.services

import android.app.IntentService
import android.content.Intent
import android.util.Log

class PrimesIntentService : IntentService("PrimesIntentService") {

    private var requestCount = 0

    override fun onHandleIntent(intent: Intent?) {
        requestCount++

        AppUtils.sendServiceMessageBroadcast(
            this,
            AppConstants.SERVICE_PRIMES_INTENT,
            AppConstants.SERVICE_STATUS_RUNNING
        )

        if (intent?.hasExtra(AppConstants.EXTRA_MAX_VALUE) == true) {
            val maxValue = intent.getIntExtra(AppConstants.EXTRA_MAX_VALUE, 0)

            var primesCount = 0
            for (i in 1..maxValue) {
                if (AppUtils.isPrime(i)) {
                    primesCount++
                }
            }

            AppUtils.showNotification(
                baseContext,
                "Primes Intent Service",
                "Request $requestCount - There are $primesCount prime numbers below $maxValue",
                200 + requestCount
            )
        }
    }

    override fun onCreate() {
        super.onCreate()
        AppUtils.sendServiceMessageBroadcast(
            this,
            AppConstants.SERVICE_PRIMES_INTENT,
            AppConstants.SERVICE_STATUS_CREATED
        )
    }

    override fun onDestroy() {
        AppUtils.sendServiceMessageBroadcast(
            this,
            AppConstants.SERVICE_PRIMES_INTENT,
            AppConstants.SERVICE_STATUS_DESTROYED
        )
        super.onDestroy()
    }
}
