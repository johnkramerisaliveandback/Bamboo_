package com.example.ui.state

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.*
import com.example.notifications.AlarmHelper
import com.example.notifications.NotificationHelper
import com.example.ui.screens.academics.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val attendanceRepo by lazy { AttendanceRepository(application) }
    private val academicRepo by lazy { AcademicRepository(application) }
    private val notificationRepo by lazy { LocalNotificationRepository(application) }

    private var stopwatchJob: Job? = null
    private var timerJob: Job? = null

    private val _uiState = MutableStateFlow(BambooUiState())
    val uiState: StateFlow<BambooUiState> = _uiState.asStateFlow()

    private val motivationalQuotes = listOf(
        "Discipline is choosing between what you want now and what you want most.",
        "Your future self will thank you for the work you do today.",
        "Consistency is the secret code to extraordinary success.",
        "Small daily improvements over time lead to stunning results."
    )

    init {
        loadStoredProfileAndData()
        startNotificationListener()
    }

    private fun loadStoredProfileAndData() {
        val profile = academicRepo.getUserProfile()
        val todayStr = SimpleDateFormat("yyyy-MM-dd", Locale.US).format(Date())
        val cal = Calendar.getInstance()
        val todayYear = cal.get(Calendar.YEAR)
        val todayMonth = cal.get(Calendar.MONTH) + 1
        val todayDayName = getTodayDayName()

        val themeModeStr = academicRepo.getThemeMode()
        val themeMode = try { AppThemeMode.valueOf(themeModeStr) } catch (e: Exception) { AppThemeMode.DARK }

        val themeColorStr = academicRepo.getThemeColor()
        val themeColor = try { BambooThemeColor.valueOf(themeColorStr) } catch (e: Exception) { BambooThemeColor.GREEN }

        val isCompleted = profile.hasCompletedOnboarding && profile.studentName.isNotBlank()
        val initialDestination = if (isCompleted) NavDestination.HOME else NavDestination.STUDENT_SETUP

        _uiState.update {
            it.copy(
                isInitializing = false,
                isWelcomeCompleted = isCompleted,
                currentDestination = initialDestination,
                backStack = listOf(initialDestination),
                studentName = profile.studentName,
                branch = profile.branch,
                studentYear = profile.year,
                studentSection = profile.section ?: TimetableSection.SECTION_1,
                yearSemester = profile.yearSemester,
                selectedScheduleDay = todayDayName,
                selectedScheduleBranch = profile.branch,
                selectedAttendanceDate = todayStr,
                selectedAttendanceYear = todayYear,
                selectedAttendanceMonth = todayMonth,
                themeMode = themeMode,
                themeColor = themeColor,
                currentQuote = motivationalQuotes.random()
            )
        }

        refreshAllData()
    }

    private fun getTodayDayName(): String {
        val calendar = Calendar.getInstance()
        return when (calendar.get(Calendar.DAY_OF_WEEK)) {
            Calendar.MONDAY -> "MONDAY"
            Calendar.TUESDAY -> "TUESDAY"
            Calendar.WEDNESDAY -> "WEDNESDAY"
            Calendar.THURSDAY -> "THURSDAY"
            Calendar.FRIDAY -> "FRIDAY"
            Calendar.SATURDAY -> "SATURDAY"
            else -> "MONDAY" // Default to Monday for Sunday
        }
    }

    private fun startNotificationListener() {
        viewModelScope.launch {
            notificationRepo.getNotifications().collect { notifs ->
                val unreadCount = notifs.count { !it.isRead }
                _uiState.update {
                    it.copy(
                        notifications = notifs,
                        unreadNotificationsCount = unreadCount
                    )
                }
            }
        }
    }

    fun completeWelcome(name: String, branch: String, year: String, section: TimetableSection) {
        val yearSem = when (year) {
            "1st Year" -> "Year 1 • Sem 1"
            "2nd Year" -> "Year 2 • Sem 3"
            "3rd Year" -> "Year 3 • Sem 5"
            "4th Year" -> "Year 4 • Sem 7"
            else -> "Year 1 • Sem 1"
        }

        val profile = UserProfile(
            studentName = name,
            branch = branch,
            year = year,
            yearSemester = yearSem,
            section = section,
            hasCompletedOnboarding = true
        )
        academicRepo.saveUserProfile(profile)

        _uiState.update {
            it.copy(
                isWelcomeCompleted = true,
                currentDestination = NavDestination.HOME,
                backStack = listOf(NavDestination.HOME),
                studentName = name,
                branch = branch,
                studentYear = year,
                studentSection = section,
                yearSemester = yearSem,
                selectedScheduleBranch = branch
            )
        }

        refreshAllData()
    }

    fun updateUserProfile(name: String, branch: String, year: String, section: TimetableSection) {
        val yearSem = when (year) {
            "1st Year" -> "Year 1 • Sem 1"
            "2nd Year" -> "Year 2 • Sem 3"
            "3rd Year" -> "Year 3 • Sem 5"
            "4th Year" -> "Year 4 • Sem 7"
            else -> "Year 1 • Sem 1"
        }

        val profile = UserProfile(
            studentName = name,
            branch = branch,
            year = year,
            yearSemester = yearSem,
            section = section,
            hasCompletedOnboarding = true
        )
        academicRepo.saveUserProfile(profile)

        _uiState.update {
            it.copy(
                studentName = name,
                branch = branch,
                studentYear = year,
                studentSection = section,
                yearSemester = yearSem,
                selectedScheduleBranch = branch,
                userSavedSuccessToast = true,
                toastMessage = "Profile updated successfully!"
            )
        }

        refreshAllData()
    }

    fun resetProfileAndOnboarding() {
        academicRepo.resetProfileAndOnboarding()
        attendanceRepo.resetAllAttendance()

        _uiState.update {
            it.copy(
                isWelcomeCompleted = false,
                currentDestination = NavDestination.STUDENT_SETUP,
                backStack = listOf(NavDestination.STUDENT_SETUP),
                studentName = "",
                branch = "CS (AI)",
                studentYear = "1st Year",
                studentSection = TimetableSection.SECTION_1
            )
        }
    }

    fun refreshAllData() {
        val state = _uiState.value
        val branch = state.branch
        val section = state.studentSection
        val todayStr = SimpleDateFormat("yyyy-MM-dd", Locale.US).format(Date())
        val selectedDate = state.selectedAttendanceDate.ifBlank { todayStr }

        // 1. Attendance & Subjects
        val subjectList = attendanceRepo.getSubjectAttendanceList(branch, section)
        val overallStats = attendanceRepo.calculateOverallStats(subjectList)
        val streak = attendanceRepo.calculateCurrentStreak()

        // 2. Timetable Entries & Live Schedule Calculation
        val dayEntries = AcademicData.getFilteredTimetable(
            AcademicData.TIMETABLE[branch]?.get(state.selectedScheduleDay) ?: emptyList(),
            section
        )

        val todayEntries = AcademicData.getFilteredTimetable(
            AcademicData.TIMETABLE[branch]?.get(getTodayDayName()) ?: emptyList(),
            section
        )

        val cal = Calendar.getInstance()
        val curMins = cal.get(Calendar.HOUR_OF_DAY) * 60 + cal.get(Calendar.MINUTE)

        var currentClass: TimetableEntry? = null
        var nextClass: TimetableEntry? = null
        var minsLeft: Int? = null
        var lastClassEndMins = 0

        for (entry in todayEntries) {
            val period = AcademicData.PERIODS.find { it.periodName == entry.period } ?: continue
            val startMins = period.startHour * 60 + period.startMinute
            val endMins = period.endHour * 60 + period.endMinute

            if (endMins > lastClassEndMins) lastClassEndMins = endMins

            if (curMins in startMins..endMins) {
                currentClass = entry
            } else if (startMins > curMins && nextClass == null) {
                nextClass = entry
                minsLeft = startMins - curMins
            }
        }

        val isDone = (currentClass == null && nextClass == null && todayEntries.isNotEmpty() && curMins > lastClassEndMins)

        // 3. Holidays (Persistent from Repository)
        val officialHolidays = academicRepo.getOfficialHolidays()
        val todayHoliday = officialHolidays.find {
            todayStr >= it.startDate && todayStr <= it.endDate
        }
        val upcomingHoliday = officialHolidays.filter {
            it.startDate > todayStr
        }.minByOrNull { it.startDate }

        // 4. Assignments, Exams, Notes, Settings
        val assignments = academicRepo.getAssignments()
        val activeAssignments = assignments.filter {
            it.status != AssignmentStatus.COMPLETED && it.dueDate >= todayStr
        }.sortedWith(compareBy({ it.dueDate }, { it.dueTime }))
        val nextAssignment = activeAssignments.firstOrNull()

        val exams = academicRepo.getExams()
        val activeExams = exams.filter {
            !it.isCompleted && it.date >= todayStr
        }.sortedWith(compareBy({ it.date }, { it.startTime }))
        val nextExam = activeExams.firstOrNull()

        val notes = academicRepo.getNotes()
        val expenses = academicRepo.getExpenses()
        val notifSettings = academicRepo.getNotificationSettings()

        // 5. Attendance Map for selected date
        val dayNameForSel = getDayNameForDate(selectedDate)
        val dayEntriesForSelectedDate = AcademicData.getFilteredTimetable(
            AcademicData.TIMETABLE[branch]?.get(dayNameForSel) ?: emptyList(),
            section
        )
        val dateAttendanceMap = attendanceRepo.getAttendanceForDate(selectedDate, dayEntriesForSelectedDate)

        _uiState.update {
            it.copy(
                subjectAttendanceList = subjectList,
                overallAttendanceStats = overallStats,
                streakCount = streak,
                customTimetable = dayEntries,
                todayEntries = todayEntries,
                currentEntry = currentClass,
                nextEntry = nextClass,
                nextEntryMinutesLeft = minsLeft,
                isAllClassesDone = isDone,
                todayHoliday = todayHoliday,
                upcomingHoliday = upcomingHoliday,
                assignments = assignments,
                nextUrgentAssignment = nextAssignment,
                exams = exams,
                nextUpcomingExam = nextExam,
                notes = notes,
                expenses = expenses,
                officialHolidays = officialHolidays,
                notificationSettings = notifSettings,
                selectedDateAttendanceMap = dateAttendanceMap
            )
        }
    }

    // ----------------------------------------------------
    // ATTENDANCE MANAGEMENT
    // ----------------------------------------------------
    private fun getDayNameForDate(dateStr: String): String {
        return try {
            val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.US)
            val date = sdf.parse(dateStr)
            val cal = Calendar.getInstance()
            if (date != null) {
                cal.time = date
                when (cal.get(Calendar.DAY_OF_WEEK)) {
                    Calendar.MONDAY -> "MONDAY"
                    Calendar.TUESDAY -> "TUESDAY"
                    Calendar.WEDNESDAY -> "WEDNESDAY"
                    Calendar.THURSDAY -> "THURSDAY"
                    Calendar.FRIDAY -> "FRIDAY"
                    Calendar.SATURDAY -> "SATURDAY"
                    Calendar.SUNDAY -> "SUNDAY"
                    else -> "MONDAY"
                }
            } else "MONDAY"
        } catch (e: Exception) {
            "MONDAY"
        }
    }

    fun selectAttendanceDate(dateStr: String) {
        try {
            val parts = dateStr.split("-")
            if (parts.size == 3) {
                val yr = parts[0].toInt()
                val mo = parts[1].toInt()
                _uiState.update {
                    it.copy(
                        selectedAttendanceDate = dateStr,
                        selectedAttendanceYear = yr,
                        selectedAttendanceMonth = mo
                    )
                }
                refreshAllData()
            }
        } catch (e: Exception) {
            _uiState.update { it.copy(selectedAttendanceDate = dateStr) }
            refreshAllData()
        }
    }

    fun selectAttendanceYear(year: Int) {
        val currentState = _uiState.value
        val currentMonth = currentState.selectedAttendanceMonth
        val day = try {
            currentState.selectedAttendanceDate.split("-")[2].toInt()
        } catch (e: Exception) {
            1
        }
        val cal = Calendar.getInstance().apply {
            set(year, currentMonth - 1, 1)
        }
        val maxDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH)
        val safeDay = day.coerceIn(1, maxDay)
        val newDateStr = String.format(Locale.US, "%04d-%02d-%02d", year, currentMonth, safeDay)

        _uiState.update {
            it.copy(
                selectedAttendanceYear = year,
                selectedAttendanceDate = newDateStr
            )
        }
        refreshAllData()
    }

    fun selectAttendanceMonth(month: Int) {
        val currentState = _uiState.value
        val currentYear = currentState.selectedAttendanceYear
        val day = try {
            currentState.selectedAttendanceDate.split("-")[2].toInt()
        } catch (e: Exception) {
            1
        }
        val cal = Calendar.getInstance().apply {
            set(currentYear, month - 1, 1)
        }
        val maxDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH)
        val safeDay = day.coerceIn(1, maxDay)
        val newDateStr = String.format(Locale.US, "%04d-%02d-%02d", currentYear, month, safeDay)

        _uiState.update {
            it.copy(
                selectedAttendanceMonth = month,
                selectedAttendanceDate = newDateStr
            )
        }
        refreshAllData()
    }

    fun markAttendanceForSubject(courseCode: String, attendedDelta: Int, totalDelta: Int) {
        attendanceRepo.addManualSubjectAttendance(courseCode, attendedDelta, totalDelta)
        refreshAllData()
    }

    fun markClassAttendance(date: String, courseCode: String, status: DayStatus) {
        attendanceRepo.markAttendance(date, courseCode, status)
        refreshAllData()
    }

    fun markAllPresent(date: String) {
        val dayName = getDayNameForDate(date)
        val entries = AcademicData.getFilteredTimetable(
            AcademicData.TIMETABLE[_uiState.value.branch]?.get(dayName) ?: emptyList(),
            _uiState.value.studentSection
        )
        attendanceRepo.markAllPresent(date, entries)
        refreshAllData()
    }

    fun markAllAbsent(date: String) {
        val dayName = getDayNameForDate(date)
        val entries = AcademicData.getFilteredTimetable(
            AcademicData.TIMETABLE[_uiState.value.branch]?.get(dayName) ?: emptyList(),
            _uiState.value.studentSection
        )
        attendanceRepo.markAllAbsent(date, entries)
        refreshAllData()
    }

    fun resetAttendanceForDate(date: String) {
        val dayName = getDayNameForDate(date)
        val entries = AcademicData.getFilteredTimetable(
            AcademicData.TIMETABLE[_uiState.value.branch]?.get(dayName) ?: emptyList(),
            _uiState.value.studentSection
        )
        attendanceRepo.resetAttendanceForDate(date, entries)
        refreshAllData()
    }

    // ----------------------------------------------------
    // TIMETABLE & NAVIGATION
    // ----------------------------------------------------
    fun selectScheduleDay(day: String) {
        _uiState.update { it.copy(selectedScheduleDay = day) }
        refreshAllData()
    }

    fun selectScheduleBranch(branchCode: String) {
        _uiState.update { it.copy(selectedScheduleBranch = branchCode) }
        refreshAllData()
    }

    fun selectDestination(destination: NavDestination) {
        val currentBackStack = _uiState.value.backStack.toMutableList()
        if (currentBackStack.lastOrNull() != destination) {
            currentBackStack.add(destination)
        }
        val todayStr = SimpleDateFormat("yyyy-MM-dd", Locale.US).format(Date())
        val cal = Calendar.getInstance()
        val todayYear = cal.get(Calendar.YEAR)
        val todayMonth = cal.get(Calendar.MONTH) + 1

        _uiState.update {
            val updates = it.copy(
                currentDestination = destination,
                backStack = currentBackStack
            )
            if (destination == NavDestination.ATTENDANCE) {
                updates.copy(
                    selectedAttendanceDate = todayStr,
                    selectedAttendanceYear = todayYear,
                    selectedAttendanceMonth = todayMonth
                )
            } else {
                updates
            }
        }
        refreshAllData()
    }

    fun navigateBack(): Boolean {
        val currentBackStack = _uiState.value.backStack.toMutableList()
        if (currentBackStack.size > 1) {
            currentBackStack.removeAt(currentBackStack.size - 1)
            val prevDestination = currentBackStack.last()
            _uiState.update {
                it.copy(
                    currentDestination = prevDestination,
                    backStack = currentBackStack
                )
            }
            return true
        }
        return false
    }

    fun selectAcademicsTab(tab: AcademicsTab) {
        val todayStr = SimpleDateFormat("yyyy-MM-dd", Locale.US).format(Date())
        val cal = Calendar.getInstance()
        val todayYear = cal.get(Calendar.YEAR)
        val todayMonth = cal.get(Calendar.MONTH) + 1

        _uiState.update {
            val updates = it.copy(selectedAcademicsTab = tab)
            if (tab == AcademicsTab.ATTENDANCE) {
                updates.copy(
                    selectedAttendanceDate = todayStr,
                    selectedAttendanceYear = todayYear,
                    selectedAttendanceMonth = todayMonth
                )
            } else {
                updates
            }
        }
        refreshAllData()
    }

    // ----------------------------------------------------
    // ASSIGNMENTS CRUD
    // ----------------------------------------------------
    fun saveAssignment(assignment: Assignment) {
        academicRepo.saveAssignment(assignment)
        refreshAllData()
    }

    fun deleteAssignment(id: String) {
        academicRepo.deleteAssignment(id)
        refreshAllData()
    }

    fun toggleAssignmentCompleted(id: String) {
        academicRepo.toggleAssignmentCompleted(id)
        refreshAllData()
    }

    // ----------------------------------------------------
    // EXAMS CRUD
    // ----------------------------------------------------
    fun saveExam(exam: Exam) {
        academicRepo.saveExam(exam)
        refreshAllData()
    }

    fun deleteExam(id: String) {
        academicRepo.deleteExam(id)
        refreshAllData()
    }

    fun toggleExamCompleted(id: String) {
        academicRepo.toggleExamCompleted(id)
        refreshAllData()
    }

    // ----------------------------------------------------
    // NOTES CRUD
    // ----------------------------------------------------
    fun saveNote(note: Note) {
        academicRepo.saveNote(note)
        refreshAllData()
    }

    fun deleteNote(id: String) {
        academicRepo.deleteNote(id)
        refreshAllData()
    }

    fun renameNoteAttachment(noteId: String, attachId: String, newName: String) {
        val notes = academicRepo.getNotes().toMutableList()
        val noteIdx = notes.indexOfFirst { it.id == noteId }
        if (noteIdx >= 0) {
            val note = notes[noteIdx]
            val updatedAttachments = note.attachments.map { att ->
                if (att.id == attachId) att.copy(displayName = newName) else att
            }
            academicRepo.saveNote(note.copy(attachments = updatedAttachments))
            refreshAllData()
        }
    }

    fun deleteNoteAttachment(noteId: String, attachId: String) {
        val notes = academicRepo.getNotes().toMutableList()
        val noteIdx = notes.indexOfFirst { it.id == noteId }
        if (noteIdx >= 0) {
            val note = notes[noteIdx]
            val updatedAttachments = note.attachments.filterNot { it.id == attachId }
            academicRepo.saveNote(note.copy(attachments = updatedAttachments))
            refreshAllData()
        }
    }

    // ----------------------------------------------------
    // EXPENSES & HOLIDAYS
    // ----------------------------------------------------
    fun saveExpense(expense: Expense) {
        academicRepo.saveExpense(expense)
        refreshAllData()
    }

    fun deleteExpense(id: String) {
        academicRepo.deleteExpense(id)
        refreshAllData()
    }

    fun saveHoliday(holiday: OfficialHoliday) {
        academicRepo.saveHoliday(holiday)
        refreshAllData()
    }

    fun deleteHoliday(id: String) {
        academicRepo.deleteHoliday(id)
        refreshAllData()
    }

    fun calculateDaysUntilHoliday(startDate: String): String {
        return try {
            val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.US)
            val holidayDate = sdf.parse(startDate) ?: return ""
            val today = Calendar.getInstance()
            today.set(Calendar.HOUR_OF_DAY, 0)
            today.set(Calendar.MINUTE, 0)
            today.set(Calendar.SECOND, 0)
            today.set(Calendar.MILLISECOND, 0)
            val diff = holidayDate.time - today.timeInMillis
            val days = diff / (24 * 60 * 60 * 1000)
            when {
                days == 0L -> "Today"
                days == 1L -> "Tomorrow"
                days > 1L -> "$days Days Left"
                else -> "Passed"
            }
        } catch (e: Exception) { "" }
    }

    fun setHolidaySearchQuery(query: String) {
        _uiState.update { it.copy(holidaySearchQuery = query) }
    }

    fun setHolidayCategoryFilter(category: String) {
        _uiState.update { it.copy(holidayCategoryFilter = category) }
    }

    fun hideHoliday(holidayId: String) {
        val currentHidden = _uiState.value.hiddenHolidayIds.toMutableSet()
        currentHidden.add(holidayId)
        _uiState.update { it.copy(hiddenHolidayIds = currentHidden) }
    }

    fun unhideAllHolidays() {
        _uiState.update { it.copy(hiddenHolidayIds = emptySet()) }
    }

    // ----------------------------------------------------
    // NOTIFICATION & SETTINGS
    // ----------------------------------------------------
    fun updateNotificationSettings(settings: NotificationSettings) {
        academicRepo.saveNotificationSettings(settings)
        _uiState.update { it.copy(notificationSettings = settings) }
    }

    fun updateThemeColor(color: BambooThemeColor) {
        academicRepo.saveThemeColor(color.name)
        _uiState.update { it.copy(themeColor = color) }
    }

    fun updateGradientIntensity(intensity: GradientIntensity) {
        _uiState.update { it.copy(gradientIntensity = intensity) }
    }

    fun showComingSoon(featureName: String) {
        _uiState.update { it.copy(comingSoonFeatureName = featureName) }
    }

    fun dismissComingSoon() {
        _uiState.update { it.copy(comingSoonFeatureName = null) }
    }

    fun markGuideCompleted() {
        _uiState.update { it.copy(guideCompleted = true) }
    }

    fun toggleHaptics(enabled: Boolean) {
        _uiState.update { it.copy(hapticsEnabled = enabled) }
    }

    fun markNotificationAsRead(id: String) {
        notificationRepo.markAsRead(id)
    }

    fun markAllNotificationsAsRead() {
        notificationRepo.markAllAsRead()
    }

    fun deleteNotification(id: String) {
        notificationRepo.deleteNotification(id)
    }

    fun openSessionDetail(session: ClassSession) {
        _uiState.update { it.copy(selectedSessionForDetail = session) }
    }

    fun dismissSessionDetail() {
        _uiState.update { it.copy(selectedSessionForDetail = null) }
    }

    fun dismissToast() {
        _uiState.update { it.copy(userSavedSuccessToast = false, toastMessage = null) }
    }

    // ----------------------------------------------------
    // TOOLS & PRODUCTIVITY
    // ----------------------------------------------------
    fun startStopwatch() {
        if (_uiState.value.isStopwatchRunning) return
        _uiState.update { it.copy(isStopwatchRunning = true) }
        stopwatchJob = viewModelScope.launch {
            while (_uiState.value.isStopwatchRunning) {
                delay(100)
                _uiState.update { it.copy(stopwatchTime = it.stopwatchTime + 100) }
            }
        }
    }

    fun pauseStopwatch() {
        _uiState.update { it.copy(isStopwatchRunning = false) }
        stopwatchJob?.cancel()
    }

    fun resetStopwatch() {
        pauseStopwatch()
        _uiState.update { it.copy(stopwatchTime = 0L, stopwatchLaps = emptyList()) }
    }

    fun lapStopwatch() {
        val currentTime = _uiState.value.stopwatchTime
        val laps = _uiState.value.stopwatchLaps.toMutableList()
        laps.add(currentTime)
        _uiState.update { it.copy(stopwatchLaps = laps) }
    }

    fun startTimer(durationMillis: Long) {
        pauseTimer()
        _uiState.update {
            it.copy(
                timerInitialDuration = durationMillis,
                timerRemainingTime = durationMillis,
                isTimerRunning = true
            )
        }
        timerJob = viewModelScope.launch {
            while (_uiState.value.isTimerRunning && _uiState.value.timerRemainingTime > 0) {
                delay(1000)
                _uiState.update { it.copy(timerRemainingTime = (it.timerRemainingTime - 1000).coerceAtLeast(0L)) }
                if (_uiState.value.timerRemainingTime == 0L) {
                    _uiState.update { it.copy(isTimerRunning = false) }
                    break
                }
            }
        }
    }

    fun pauseTimer() {
        _uiState.update { it.copy(isTimerRunning = false) }
        timerJob?.cancel()
    }

    fun resetTimer() {
        pauseTimer()
        val initial = _uiState.value.timerInitialDuration
        _uiState.update { it.copy(timerRemainingTime = initial) }
    }

    fun saveAlarm(alarm: Alarm) {
        val current = _uiState.value.alarms.toMutableList()
        val index = current.indexOfFirst { it.id == alarm.id }
        if (index >= 0) current[index] = alarm else current.add(alarm)
        _uiState.update { it.copy(alarms = current) }
    }

    fun deleteAlarm(id: String) {
        val current = _uiState.value.alarms.filterNot { it.id == id }
        _uiState.update { it.copy(alarms = current) }
    }

    fun toggleAlarm(id: String) {
        val current = _uiState.value.alarms.toMutableList()
        val index = current.indexOfFirst { it.id == id }
        if (index >= 0) {
            val old = current[index]
            current[index] = old.copy(isEnabled = !old.isEnabled)
            _uiState.update { it.copy(alarms = current) }
        }
    }
}
