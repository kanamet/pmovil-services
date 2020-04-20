package com.pmovil.services

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        Toast.makeText(context, "Broadcast received", Toast.LENGTH_SHORT).show()
        val delayTime = intent?.getIntExtra(AppConstants.EXTRA_DELAY_TIME, 0)
        AppUtils.showNotification(
            context,
            "Alarm Notification",
            "Alarm was delayed $delayTime seconds",
            100
        )
    }
}