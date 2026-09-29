package com.example.notifications

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val alarmId = intent.getStringExtra("ALARM_ID") ?: return
        val label = intent.getStringExtra("ALARM_LABEL") ?: "Alarm"
        val soundUrl = intent.getStringExtra("ALARM_SOUND_URL")
        
        Log.d("AlarmReceiver", "Alarm fired: $alarmId - $label")
        
        val fullScreenIntent = Intent(context, AlarmActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            putExtra("ALARM_ID", alarmId)
            putExtra("ALARM_LABEL", label)
            putExtra("ALARM_SOUND_URL", soundUrl)
        }
        
        NotificationHelper.showSystemNotification(
            context,
            alarmId.hashCode(),
            "⏰ Alarm: $label",
            "Tap to dismiss",
            NotificationHelper.CHANNEL_ACADEMIC,
            fullScreenIntent
        )
    }
}
