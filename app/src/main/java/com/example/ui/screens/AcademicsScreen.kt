package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.Assignment
import com.example.data.Exam
import com.example.data.OfficialHoliday
import com.example.data.TimetableEntry
import com.example.ui.components.pressClick
import com.example.ui.screens.academics.AcademicHubOverview
import com.example.ui.screens.academics.AssignmentsSubScreen
import com.example.ui.screens.academics.ExamsSubScreen
import com.example.ui.screens.academics.ExpensesSubScreen
import com.example.ui.screens.academics.HolidaysSubScreen
import com.example.ui.screens.academics.NotesSubScreen
import com.example.ui.screens.academics.ToolsSubScreen
import com.example.ui.state.AcademicsTab
import com.example.ui.state.BambooUiState
import com.example.ui.theme.BambooBg
import com.example.ui.theme.BambooBorder
import com.example.ui.theme.BambooPrimaryGreen
import com.example.ui.theme.BambooSurface
import com.example.ui.theme.BambooTextPrimary
import com.example.ui.theme.BambooTextSecondary
import com.example.ui.theme.PoppinsFontFamily

@Composable
fun AcademicsScreen(
    uiState: BambooUiState,
    viewModel: com.example.ui.state.MainViewModel,
    onTabSelect: (AcademicsTab) -> Unit,
    onSearchQueryChange: (String) -> Unit,
    onSaveAssignment: (Assignment) -> Unit,
    onDeleteAssignment: (String) -> Unit,
    onToggleAssignmentCompleted: (String) -> Unit,
    onSaveExam: (Exam) -> Unit,
    onDeleteExam: (String) -> Unit,
    onToggleExamCompleted: (String) -> Unit,
    onSaveNote: (com.example.data.Note) -> Unit,
    onDeleteNote: (String) -> Unit,
    onSaveExpense: (com.example.data.Expense) -> Unit,
    onDeleteExpense: (String) -> Unit,
    onScheduleBranchChange: (String) -> Unit,
    onScheduleDayChange: (String) -> Unit,
    onAttendanceDateSelect: (String) -> Unit,
    onMarkAllPresent: () -> Unit,
    onMarkAllAbsent: () -> Unit,
    onOpenSessionDetail: (com.example.ui.state.ClassSession) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(BambooBg)
    ) {
        Spacer(modifier = Modifier.height(12.dp))

        // Horizontal Scrollable Command Strip (Top Tabs)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            AcademicsTab.values().forEach { tab ->
                val isSelected = uiState.selectedAcademicsTab == tab
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(14.dp))
                        .background(if (isSelected) BambooPrimaryGreen else BambooSurface)
                        .border(
                            1.dp,
                            if (isSelected) BambooPrimaryGreen else BambooBorder,
                            RoundedCornerShape(14.dp)
                        )
                        .pressClick { onTabSelect(tab) }
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = tab.title.uppercase(),
                        color = if (isSelected) BambooBg else BambooTextSecondary,
                        fontFamily = PoppinsFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp,
                        letterSpacing = 0.5.sp
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Main Tab Content Router
        Box(modifier = Modifier.fillMaxSize()) {
            when (uiState.selectedAcademicsTab) {
                AcademicsTab.OVERVIEW -> {
                    AcademicHubOverview(
                        nextClass = uiState.nextEntry,
                        nextAssignment = uiState.nextUrgentAssignment,
                        nextExam = uiState.nextUpcomingExam,
                        nextHoliday = uiState.nextOfficialHoliday,
                        attendancePercentage = uiState.overallAttendanceStats.percentage,
                        onNavigateTab = onTabSelect
                    )
                }

                AcademicsTab.TIMETABLE -> {
                    ScheduleScreen(
                        selectedDay = uiState.selectedScheduleDay,
                        selectedBranch = uiState.selectedScheduleBranch,
                        studentSection = uiState.studentSection,
                        onDaySelected = onScheduleDayChange,
                        onBranchSelected = onScheduleBranchChange,
                        onSelectSession = onOpenSessionDetail
                    )
                }

                AcademicsTab.ATTENDANCE -> {
                    AttendanceScreen(
                        selectedYear = uiState.selectedAttendanceYear,
                        selectedMonth = uiState.selectedAttendanceMonth,
                        selectedDate = uiState.selectedAttendanceDate,
                        selectedBranch = uiState.branch,
                        overallAttendanceStats = uiState.overallAttendanceStats,
                        subjectAttendanceList = uiState.subjectAttendanceList,
                        selectedDateAttendanceMap = uiState.selectedDateAttendanceMap,
                        studentSection = uiState.studentSection,
                        isSaving = false,
                        onYearSelected = { year -> viewModel.selectAttendanceYear(year) },
                        onMonthSelected = { month -> viewModel.selectAttendanceMonth(month) },
                        onDateSelected = { date -> viewModel.selectAttendanceDate(date) },
                        onMarkAttendance = { date, code, status -> viewModel.markClassAttendance(date, code, status) },
                        onMarkAllPresent = { viewModel.markAllPresent(uiState.selectedAttendanceDate) },
                        onMarkAllAbsent = { viewModel.markAllAbsent(uiState.selectedAttendanceDate) },
                        onResetDateAttendance = { viewModel.resetAttendanceForDate(uiState.selectedAttendanceDate) },
                        onSubjectAttendanceChange = { code, attendedDelta, totalDelta ->
                            viewModel.markAttendanceForSubject(code, attendedDelta, totalDelta)
                        }
                    )
                }

                AcademicsTab.ASSIGNMENTS -> {
                    AssignmentsSubScreen(
                        assignments = uiState.assignments,
                        onSaveAssignment = onSaveAssignment,
                        onDeleteAssignment = onDeleteAssignment,
                        onToggleCompleted = onToggleAssignmentCompleted
                    )
                }

                AcademicsTab.EXAMS -> {
                    ExamsSubScreen(
                        exams = uiState.exams,
                        onSaveExam = onSaveExam,
                        onDeleteExam = onDeleteExam,
                        onToggleCompleted = onToggleExamCompleted
                    )
                }

                AcademicsTab.NOTES -> {
                    NotesSubScreen(
                        notes = uiState.notes,
                        onSaveNote = onSaveNote,
                        onDeleteNote = onDeleteNote,
                        onRenameAttachment = { noteId, attachId, newName ->
                            viewModel.renameNoteAttachment(noteId, attachId, newName)
                        },
                        onDeleteAttachment = { noteId, attachId ->
                            viewModel.deleteNoteAttachment(noteId, attachId)
                        }
                    )
                }

                AcademicsTab.EXPENSES -> {
                    ExpensesSubScreen(
                        expenses = uiState.expenses,
                        onSaveExpense = onSaveExpense,
                        onDeleteExpense = onDeleteExpense
                    )
                }

                AcademicsTab.HOLIDAYS -> {
                    HolidaysSubScreen(
                        holidays = uiState.officialHolidays,
                        progress = uiState.holidayProgress,
                        onSaveHoliday = { viewModel.saveHoliday(it) },
                        onDeleteHoliday = { viewModel.deleteHoliday(it) },
                        calculateDaysToCome = { viewModel.calculateDaysUntilHoliday(it) }
                    )
                }

                AcademicsTab.SYLLABUS -> {
                    SyllabusContent(
                        searchQuery = uiState.syllabusSearchQuery,
                        onSearchQueryChange = onSearchQueryChange
                    )
                }

                AcademicsTab.TOOLS -> {
                    ToolsSubScreen(
                        stopwatchTime = uiState.stopwatchTime,
                        isStopwatchRunning = uiState.isStopwatchRunning,
                        stopwatchLaps = uiState.stopwatchLaps,
                        onStartStopwatch = { viewModel.startStopwatch() },
                        onPauseStopwatch = { viewModel.pauseStopwatch() },
                        onResetStopwatch = { viewModel.resetStopwatch() },
                        onLapStopwatch = { viewModel.lapStopwatch() },
                        timerRemaining = uiState.timerRemainingTime,
                        isTimerRunning = uiState.isTimerRunning,
                        onStartTimer = { viewModel.startTimer(it) },
                        onPauseTimer = { viewModel.pauseTimer() },
                        onResetTimer = { viewModel.resetTimer() },
                        alarms = uiState.alarms,
                        onSaveAlarm = { viewModel.saveAlarm(it) },
                        onDeleteAlarm = { viewModel.deleteAlarm(it) },
                        onToggleAlarm = { viewModel.toggleAlarm(it) }
                    )
                }
            }
        }
    }
}

@Composable
private fun SyllabusContent(
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit
) {
    // Legacy syllabus breakdown
    com.example.ui.screens.LegacySyllabusView(
        searchQuery = searchQuery,
        onSearchQueryChange = onSearchQueryChange
    )
}
