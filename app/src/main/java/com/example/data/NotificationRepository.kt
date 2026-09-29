package com.example.data

import android.content.Context
import android.content.SharedPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.json.JSONArray
import org.json.JSONObject
import java.util.UUID

interface NotificationRepository {
    fun getNotifications(): Flow<List<AcademicNotification>>
    fun markAsRead(notificationId: String)
    fun markAllAsRead()
    fun deleteNotification(notificationId: String)
    fun sendNotification(notification: AcademicNotification)
}

class LocalNotificationRepository(context: Context) : NotificationRepository {
    private val prefs: SharedPreferences = context.getSharedPreferences("bamboo_notifications_db", Context.MODE_PRIVATE)
    private val _notificationsFlow = MutableStateFlow<List<AcademicNotification>>(emptyList())

    init {
        loadNotifications()
    }

    private fun loadNotifications() {
        val jsonStr = prefs.getString("notifications_list", null) ?: "[]"
        val list = mutableListOf<AcademicNotification>()
        try {
            val array = JSONArray(jsonStr)
            for (i in 0 until array.length()) {
                val obj = array.getJSONObject(i)
                val relId = if (obj.has("relatedId") && !obj.isNull("relatedId")) obj.getString("relatedId") else null
                list.add(
                    AcademicNotification(
                        id = obj.optString("id", UUID.randomUUID().toString()),
                        title = obj.optString("title", ""),
                        body = obj.optString("body", ""),
                        type = try {
                            NotificationType.valueOf(obj.optString("type", NotificationType.ACADEMIC_ALERT.name))
                        } catch (e: Exception) {
                            NotificationType.ACADEMIC_ALERT
                        },
                        timestamp = obj.optLong("timestamp", System.currentTimeMillis()),
                        isRead = obj.optBoolean("isRead", false),
                        relatedId = relId
                    )
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        _notificationsFlow.value = list.sortedByDescending { it.timestamp }
    }

    private fun saveNotifications(list: List<AcademicNotification>) {
        val array = JSONArray()
        for (item in list) {
            val obj = JSONObject()
            obj.put("id", item.id)
            obj.put("title", item.title)
            obj.put("body", item.body)
            obj.put("type", item.type.name)
            obj.put("timestamp", item.timestamp)
            obj.put("isRead", item.isRead)
            obj.put("relatedId", item.relatedId ?: "")
            array.put(obj)
        }
        prefs.edit().putString("notifications_list", array.toString()).apply()
        _notificationsFlow.value = list.sortedByDescending { it.timestamp }
    }

    override fun getNotifications(): Flow<List<AcademicNotification>> = _notificationsFlow.asStateFlow()

    override fun markAsRead(notificationId: String) {
        val current = _notificationsFlow.value.toMutableList()
        val index = current.indexOfFirst { it.id == notificationId }
        if (index >= 0) {
            current[index] = current[index].copy(isRead = true)
            saveNotifications(current)
        }
    }

    override fun markAllAsRead() {
        val updated = _notificationsFlow.value.map { it.copy(isRead = true) }
        saveNotifications(updated)
    }

    override fun deleteNotification(notificationId: String) {
        val current = _notificationsFlow.value.filterNot { it.id == notificationId }
        saveNotifications(current)
    }

    override fun sendNotification(notification: AcademicNotification) {
        val current = _notificationsFlow.value.toMutableList()
        val newItem = if (notification.id.isBlank()) notification.copy(id = UUID.randomUUID().toString()) else notification
        current.add(0, newItem)
        saveNotifications(current)
    }
}
