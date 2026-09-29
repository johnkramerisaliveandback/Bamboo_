package com.example.notifications

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import androidx.core.app.NotificationCompat
import com.example.MainActivity
import com.example.R
import java.util.concurrent.TimeUnit

class ProductivityService : Service() {

    private val handler = Handler(Looper.getMainLooper())
    private var startTime = 0L
    private var isTimer = false
    private var initialDuration = 0L
    private var isRunning = false

    private val updateRunnable = object : Runnable {
        override fun run() {
            if (!isRunning) return
            updateNotification()
            handler.postDelayed(this, 1000)
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val action = intent?.action
        when (action) {
            ACTION_START -> {
                startTime = intent.getLongExtra(EXTRA_START_TIME, System.currentTimeMillis())
                isTimer = intent.getBooleanExtra(EXTRA_IS_TIMER, false)
                initialDuration = intent.getLongExtra(EXTRA_DURATION, 0L)
                isRunning = true
                
                createChannel()
                startForeground(NOTIFICATION_ID, buildNotification())
                handler.post(updateRunnable)
            }
            ACTION_STOP -> {
                isRunning = false
                handler.removeCallbacks(updateRunnable)
                stopForeground(Service.STOP_FOREGROUND_REMOVE)
                stopSelf()
            }
        }
        return START_STICKY
    }

    private fun updateNotification() {
        val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.notify(NOTIFICATION_ID, buildNotification())
    }

    private fun buildNotification(): Notification {
        val elapsed = System.currentTimeMillis() - startTime
        val displayTime = if (isTimer) {
            (initialDuration - elapsed).coerceAtLeast(0)
        } else {
            elapsed
        }

        val timeStr = formatTime(displayTime)
        val title = if (isTimer) "Timer Running" else "Stopwatch Running"

        val intent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)

        val stopIntent = Intent(this, ProductivityService::class.java).apply {
            action = ACTION_STOP
        }
        val stopPendingIntent = PendingIntent.getService(this, 1, stopIntent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)

        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.bamboo_icon_fg)
            .setContentTitle(title)
            .setContentText(timeStr)
            .setOnlyAlertOnce(true)
            .setOngoing(true)
            .setContentIntent(pendingIntent)
            .addAction(R.drawable.bamboo_icon_fg, "STOP", stopPendingIntent)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .build()
    }

    private fun createChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(CHANNEL_ID, "Productivity", NotificationManager.IMPORTANCE_LOW)
            val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(channel)
        }
    }

    private fun formatTime(millis: Long): String {
        val h = TimeUnit.MILLISECONDS.toHours(millis)
        val m = TimeUnit.MILLISECONDS.toMinutes(millis) % 60
        val s = TimeUnit.MILLISECONDS.toSeconds(millis) % 60
        return String.format(java.util.Locale.US, "%02d:%02d:%02d", h, m, s)
    }

    override fun onBind(intent: Intent?): IBinder? = null

    companion object {
        const val NOTIFICATION_ID = 101
        const val CHANNEL_ID = "productivity_channel"
        const val ACTION_START = "start"
        const val ACTION_STOP = "stop"
        const val EXTRA_START_TIME = "start_time"
        const val EXTRA_IS_TIMER = "is_timer"
        const val EXTRA_DURATION = "duration"
    }
}
