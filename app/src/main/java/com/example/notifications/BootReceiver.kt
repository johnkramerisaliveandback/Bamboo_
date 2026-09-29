package com.example.notifications

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

class BootReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val action = intent.action
        if (action == Intent.ACTION_BOOT_COMPLETED || 
            action == Intent.ACTION_LOCKED_BOOT_COMPLETED ||
            action == "android.intent.action.QUICKBOOT_POWERON") {
            
            Log.d("BootReceiver", "Device rebooted, rescheduling all notifications")
            
            // Recalculate all future notifications from the repository
            val repo = com.example.data.AcademicRepository(context)
            repo.rescheduleAllNotifications()
        }
    }
}
