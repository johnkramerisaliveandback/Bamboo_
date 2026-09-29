package com.example.notifications

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class NotificationReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val notifId = intent.getIntExtra("EXTRA_NOTIF_ID", 0)
        val title = intent.getStringExtra("EXTRA_TITLE") ?: "Bamboo Academic OS"
        val message = intent.getStringExtra("EXTRA_MESSAGE") ?: ""
        val channelId = intent.getStringExtra("EXTRA_CHANNEL_ID") ?: NotificationHelper.CHANNEL_CLASSES

        NotificationHelper.showSystemNotification(context, notifId, title, message, channelId)
    }
}
