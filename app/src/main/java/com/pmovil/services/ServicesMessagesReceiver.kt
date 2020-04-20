package com.pmovil.services

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class ServicesMessagesReceiver(private val listener: MessageListener) : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        intent?.let {
            listener.onMessageReceived(intent)
        }
    }
}