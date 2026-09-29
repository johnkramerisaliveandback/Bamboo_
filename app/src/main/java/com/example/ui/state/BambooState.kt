package com.example.ui.state

import com.example.data.Assignment
import com.example.data.Alarm
import com.example.data.Exam
import com.example.data.NotificationSettings
import com.example.data.OfficialHoliday
import com.example.data.OverallAttendanceStats
import com.example.data.SubjectAttendance
import com.example.data.TimetableEntry
import androidx.compose.runtime.staticCompositionLocalOf

val LocalBambooUiState = staticCompositionLocalOf { BambooUiState() }

enum class NavDestination(val title: String, val testTag: String, val isPrimary: Boolean = true) {
    HOME("Home", "nav_home"),
    SCHEDULE("Schedule", "nav_schedule"),
    ATTENDANCE("Attendance", "nav_attendance"),
    ACADEMICS("Academics", "nav_academics"),
    MORE("More", "nav_more"),
    NOTIFICATIONS("Notifications", "nav_notifications", false),
    STUDENT_SETUP("Student Setup", "nav_student_setup", false),
    APP_GUIDE("App Guide", "nav_app_guide", false),
    ABOUT("About", "nav_about", false)
}

enum class AcademicsTab(val title: String) {
    OVERVIEW("Hub"),
    TIMETABLE("Timetable"),
    ATTENDANCE("Attendance"),
    ASSIGNMENTS("Assignments"),
    EXAMS("Exams"),
    NOTES("Notes"),
    EXPENSES("Expenses"),
    HOLIDAYS("Holidays"),
    SYLLABUS("Syllabus"),
    TOOLS("Tools")
}

enum class AppThemeMode {
    DARK, LIGHT, SYSTEM
}

enum class BambooThemeColor {
    GREEN, PURPLE, BLUE, CYAN, RED, ORANGE, PINK
}

enum class GradientIntensity(val label: String, val alphaMultiplier: Float) {
    SUBTLE("Subtle", 0.12f),
    MEDIUM("Medium", 0.25f),
    STRONG("Strong", 0.40f)
}

data class Attachment(
    val id: String = java.util.UUID.randomUUID().toString(),
    val uri: String,
    val type: String, // "image", "pdf"
    val name: String, // Internal filename or original name
    val displayName: String // Display name shown to user
)

data class ClassSession(
    val id: String,
    val time: String,
    val subject: String,
    val courseCode: String,
    val room: String,
    val teacher: String,
    val status: SessionStatus,
    val tags: List<String> = emptyList(),
    val durationText: String = ""
)

enum class SessionStatus {
    COMPLETED, CURRENT, UPCOMING
}

data class CalendarDay(
    val dayNumber: Int,
    val fullDateString: String, // "YYYY-MM-DD"
    val status: DayStatus,
    val isHoliday: Boolean = false,
    val holidayTitle: String = ""
)

enum class DayStatus {
    PRESENT, ABSENT, CANCELLED, HOLIDAY, NONE
}

data class SyllabusItem(
    val title: String,
    val code: String,
    val totalModules: Int,
    val completedModules: Int,
    val progress: Float
)

data class BambooUiState(
    val isInitializing: Boolean = true,
    val isWelcomeCompleted: Boolean = false,
    val guideCompleted: Boolean = false,
    val currentDestination: NavDestination = NavDestination.HOME,
    val backStack: List<NavDestination> = listOf(NavDestination.HOME),
    val studentName: String = "",
    val branch: String = "CS (AI)",
    val studentYear: String = "1st Year",
    val studentSection: com.example.data.TimetableSection? = com.example.data.TimetableSection.SECTION_1,
    val yearSemester: String = "Year 1 • Sem 1",
    val selectedScheduleDay: String = "MONDAY",
    val selectedScheduleBranch: String = "CS (AI)",
    val selectedAttendanceYear: Int = 2026,
    val selectedAttendanceMonth: Int = 8,
    val selectedAttendanceDate: String = "", // "YYYY-MM-DD"
    val selectedAcademicsTab: AcademicsTab = AcademicsTab.OVERVIEW,
    val syllabusSearchQuery: String = "",
    val themeMode: AppThemeMode = AppThemeMode.DARK,
    val themeColor: BambooThemeColor = BambooThemeColor.GREEN,
    val gradientIntensity: GradientIntensity = GradientIntensity.SUBTLE,
    val hapticsEnabled: Boolean = true,
    val isNotificationPermissionGranted: Boolean = true,
    val isBatteryOptimizationEnabled: Boolean = false,
    val userSavedSuccessToast: Boolean = false,
    val toastMessage: String? = null,
    val selectedSessionForDetail: ClassSession? = null,
    val comingSoonFeatureName: String? = null,
    
    // Academic Hub Data
    val assignments: List<Assignment> = emptyList(),
    val exams: List<Exam> = emptyList(),
    val notes: List<com.example.data.Note> = emptyList(),
    val expenses: List<com.example.data.Expense> = emptyList(),
    val officialHolidays: List<OfficialHoliday> = emptyList(),
    val todayHoliday: OfficialHoliday? = null,
    val upcomingHoliday: OfficialHoliday? = null,
    val holidayProgress: HolidayProgress = HolidayProgress(),
    val holidaySearchQuery: String = "",
    val holidayCategoryFilter: String = "All",
    val hiddenHolidayIds: Set<String> = emptySet(),
    val notificationSettings: NotificationSettings = NotificationSettings(),

    // Intelligent Dashboard Highlights
    val nextUrgentAssignment: Assignment? = null,
    val nextUpcomingExam: Exam? = null,
    val nextOfficialHoliday: OfficialHoliday? = null,
    
    // Smart Dashboard Features
    val isAllClassesDone: Boolean = false,
    val isDayCompleteAchievement: Boolean = false,
    val congratsMessage: String? = null,
    val currentQuote: String = "Discipline is choosing between what you want now and what you want most.",
    val streakCount: Int = 0,
    
    // Calculated live data
    val customTimetable: List<TimetableEntry> = emptyList(),
    val todayEntries: List<TimetableEntry> = emptyList(),
    val currentEntry: TimetableEntry? = null,
    val nextEntry: TimetableEntry? = null,
    val nextEntryMinutesLeft: Int? = null,
    val overallAttendanceStats: OverallAttendanceStats = OverallAttendanceStats(0, 0, 0, 0f, "NO DATA"),
    val subjectAttendanceList: List<SubjectAttendance> = emptyList(),
    val selectedDateAttendanceMap: Map<String, DayStatus> = emptyMap(),

    // Tools / Productivity
    val stopwatchTime: Long = 0L,
    val isStopwatchRunning: Boolean = false,
    val stopwatchLaps: List<Long> = emptyList(),
    
    val timerRemainingTime: Long = 0L,
    val isTimerRunning: Boolean = false,
    val timerInitialDuration: Long = 0L,
    
    val alarms: List<Alarm> = emptyList(),

    // Notification Center
    val notifications: List<com.example.data.AcademicNotification> = emptyList(),
    val unreadNotificationsCount: Int = 0
)

data class HolidayProgress(
    val total: Int = 0,
    val completed: Int = 0,
    val upcoming: Int = 0,
    val percentage: Float = 0f
)
