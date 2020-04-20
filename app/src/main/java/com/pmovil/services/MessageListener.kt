package com.pmovil.services

import android.content.Intent

interface MessageListener {
    fun onMessageReceived(intent: Intent)
}