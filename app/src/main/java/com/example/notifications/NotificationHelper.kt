package com.example.notifications

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.MainActivity
import com.example.R
import java.util.Calendar

object NotificationHelper {

    const val CHANNEL_CLASSES = "bamboo_classes"
    const val CHANNEL_ASSIGNMENTS = "bamboo_assignments"
    const val CHANNEL_ACADEMIC = "bamboo_academic"

    fun createNotificationChannels(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            
            val channels = listOf(
                NotificationChannel(
                    CHANNEL_CLASSES,
                    "Class Reminders",
                    NotificationManager.IMPORTANCE_HIGH
                ).apply {
                    description = "Upcoming class alerts"
                    enableVibration(true)
                },
                NotificationChannel(
                    CHANNEL_ASSIGNMENTS,
                    "Assignments",
                    NotificationManager.IMPORTANCE_DEFAULT
                ).apply {
                    description = "Due date reminders"
                },
                NotificationChannel(
                    CHANNEL_ACADEMIC,
                    "Academic Progress",
                    NotificationManager.IMPORTANCE_DEFAULT
                ).apply {
                    description = "Streaks and achievement celebrations"
                }
            )
            manager.createNotificationChannels(channels)
        }
    }

    fun scheduleNotification(
        context: Context,
        notificationId: Int,
        title: String,
        message: String,
        triggerTimeMillis: Long,
        channelId: String = CHANNEL_CLASSES
    ) {
        if (triggerTimeMillis <= System.currentTimeMillis()) return

        val intent = Intent(context, NotificationReceiver::class.java).apply {
            putExtra("EXTRA_NOTIF_ID", notificationId)
            putExtra("EXTRA_TITLE", title)
            putExtra("EXTRA_MESSAGE", message)
            putExtra("EXTRA_CHANNEL_ID", channelId)
        }

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            notificationId,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                if (alarmManager.canScheduleExactAlarms()) {
                    alarmManager.setExactAndAllowWhileIdle(
                        AlarmManager.RTC_WAKEUP,
                        triggerTimeMillis,
                        pendingIntent
                    )
                } else {
                    alarmManager.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, triggerTimeMillis, pendingIntent)
                }
            } else {
                alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, triggerTimeMillis, pendingIntent)
            }
            Log.d("NotificationHelper", "Scheduled alarm $notificationId at $triggerTimeMillis")
        } catch (e: Exception) {
            Log.e("NotificationHelper", "Alarm scheduling failed: ${e.message}")
        }
    }

    fun showSystemNotification(
        context: Context, 
        notificationId: Int, 
        title: String, 
        message: String,
        channelId: String = CHANNEL_CLASSES,
        fullScreenIntent: Intent? = null
    ) {
        val permission = android.Manifest.permission.POST_NOTIFICATIONS
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
            androidx.core.content.ContextCompat.checkSelfPermission(context, permission) != 
            android.content.pm.PackageManager.PERMISSION_GRANTED) return

        createNotificationChannels(context)

        // Default content intent to open app
        val contentIntent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        
        val pendingContentIntent = PendingIntent.getActivity(
            context,
            notificationId + 1000,
            contentIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val builder = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.bamboo_icon_fg)
            .setContentTitle(title)
            .setContentText(message)
            .setStyle(NotificationCompat.BigTextStyle().bigText(message))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setCategory(NotificationCompat.CATEGORY_ALARM)
            .setContentIntent(pendingContentIntent)
            .setAutoCancel(true)

        if (fullScreenIntent != null) {
            val pendingFullScreenIntent = PendingIntent.getActivity(
                context,
                notificationId + 2000,
                fullScreenIntent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
            builder.setFullScreenIntent(pendingFullScreenIntent, true)
        }

        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.notify(notificationId, builder.build())
    }

    fun cancelNotification(context: Context, notificationId: Int) {
        val intent = Intent(context, NotificationReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            notificationId,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        (context.getSystemService(Context.ALARM_SERVICE) as AlarmManager).cancel(pendingIntent)
    }
}
