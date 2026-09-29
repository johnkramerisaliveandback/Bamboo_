package com.example.data

data class Alarm(
    val id: String,
    val time: String, // "HH:mm"
    val label: String,
    val isEnabled: Boolean = true,
    val days: List<Int> = emptyList(), // 1=Sun, 2=Mon...
    val soundUri: String? = null,
    val isVibrate: Boolean = true
)

data class ProductivitySession(
    val id: String,
    val type: ProductivityType,
    val startTime: Long,
    val duration: Long, // millis
    val label: String? = null
)

enum class ProductivityType {
    STOPWATCH, TIMER, POMODORO
}

enum class NotificationType {
    CLASS_REMINDER,
    ASSIGNMENT_DUE,
    EXAM_ALERT,
    HOLIDAY_NOTICE,
    ACADEMIC_ALERT,
    PARENT_CONNECTION_REQUEST,
    PARENT_ACCEPTED,
    PARENT_REJECTED
}

data class AcademicNotification(
    val id: String = java.util.UUID.randomUUID().toString(),
    val title: String,
    val body: String,
    val type: NotificationType = NotificationType.ACADEMIC_ALERT,
    val timestamp: Long = System.currentTimeMillis(),
    val isRead: Boolean = false,
    val relatedId: String? = null
)
