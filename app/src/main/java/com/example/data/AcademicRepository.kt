package com.example.data

import android.content.Context
import android.content.SharedPreferences
import com.example.notifications.NotificationHelper
import org.json.JSONArray
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class AcademicRepository(private val context: Context) {

    private val prefs: SharedPreferences by lazy {
        try {
            val storageContext = context.createDeviceProtectedStorageContext()
            storageContext.moveSharedPreferencesFrom(context, "bamboo_academic_db")
            storageContext.getSharedPreferences("bamboo_academic_db", Context.MODE_PRIVATE)
        } catch (e: Exception) {
            context.getSharedPreferences("bamboo_academic_db", Context.MODE_PRIVATE)
        }
    }

    // ----------------------------------------------------
    // USER PROFILE & ONBOARDING STATE
    // ----------------------------------------------------
    fun getUserProfile(): UserProfile {
        return UserProfile(
            studentName = prefs.getString("student_name", "Alex Bamboo") ?: "Alex Bamboo",
            branch = prefs.getString("student_branch", "CS (AI)") ?: "CS (AI)",
            yearSemester = prefs.getString("year_semester", "Year 1 • Sem 1") ?: "Year 1 • Sem 1",
            hasCompletedOnboarding = prefs.getBoolean("hasCompletedOnboarding", false),
            section = prefs.getString("student_section", null)?.let {
                try { TimetableSection.valueOf(it) } catch (e: Exception) { null }
            }
        )
    }

    fun saveUserProfile(profile: UserProfile) {
        prefs.edit()
            .putString("student_name", profile.studentName)
            .putString("student_branch", profile.branch)
            .putString("year_semester", profile.yearSemester)
            .putBoolean("hasCompletedOnboarding", profile.hasCompletedOnboarding)
            .putString("student_section", profile.section?.name)
            .apply()
    }

    fun setOnboardingCompleted(completed: Boolean) {
        prefs.edit().putBoolean("hasCompletedOnboarding", completed).apply()
    }

    fun resetProfileAndOnboarding() {
        // Clear all persistent settings except default template structures if needed
        prefs.edit()
            .putBoolean("hasCompletedOnboarding", false)
            .putString("student_name", "Alex Bamboo")
            .putString("student_branch", "CS (AI)")
            .putString("year_semester", "Year 1 • Sem 1")
            .putString("app_theme_mode", "DARK")
            .putBoolean("is_migration_completed", false)
            .apply()

        cancelAllScheduledNotifications()
    }

    fun isMigrationCompleted(): Boolean = prefs.getBoolean("is_migration_completed", false)
    fun markMigrationCompleted() = prefs.edit().putBoolean("is_migration_completed", true).apply()

    // ----------------------------------------------------
    // PERMANENT CONNECTION CODE
    // ----------------------------------------------------
    fun getLocalConnectionCode(): String {
        var code = prefs.getString("permanent_connection_code", null)
        if (code == null || code.length != 10) {
            code = (1..10).map { (0..9).random() }.joinToString("")
            prefs.edit().putString("permanent_connection_code", code).apply()
        }
        return code
    }

    fun saveLocalConnectionCode(code: String) {
        prefs.edit().putString("permanent_connection_code", code).apply()
    }

    // ----------------------------------------------------
    // THEME PERSISTENCE
    // ----------------------------------------------------
    fun getThemeMode(): String {
        return prefs.getString("app_theme_mode", "DARK") ?: "DARK"
    }

    fun saveThemeMode(mode: String) {
        prefs.edit().putString("app_theme_mode", mode).apply()
    }

    fun getThemeColor(): String {
        return prefs.getString("app_theme_color", "GREEN") ?: "GREEN"
    }

    fun saveThemeColor(color: String) {
        prefs.edit().putString("app_theme_color", color).apply()
    }

    // ----------------------------------------------------
    // NOTIFICATION SETTINGS
    // ----------------------------------------------------
    fun getNotificationSettings(): NotificationSettings {
        return NotificationSettings(
            classesEnabled = prefs.getBoolean("notif_classes", true),
            examsEnabled = prefs.getBoolean("notif_exams", true),
            assignmentsEnabled = prefs.getBoolean("notif_assignments", true),
            holidaysEnabled = prefs.getBoolean("notif_holidays", true),
            classReminderMinutes = prefs.getInt("notif_class_mins", 15)
        )
    }

    fun saveNotificationSettings(settings: NotificationSettings) {
        prefs.edit()
            .putBoolean("notif_classes", settings.classesEnabled)
            .putBoolean("notif_exams", settings.examsEnabled)
            .putBoolean("notif_assignments", settings.assignmentsEnabled)
            .putBoolean("notif_holidays", settings.holidaysEnabled)
            .putInt("notif_class_mins", settings.classReminderMinutes)
            .apply()

        rescheduleAllNotifications()
    }

    // ----------------------------------------------------
    // ASSIGNMENTS CRUD
    // ----------------------------------------------------
    fun getAssignments(): List<Assignment> {
        val jsonStr = prefs.getString("assignments_json", null)
        if (jsonStr.isNullOrBlankLegacy()) {
            val initialList = getSampleAssignments()
            saveAssignments(initialList)
            return initialList
        }

        val list = mutableListOf<Assignment>()
        try {
            val array = JSONArray(jsonStr)
            val todayStr = SimpleDateFormat("yyyy-MM-dd", Locale.US).format(Date())
            for (i in 0 until array.length()) {
                val obj = array.getJSONObject(i)
                val isDone = obj.optBoolean("isCompleted", false)
                val dueDate = obj.optString("dueDate", todayStr)

                val statusStr = if (isDone) {
                    AssignmentStatus.COMPLETED
                } else if (dueDate < todayStr) {
                    AssignmentStatus.OVERDUE
                } else {
                    AssignmentStatus.PENDING
                }

                val item = Assignment(
                    id = obj.optString("id", "a_$i"),
                    title = obj.optString("title", "Assignment"),
                    subject = obj.optString("subject", "General"),
                    description = obj.optString("description", ""),
                    assignedDate = obj.optString("assignedDate", todayStr),
                    dueDate = dueDate,
                    dueTime = obj.optString("dueTime", "23:59"),
                    priority = try {
                        AssignmentPriority.valueOf(obj.optString("priority", "MEDIUM"))
                    } catch (priorityEx: Exception) {
                        AssignmentPriority.MEDIUM
                    },
                    status = statusStr,
                    reminderTiming = obj.optString("reminderTiming", "1_DAY")
                )
                list.add(item)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return list
    }

    fun saveAssignment(assignment: Assignment) {
        val currentList = getAssignments().toMutableList()
        val index = currentList.indexOfFirst { it.id == assignment.id }

        // Cancel old notification for this assignment
        cancelAssignmentNotification(assignment.id)

        if (index >= 0) {
            currentList[index] = assignment
        } else {
            currentList.add(assignment)
        }

        saveAssignments(currentList)

        // Schedule new notification if active
        if (assignment.status != AssignmentStatus.COMPLETED) {
            scheduleAssignmentNotification(assignment)
        }
    }

    fun deleteAssignment(id: String) {
        val currentList = getAssignments().toMutableList()
        currentList.removeAll { it.id == id }
        cancelAssignmentNotification(id)
        saveAssignments(currentList)
    }

    fun toggleAssignmentCompleted(id: String) {
        val currentList = getAssignments().toMutableList()
        val index = currentList.indexOfFirst { it.id == id }
        if (index >= 0) {
            val old = currentList[index]
            val newStatus = if (old.status == AssignmentStatus.COMPLETED) AssignmentStatus.PENDING else AssignmentStatus.COMPLETED
            val updated = old.copy(status = newStatus)
            currentList[index] = updated
            saveAssignments(currentList)

            if (newStatus == AssignmentStatus.COMPLETED) {
                cancelAssignmentNotification(id)
            } else {
                scheduleAssignmentNotification(updated)
            }
        }
    }

    private fun saveAssignments(list: List<Assignment>) {
        val array = JSONArray()
        for (item in list) {
            val obj = JSONObject()
            obj.put("id", item.id)
            obj.put("title", item.title)
            obj.put("subject", item.subject)
            obj.put("description", item.description)
            obj.put("assignedDate", item.assignedDate)
            obj.put("dueDate", item.dueDate)
            obj.put("dueTime", item.dueTime)
            obj.put("priority", item.priority.name)
            obj.put("status", item.status.name)
            obj.put("isCompleted", item.status == AssignmentStatus.COMPLETED)
            obj.put("reminderTiming", item.reminderTiming)
            array.put(obj)
        }
        prefs.edit().putString("assignments_json", array.toString()).apply()
    }

    private fun getSampleAssignments(): List<Assignment> {
        val cal = Calendar.getInstance()
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.US)
        val todayStr = sdf.format(cal.time)

        cal.add(Calendar.DAY_OF_YEAR, 2)
        val dueSoonStr = sdf.format(cal.time)

        cal.add(Calendar.DAY_OF_YEAR, 5)
        val dueLaterStr = sdf.format(cal.time)

        return listOf(
            Assignment(
                id = "assign_1",
                title = "Quantum Physics Lab Report",
                subject = "JAS101",
                description = "Complete experiment 4 analysis on photoelectric effect and attach error calculations.",
                assignedDate = todayStr,
                dueDate = dueSoonStr,
                dueTime = "23:59",
                priority = AssignmentPriority.HIGH,
                status = AssignmentStatus.PENDING,
                reminderTiming = "1_DAY"
            ),
            Assignment(
                id = "assign_2",
                title = "Matrices & Eigenvalues Problem Set",
                subject = "JAS102",
                description = "Solve exercises 3.1 to 3.4 in textbook Chapter 3.",
                assignedDate = todayStr,
                dueDate = dueLaterStr,
                dueTime = "18:00",
                priority = AssignmentPriority.MEDIUM,
                status = AssignmentStatus.PENDING,
                reminderTiming = "12_HOURS"
            )
        )
    }

    // ----------------------------------------------------
    // EXAMS CRUD
    // ----------------------------------------------------
    fun getExams(): List<Exam> {
        val jsonStr = prefs.getString("exams_json", null)
        if (jsonStr.isNullOrBlankLegacy()) {
            val initialList = getSampleExams()
            saveExams(initialList)
            return initialList
        }

        val list = mutableListOf<Exam>()
        try {
            val array = JSONArray(jsonStr)
            for (i in 0 until array.length()) {
                val obj = array.getJSONObject(i)
                val item = Exam(
                    id = obj.optString("id", "e_$i"),
                    subject = obj.optString("subject", "Subject"),
                    examType = try {
                        ExamType.valueOf(obj.optString("examType", "MID_SEM"))
                    } catch (e: Exception) {
                        ExamType.MID_SEM
                    },
                    date = obj.optString("date", "2026-08-20"),
                    startTime = obj.optString("startTime", "09:30"),
                    endTime = obj.optString("endTime", "12:30"),
                    venue = obj.optString("venue", "LT-1"),
                    notes = obj.optString("notes", ""),
                    isCompleted = obj.optBoolean("isCompleted", false),
                    reminderTiming = obj.optString("reminderTiming", "1_DAY")
                )
                list.add(item)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return list
    }

    fun saveExam(exam: Exam) {
        val currentList = getExams().toMutableList()
        val index = currentList.indexOfFirst { it.id == exam.id }

        cancelExamNotification(exam.id)

        if (index >= 0) {
            currentList[index] = exam
        } else {
            currentList.add(exam)
        }

        saveExams(currentList)

        if (!exam.isCompleted) {
            scheduleExamNotification(exam)
        }
    }

    fun deleteExam(id: String) {
        val currentList = getExams().toMutableList()
        currentList.removeAll { it.id == id }
        cancelExamNotification(id)
        saveExams(currentList)
    }

    fun toggleExamCompleted(id: String) {
        val currentList = getExams().toMutableList()
        val index = currentList.indexOfFirst { it.id == id }
        if (index >= 0) {
            val old = currentList[index]
            val updated = old.copy(isCompleted = !old.isCompleted)
            currentList[index] = updated
            saveExams(currentList)

            if (updated.isCompleted) {
                cancelExamNotification(id)
            } else {
                scheduleExamNotification(updated)
            }
        }
    }

    private fun saveExams(list: List<Exam>) {
        val array = JSONArray()
        for (item in list) {
            val obj = JSONObject()
            obj.put("id", item.id)
            obj.put("subject", item.subject)
            obj.put("examType", item.examType.name)
            obj.put("date", item.date)
            obj.put("startTime", item.startTime)
            obj.put("endTime", item.endTime)
            obj.put("venue", item.venue)
            obj.put("notes", item.notes)
            obj.put("isCompleted", item.isCompleted)
            obj.put("reminderTiming", item.reminderTiming)
            array.put(obj)
        }
        prefs.edit().putString("exams_json", array.toString()).apply()
    }

    private fun getSampleExams(): List<Exam> {
        val cal = Calendar.getInstance()
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.US)

        cal.add(Calendar.DAY_OF_YEAR, 6)
        val exam1Date = sdf.format(cal.time)

        cal.add(Calendar.DAY_OF_YEAR, 4)
        val exam2Date = sdf.format(cal.time)

        return listOf(
            Exam(
                id = "exam_1",
                subject = "JAS101 — Physics",
                examType = ExamType.MID_SEM,
                date = exam1Date,
                startTime = "09:30",
                endTime = "11:30",
                venue = "NLT-201",
                notes = "Syllabus Units 1 & 2 included. Bring scientific calculator.",
                isCompleted = false,
                reminderTiming = "1_DAY"
            ),
            Exam(
                id = "exam_2",
                subject = "JAS102 — Calculus & Linear Algebra",
                examType = ExamType.MID_SEM,
                date = exam2Date,
                startTime = "14:00",
                endTime = "16:00",
                venue = "NLT-104",
                notes = "Focus on Eigenvalues, Fourier Series, and Differential Equations.",
                isCompleted = false,
                reminderTiming = "12_HOURS"
            )
        )
    }

    // Helper extension for String check
    private fun String?.isNullOrBlankLegacy(): Boolean = (this == null) || this.trim().isEmpty()

    // ----------------------------------------------------
    // NOTES CRUD
    // ----------------------------------------------------
    fun getNotes(): List<Note> {
        val jsonStr = prefs.getString("notes_json", null)
        if (jsonStr.isNullOrBlankLegacy()) return emptyList()

        val list = mutableListOf<Note>()
        try {
            val array = JSONArray(jsonStr)
            for (i in 0 until array.length()) {
                val obj = array.getJSONObject(i)
                val tagsArr = obj.optJSONArray("tags")
                val tags = mutableListOf<String>()
                if (tagsArr != null) {
                    for (j in 0 until tagsArr.length()) {
                        tags.add(tagsArr.getString(j))
                    }
                }
                val item = Note(
                    id = obj.optString("id", "n_$i"),
                    title = obj.optString("title", ""),
                    content = obj.optString("content", ""),
                    date = obj.optString("date", ""),
                    tags = tags,
                    attachments = try {
                        val attArr = obj.optJSONArray("attachments")
                        val attList = mutableListOf<com.example.ui.state.Attachment>()
                        if (attArr != null) {
                            for (k in 0 until attArr.length()) {
                                val aObj = attArr.getJSONObject(k)
                                val internalName = aObj.optString("name", "")
                                attList.add(
                                    com.example.ui.state.Attachment(
                                        id = aObj.optString("id", java.util.UUID.randomUUID().toString()),
                                        uri = aObj.getString("uri"),
                                        type = aObj.getString("type"),
                                        name = internalName,
                                        displayName = aObj.optString("displayName", internalName)
                                    )
                                )
                            }
                        }
                        attList
                    } catch (e: Exception) {
                        emptyList()
                    }
                )
                list.add(item)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return list
    }

    fun saveNote(note: Note) {
        val currentList = getNotes().toMutableList()
        val index = currentList.indexOfFirst { it.id == note.id }
        if (index >= 0) {
            currentList[index] = note
        } else {
            currentList.add(note)
        }
        saveNotesInternal(currentList)
    }

    fun deleteNote(id: String) {
        val currentList = getNotes().toMutableList()
        currentList.removeAll { it.id == id }
        saveNotesInternal(currentList)
    }

    private fun saveNotesInternal(list: List<Note>) {
        val array = JSONArray()
        for (item in list) {
            val obj = JSONObject()
            obj.put("id", item.id)
            obj.put("title", item.title)
            obj.put("content", item.content)
            obj.put("date", item.date)
            val tagsArr = JSONArray()
            item.tags.forEach { tagsArr.put(it) }
            obj.put("tags", tagsArr)
            
            val attArr = JSONArray()
            item.attachments.forEach { att ->
                val aObj = JSONObject()
                aObj.put("id", att.id)
                aObj.put("uri", att.uri)
                aObj.put("type", att.type)
                aObj.put("name", att.name)
                aObj.put("displayName", att.displayName)
                attArr.put(aObj)
            }
            obj.put("attachments", attArr)
            array.put(obj)
        }
        prefs.edit().putString("notes_json", array.toString()).apply()
    }

    private fun attachName(att: com.example.ui.state.Attachment): String {
        return att.name
    }

    fun saveFileToInternalStorage(uri: android.net.Uri, fileName: String): String? {
        return try {
            val inputStream = context.contentResolver.openInputStream(uri)
            val file = java.io.File(context.filesDir, fileName)
            val outputStream = java.io.FileOutputStream(file)
            inputStream?.use { input ->
                outputStream.use { output ->
                    input.copyTo(output)
                }
            }
            file.absolutePath
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    fun getFileName(uri: android.net.Uri): String? {
        var result: String? = null
        if (uri.scheme == "content") {
            val cursor = context.contentResolver.query(uri, null, null, null, null)
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    val index = cursor.getColumnIndex(android.provider.OpenableColumns.DISPLAY_NAME)
                    if (index != -1) {
                        result = cursor.getString(index)
                    }
                }
            } finally {
                cursor?.close()
            }
        }
        if (result == null) {
            result = uri.path
            val cut = result?.lastIndexOf('/') ?: -1
            if (cut != -1) {
                result = result?.substring(cut + 1)
            }
        }
        return result
    }

    // ----------------------------------------------------
    // EXPENSES CRUD
    // ----------------------------------------------------
    fun getExpenses(): List<Expense> {
        val jsonStr = prefs.getString("expenses_json", null)
        if (jsonStr.isNullOrBlankLegacy()) return emptyList()

        val list = mutableListOf<Expense>()
        try {
            val array = JSONArray(jsonStr)
            for (i in 0 until array.length()) {
                val obj = array.getJSONObject(i)
                val item = Expense(
                    id = obj.optString("id", "ex_$i"),
                    title = obj.optString("title", ""),
                    amount = obj.optDouble("amount", 0.0),
                    category = obj.optString("category", "Other"),
                    date = obj.optString("date", ""),
                    notes = obj.optString("notes", "")
                )
                list.add(item)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return list
    }

    fun saveExpense(expense: Expense) {
        val currentList = getExpenses().toMutableList()
        val index = currentList.indexOfFirst { it.id == expense.id }
        if (index >= 0) {
            currentList[index] = expense
        } else {
            currentList.add(expense)
        }
        saveExpensesInternal(currentList)
    }

    fun deleteExpense(id: String) {
        val currentList = getExpenses().toMutableList()
        currentList.removeAll { it.id == id }
        saveExpensesInternal(currentList)
    }

    private fun saveExpensesInternal(list: List<Expense>) {
        val array = JSONArray()
        for (item in list) {
            val obj = JSONObject()
            obj.put("id", item.id)
            obj.put("title", item.title)
            obj.put("amount", item.amount)
            obj.put("category", item.category)
            obj.put("date", item.date)
            obj.put("notes", item.notes)
            array.put(obj)
        }
        prefs.edit().putString("expenses_json", array.toString()).apply()
    }

    // ----------------------------------------------------
    // ALARMS CRUD
    // ----------------------------------------------------
    fun getAlarms(): List<Alarm> {
        val jsonStr = prefs.getString("alarms_json", null)
        if (jsonStr.isNullOrBlankLegacy()) return emptyList()

        val list = mutableListOf<Alarm>()
        try {
            val array = JSONArray(jsonStr)
            for (i in 0 until array.length()) {
                val obj = array.getJSONObject(i)
                val daysArr = obj.optJSONArray("days")
                val days = mutableListOf<Int>()
                if (daysArr != null) {
                    for (j in 0 until daysArr.length()) {
                        days.add(daysArr.getInt(j))
                    }
                }
                list.add(Alarm(
                    id = obj.getString("id"),
                    time = obj.getString("time"),
                    label = obj.getString("label"),
                    isEnabled = obj.optBoolean("isEnabled", true),
                    days = days,
                    soundUri = obj.optString("soundUri", null),
                    isVibrate = obj.optBoolean("isVibrate", true)
                ))
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return list
    }

    fun saveAlarm(alarm: Alarm) {
        val current = getAlarms().toMutableList()
        val index = current.indexOfFirst { it.id == alarm.id }
        if (index >= 0) current[index] = alarm else current.add(alarm)
        saveAlarmsInternal(current)
    }

    fun deleteAlarm(id: String) {
        val current = getAlarms().toMutableList()
        current.removeAll { it.id == id }
        saveAlarmsInternal(current)
    }

    private fun saveAlarmsInternal(list: List<Alarm>) {
        val array = JSONArray()
        for (item in list) {
            val obj = JSONObject()
            obj.put("id", item.id)
            obj.put("time", item.time)
            obj.put("label", item.label)
            obj.put("isEnabled", item.isEnabled)
            val daysArr = JSONArray()
            item.days.forEach { daysArr.put(it) }
            obj.put("days", daysArr)
            obj.put("soundUri", item.soundUri)
            obj.put("isVibrate", item.isVibrate)
            array.put(obj)
        }
        prefs.edit().putString("alarms_json", array.toString()).apply()
    }

    // ----------------------------------------------------
    // HOLIDAYS LIST & REMINDERS
    // ----------------------------------------------------
    fun getOfficialHolidays(): List<OfficialHoliday> {
        val jsonStr = prefs.getString("holidays_json", null)
        if (jsonStr.isNullOrBlankLegacy()) {
            val initial = AcademicData.OFFICIAL_HOLIDAYS_2026
            saveHolidaysInternal(initial)
            return initial
        }

        val list = mutableListOf<OfficialHoliday>()
        try {
            val array = JSONArray(jsonStr)
            for (i in 0 until array.length()) {
                val obj = array.getJSONObject(i)
                list.add(OfficialHoliday(
                    id = obj.getString("id"),
                    name = obj.getString("name"),
                    startDate = obj.getString("startDate"),
                    endDate = obj.getString("endDate"),
                    dayOfWeek = obj.getString("dayOfWeek"),
                    numberOfDays = obj.optInt("numberOfDays", 1),
                    notes = obj.optString("notes", ""),
                    category = obj.optString("category", "Gazetted"),
                    reminderEnabled = obj.optBoolean("reminderEnabled", true),
                    reminderTiming = obj.optString("reminderTiming", "1_DAY")
                ))
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return list
    }

    fun saveHoliday(holiday: OfficialHoliday) {
        val current = getOfficialHolidays().toMutableList()
        val index = current.indexOfFirst { it.id == holiday.id }
        if (index >= 0) current[index] = holiday else current.add(holiday)
        saveHolidaysInternal(current)
        rescheduleAllNotifications()
    }

    fun deleteHoliday(id: String) {
        val current = getOfficialHolidays().toMutableList()
        current.removeAll { it.id == id }
        saveHolidaysInternal(current)
        rescheduleAllNotifications()
    }

    private fun saveHolidaysInternal(list: List<OfficialHoliday>) {
        val array = JSONArray()
        for (item in list) {
            val obj = JSONObject()
            obj.put("id", item.id)
            obj.put("name", item.name)
            obj.put("startDate", item.startDate)
            obj.put("endDate", item.endDate)
            obj.put("dayOfWeek", item.dayOfWeek)
            obj.put("numberOfDays", item.numberOfDays)
            obj.put("notes", item.notes)
            obj.put("category", item.category)
            obj.put("reminderEnabled", item.reminderEnabled)
            obj.put("reminderTiming", item.reminderTiming)
            array.put(obj)
        }
        prefs.edit().putString("holidays_json", array.toString()).apply()
    }

    // ----------------------------------------------------
    // NOTIFICATION SCHEDULING & CANCELLATION ENGINE
    // ----------------------------------------------------
    fun rescheduleAllNotifications() {
        val settings = getNotificationSettings()

        cancelAllScheduledNotifications()

        if (settings.assignmentsEnabled) {
            getAssignments().filter { it.status != AssignmentStatus.COMPLETED }.forEach {
                scheduleAssignmentNotification(it)
            }
        }

        if (settings.examsEnabled) {
            getExams().filter { !it.isCompleted }.forEach {
                scheduleExamNotification(it)
            }
        }

        if (settings.holidaysEnabled) {
            getOfficialHolidays().forEach {
                scheduleHolidayNotification(it)
            }
        }
    }

    private fun generateNotificationId(prefix: String, id: String): Int {
        val combined = "${prefix}_$id"
        return combined.hashCode() and 0x7FFFFFFF
    }

    private fun scheduleAssignmentNotification(assignment: Assignment) {
        val settings = getNotificationSettings()
        if (!settings.assignmentsEnabled || assignment.reminderTiming == "NONE") return

        val dueMillis = parseDateTimeMillis(assignment.dueDate, assignment.dueTime) ?: return
        val offsetMillis = getOffsetForTiming(assignment.reminderTiming)
        val triggerMillis = dueMillis - offsetMillis

        val notifId = generateNotificationId("assign", assignment.id)
        val title = "Assignment Due Soon"
        val message = "${assignment.title} (${assignment.subject}) is due on ${assignment.dueDate} at ${assignment.dueTime}."

        NotificationHelper.scheduleNotification(context, notifId, title, message, triggerMillis)
    }

    private fun cancelAssignmentNotification(id: String) {
        val notifId = generateNotificationId("assign", id)
        NotificationHelper.cancelNotification(context, notifId)
    }

    private fun scheduleExamNotification(exam: Exam) {
        val settings = getNotificationSettings()
        if (!settings.examsEnabled || exam.reminderTiming == "NONE") return

        val examMillis = parseDateTimeMillis(exam.date, exam.startTime) ?: return
        val offsetMillis = getOffsetForTiming(exam.reminderTiming)
        val triggerMillis = examMillis - offsetMillis

        val notifId = generateNotificationId("exam", exam.id)
        val title = "Upcoming Exam Alert"
        val venueInfo = if (exam.venue.isNotBlank()) " at ${exam.venue}" else ""
        val message = "${exam.subject} (${exam.examType.label}) is scheduled for ${exam.date} at ${exam.startTime}$venueInfo."

        NotificationHelper.scheduleNotification(context, notifId, title, message, triggerMillis)
    }

    private fun cancelExamNotification(id: String) {
        val notifId = generateNotificationId("exam", id)
        NotificationHelper.cancelNotification(context, notifId)
    }

    private fun scheduleHolidayNotification(holiday: OfficialHoliday) {
        val settings = getNotificationSettings()
        if (!settings.holidaysEnabled || !holiday.reminderEnabled) return

        // 1. Holiday Tomorrow Notification (Day before at 8:00 PM)
        val dayBeforeMillis = parseDateTimeMillis(holiday.startDate, "20:00")?.let { it - 24 * 3600 * 1000L }
        if (dayBeforeMillis != null && dayBeforeMillis > System.currentTimeMillis()) {
            val notifIdBefore = generateNotificationId("holiday_pre", holiday.id)
            NotificationHelper.scheduleNotification(
                context,
                notifIdBefore,
                "🎉 Holiday Tomorrow",
                "${holiday.name} is tomorrow. Enjoy your break!",
                dayBeforeMillis
            )
        }

        // 2. Happy Holiday Notification (Day of at 8:00 AM)
        val dayOfMillis = parseDateTimeMillis(holiday.startDate, "08:00")
        if (dayOfMillis != null && dayOfMillis > System.currentTimeMillis()) {
            val notifIdOf = generateNotificationId("holiday_day", holiday.id)
            val festiveIcon = when (holiday.category.lowercase()) {
                "festival" -> "🌸"
                "national" -> "🇮🇳"
                "religious" -> "🕌"
                else -> "🎉"
            }
            NotificationHelper.scheduleNotification(
                context,
                notifIdOf,
                "$festiveIcon Happy ${holiday.name}!",
                "No classes today. Enjoy your holiday.",
                dayOfMillis
            )
        }
    }

    private fun cancelAllScheduledNotifications() {
        // Cancel all sample IDs
        getAssignments().forEach { cancelAssignmentNotification(it.id) }
        getExams().forEach { cancelExamNotification(it.id) }
        getOfficialHolidays().forEach {
            val notifId = generateNotificationId("holiday", it.id)
            NotificationHelper.cancelNotification(context, notifId)
        }
    }

    private fun parseDateTimeMillis(dateStr: String, timeStr: String): Long? {
        return try {
            val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.US)
            val date = sdf.parse("$dateStr $timeStr")
            date?.time
        } catch (e: Exception) {
            null
        }
    }

    private fun getOffsetForTiming(timing: String): Long {
        return when (timing) {
            "1_DAY" -> 24 * 3600 * 1000L
            "12_HOURS" -> 12 * 3600 * 1000L
            "2_HOURS" -> 2 * 3600 * 1000L
            "1_HOUR" -> 1 * 3600 * 1000L
            "30_MINS" -> 30 * 60 * 1000L
            "1_WEEK" -> 7 * 24 * 3600 * 1000L
            "3_DAYS" -> 3 * 24 * 3600 * 1000L
            else -> 24 * 3600 * 1000L
        }
    }
}
