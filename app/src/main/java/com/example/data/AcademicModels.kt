package com.example.data

import com.example.ui.state.DayStatus

enum class TimetableSection {
    COMMON, SECTION_1, SECTION_2
}

enum class AcademicStatus(val label: String) {
    IN_CLASS("In Class"),
    BETWEEN_CLASSES("Between Classes"),
    FREE("Free"),
    DAY_COMPLETE("Day Complete"),
    NO_CLASSES_TODAY("No Classes Today"),
    CLASS_MISSED("Class Missed"),
    SELF_STUDY("Self Study"),
    COLLEGE_EVENT("College Event"),
    COMMUTING("Commuting"),
    OTHER("Other")
}

enum class StatusSource {
    AUTO, MANUAL
}

data class ManualStatus(
    val type: AcademicStatus,
    val activity: String? = null,
    val expiresAt: Long? = null
)

data class ParentAcademicStatus(
    val status: AcademicStatus,
    val source: StatusSource = StatusSource.AUTO,
    val currentSubject: String? = null,
    val nextSubject: String? = null,
    val nextInMinutes: Int? = null,
    val nextTime: String? = null,
    val room: String? = null,
    val timeRange: String? = null,
    val activity: String? = null,
    val expiresAt: Long? = null
)

data class StatusHistoryEntry(
    val id: String = java.util.UUID.randomUUID().toString(),
    val status: AcademicStatus,
    val label: String,
    val timestamp: Long = System.currentTimeMillis()
)

data class SubjectAttendance(
    val id: String = java.util.UUID.randomUUID().toString(),
    val subjectCode: String,
    val subjectName: String,
    val attendedClasses: Int = 0,
    val totalClasses: Int = 0
) {
    val percentage: Float
        get() = if (totalClasses > 0) (attendedClasses.toFloat() / totalClasses.toFloat()) * 100f else 0f
}

data class AttendanceRecord(
    val date: String, // "YYYY-MM-DD"
    val courseCode: String, // "JAS101"
    val status: DayStatus, // PRESENT or ABSENT
    val id: String = "${date}_${courseCode.replace("/", "-")}"
)

enum class AssignmentPriority(val label: String) {
    LOW("Low"),
    MEDIUM("Medium"),
    HIGH("High")
}

enum class AssignmentStatus(val label: String) {
    PENDING("Pending"),
    COMPLETED("Completed"),
    OVERDUE("Overdue")
}

data class Assignment(
    val id: String,
    val title: String,
    val subject: String,
    val description: String = "",
    val assignedDate: String = "", // "YYYY-MM-DD"
    val dueDate: String,           // "YYYY-MM-DD"
    val dueTime: String = "23:59",  // "HH:mm"
    val priority: AssignmentPriority = AssignmentPriority.MEDIUM,
    val status: AssignmentStatus = AssignmentStatus.PENDING,
    val reminderTiming: String = "1_DAY" // "NONE", "1_DAY", "12_HOURS", "2_HOURS", "1_HOUR"
)

enum class ExamType(val label: String) {
    MID_SEM("Mid-Sem"),
    END_SEM("End-Sem"),
    PRACTICAL("Practical"),
    QUIZ("Quiz"),
    OTHER("Other")
}

data class Exam(
    val id: String,
    val subject: String,
    val examType: ExamType = ExamType.MID_SEM,
    val date: String,             // "YYYY-MM-DD"
    val startTime: String,        // "09:00"
    val endTime: String,          // "12:00"
    val venue: String = "",
    val notes: String = "",
    val isCompleted: Boolean = false,
    val reminderTiming: String = "1_DAY"
)

data class OfficialHoliday(
    val id: String,
    val name: String,
    val startDate: String, // "YYYY-MM-DD"
    val endDate: String,   // "YYYY-MM-DD"
    val dayOfWeek: String, // "Wednesday", "Tue & Wed", etc.
    val numberOfDays: Int = 1,
    val notes: String = "",
    val category: String = "Gazetted",
    val reminderEnabled: Boolean = true,
    val reminderTiming: String = "1_DAY"
)

data class NotificationSettings(
    val classesEnabled: Boolean = true,
    val examsEnabled: Boolean = true,
    val assignmentsEnabled: Boolean = true,
    val holidaysEnabled: Boolean = true,
    val classReminderMinutes: Int = 15
)

data class UserProfile(
    val studentName: String = "",
    val branch: String = "CS (AI)",
    val year: String = "1st Year",
    val yearSemester: String = "Year 1 • Sem 1",
    val section: TimetableSection? = TimetableSection.SECTION_1,
    val hasCompletedOnboarding: Boolean = false
)

data class Expense(
    val id: String,
    val title: String,
    val amount: Double,
    val category: String, // "Food", "Travel", "Books", "Other"
    val date: String,     // "YYYY-MM-DD"
    val notes: String = ""
)

data class Note(
    val id: String,
    val title: String,
    val content: String,
    val date: String,     // "YYYY-MM-DD"
    val subjectCode: String? = null,
    val tags: List<String> = emptyList(),
    val attachments: List<com.example.ui.state.Attachment> = emptyList()
)
