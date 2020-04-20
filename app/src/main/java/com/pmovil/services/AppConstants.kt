package com.pmovil.services

class AppConstants {
    companion object {
        const val ACTION_ALARM = "com.pmovil.services.ALARM"
        const val ACTION_SERVICE_MESSAGE = "com.pmovil.services.MESSAGE"

        const val EXTRA_DELAY_TIME = "DELAY_TIME"
        const val EXTRA_MAX_VALUE = "MAX_VALUE"
        const val EXTRA_USE_THREAD = "RUNNABLE"
        const val EXTRA_SERVICE_NAME = "SERVICE_NAME"
        const val EXTRA_SERVICE_STATUS = "SERVICE_STATUS"

        const val SERVICE_PRIMES = "SERVICE_PRIMES"
        const val SERVICE_PRIMES_INTENT = "SERVICE_PRIMES_INTENT"

        const val SERVICE_STATUS_CREATED = "STATUS_CREATED"
        const val SERVICE_STATUS_RUNNING = "STATUS_RUNNING"
        const val SERVICE_STATUS_DESTROYED = "STATUS_DESTROYED"
    }
}