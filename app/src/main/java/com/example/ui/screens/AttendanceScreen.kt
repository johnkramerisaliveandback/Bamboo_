package com.example.ui.screens

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.RestartAlt
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.core.attendance.AttendanceEngine
import com.example.core.ui.EmptyState
import com.example.data.AcademicData
import com.example.data.OverallAttendanceStats
import com.example.data.SubjectAttendance
import com.example.data.TimetableEntry
import com.example.ui.components.pressClick
import com.example.ui.state.DayStatus
import com.example.ui.theme.*
import java.util.Calendar
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AttendanceScreen(
    modifier: Modifier = Modifier,
    selectedYear: Int,
    selectedMonth: Int,
    selectedDate: String,
    selectedBranch: String,
    overallAttendanceStats: OverallAttendanceStats,
    subjectAttendanceList: List<SubjectAttendance>,
    selectedDateAttendanceMap: Map<String, DayStatus>,
    studentSection: com.example.data.TimetableSection?,
    isSaving: Boolean = false,
    onYearSelected: (Int) -> Unit,
    onMonthSelected: (Int) -> Unit,
    onDateSelected: (String) -> Unit,
    onMarkAttendance: (date: String, courseCode: String, status: DayStatus) -> Unit,
    onMarkAllPresent: () -> Unit,
    onMarkAllAbsent: () -> Unit,
    onResetDateAttendance: () -> Unit = {},
    onSubjectAttendanceChange: (courseCode: String, attendedDelta: Int, totalDelta: Int) -> Unit = { _, _, _ -> }
) {
    val months = remember {
        listOf(
            "JANUARY", "FEBRUARY", "MARCH", "APRIL", "MAY", "JUNE",
            "JULY", "AUGUST", "SEPTEMBER", "OCTOBER", "NOVEMBER", "DECEMBER"
        )
    }

    val years = remember { listOf(2026, 2027, 2028, 2029, 2030) }

    val dayOfWeekName = remember(selectedDate) {
        try {
            val sdf = java.text.SimpleDateFormat("yyyy-MM-dd", Locale.US)
            val date = sdf.parse(selectedDate)
            val cal = Calendar.getInstance()
            if (date != null) {
                cal.time = date
                when (cal[Calendar.DAY_OF_WEEK]) {
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
        } catch (_: Exception) {
            "MONDAY"
        }
    }

    val scheduledEntries = remember(selectedBranch, dayOfWeekName, studentSection) {
        val allEntries = AcademicData.TIMETABLE[selectedBranch]?.get(dayOfWeekName) ?: emptyList()
        AcademicData.getFilteredTimetable(allEntries, studentSection)
    }

    val isHoliday = remember(selectedDate) { AcademicData.isHoliday(selectedDate) }
    val holidayTitle = remember(selectedDate) { AcademicData.getHolidayForDate(selectedDate)?.title ?: "Official Holiday" }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(BambooBg)
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(22.dp)
        ) {
            item {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "ATTENDANCE HUB",
                    color = BambooTextPrimary,
                    fontFamily = PoppinsFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 22.sp,
                    letterSpacing = 1.5.sp
                )
                Text(
                    text = "ACADEMIC TRACKING • $selectedBranch",
                    color = BambooTextSecondary,
                    fontFamily = PoppinsFontFamily,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 11.sp,
                    letterSpacing = 1.sp
                )
            }

            // Overall Attendance Ring Overview Card
            item {
                AttendanceRingCard(
                    stats = overallAttendanceStats
                )
            }

            // Academic Calendar Card
            item {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "ACADEMIC CALENDAR",
                            color = BambooTextPrimary,
                            fontFamily = PoppinsFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            letterSpacing = 1.sp
                        )

                        Text(
                            text = "${months[selectedMonth - 1]} $selectedYear",
                            color = BambooSoftGreen,
                            fontFamily = PoppinsFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Year Selection Tabs
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        years.forEach { year ->
                            val isSelected = year == selectedYear
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(if (isSelected) MaterialTheme.colorScheme.primary else BambooSurface)
                                    .border(
                                        1.dp,
                                        if (isSelected) MaterialTheme.colorScheme.secondary else BambooBorder,
                                        RoundedCornerShape(12.dp)
                                    )
                                    .pressClick { onYearSelected(year) }
                                    .padding(vertical = 10.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = year.toString(),
                                    color = if (isSelected) BambooBg else BambooTextSecondary,
                                    fontFamily = PoppinsFontFamily,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp
                                )
                            }
                        }
                    }
                }
            }

            // Full Calendar Grid Component
            item {
                CalendarGridCard(
                    selectedYear = selectedYear,
                    selectedMonth = selectedMonth,
                    selectedDate = selectedDate,
                    currentMonthName = months[selectedMonth - 1],
                    onPrevMonth = {
                        if (selectedMonth > 1) onMonthSelected(selectedMonth - 1)
                        else onMonthSelected(12)
                    },
                    onNextMonth = {
                        if (selectedMonth < 12) onMonthSelected(selectedMonth + 1)
                        else onMonthSelected(1)
                    },
                    onSelectDate = onDateSelected
                )
            }

            // Date Detail & Class Attendance Marking Panel
            item {
                DateAttendanceDetailCard(
                    selectedDate = selectedDate,
                    dayOfWeekName = dayOfWeekName,
                    isHoliday = isHoliday,
                    holidayTitle = holidayTitle,
                    scheduledEntries = scheduledEntries,
                    attendanceMap = selectedDateAttendanceMap,
                    isSaving = isSaving,
                    onMarkAttendance = { code, status -> onMarkAttendance(selectedDate, code, status) },
                    onMarkAllPresent = onMarkAllPresent,
                    onMarkAllAbsent = onMarkAllAbsent,
                    onResetAttendance = onResetDateAttendance
                )
            }

            // Subject-wise Breakdown Header
            item {
                Text(
                    text = "SUBJECT-WISE BREAKDOWN",
                    color = BambooTextPrimary,
                    fontFamily = PoppinsFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    letterSpacing = 1.sp
                )
            }

            if (subjectAttendanceList.isEmpty()) {
                item {
                    EmptyState(
                        title = "No Attendance Records",
                        description = "Mark attendance for today's classes or tap + ATTENDED on a subject below."
                    )
                }
            } else {
                items(subjectAttendanceList) { subject ->
                    SubjectAttendanceCard(
                        subject = subject,
                        onAttendanceChange = { attendedDelta, totalDelta ->
                            onSubjectAttendanceChange(subject.subjectCode, attendedDelta, totalDelta)
                        }
                    )
                }
            }

            // Report Issue / Bug Card
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(20.dp))
                        .background(BambooSurface)
                        .border(1.dp, BambooBorder, RoundedCornerShape(20.dp))
                        .padding(18.dp)
                ) {
                    Column {
                        Text(
                            text = "FOUND AN ISSUE OR BUG?",
                            color = BambooTextPrimary,
                            fontFamily = PoppinsFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp,
                            letterSpacing = 1.sp
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "If you encounter any issue or bug, please contact us here:",
                            color = BambooTextSecondary,
                            fontFamily = PoppinsFontFamily,
                            fontSize = 11.sp
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "johnkramerisaliveandback@gmail.com",
                            color = MaterialTheme.colorScheme.primary,
                            fontFamily = PoppinsFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp
                        )
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(100.dp))
            }
        }
    }
}

@Composable
private fun AttendanceRingCard(
    stats: OverallAttendanceStats
) {
    val pct = stats.percentage.toInt()
    val animatedPercentage by animateIntAsState(
        targetValue = pct,
        animationSpec = tween(durationMillis = 600)
    )
    val primaryColor = MaterialTheme.colorScheme.primary

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .background(BambooSurface)
            .border(1.dp, BambooBorderHighlight, RoundedCornerShape(24.dp))
            .padding(22.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "Overall Attendance",
                color = BambooTextPrimary,
                fontFamily = PoppinsFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Gauge Circular Ring
            Box(
                modifier = Modifier.size(150.dp),
                contentAlignment = Alignment.Center
            ) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val strokeWidth = 14.dp.toPx()
                    drawArc(
                        color = BambooElevated,
                        startAngle = 135f,
                        sweepAngle = 270f,
                        useCenter = false,
                        style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
                    )
                    drawArc(
                        color = primaryColor,
                        startAngle = 135f,
                        sweepAngle = 270f * (animatedPercentage / 100f),
                        useCenter = false,
                        style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
                    )
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "$animatedPercentage%",
                        color = BambooTextPrimary,
                        fontFamily = PoppinsFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 36.sp
                    )
                    Box(
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(BambooElevated)
                            .padding(horizontal = 10.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = stats.statusStanding,
                            color = when (stats.statusStanding) {
                                "GOOD STANDING" -> BambooSoftGreen
                                "WARNING" -> BambooHolidayYellow
                                "LOW ATTENDANCE" -> BambooAbsentRed
                                else -> BambooTextMuted
                            },
                            fontFamily = PoppinsFontFamily,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 10.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            val subtitleText = if (stats.totalClasses == 0) {
                "0/0 ATTENDED • 0%"
            } else {
                "Attended ${stats.totalPresent} of ${stats.totalClasses} total marked classes."
            }

            Text(
                text = subtitleText,
                color = BambooTextSecondary,
                fontFamily = PoppinsFontFamily,
                fontSize = 12.sp
            )
        }
    }
}

@Composable
private fun CalendarGridCard(
    selectedYear: Int,
    selectedMonth: Int,
    selectedDate: String,
    currentMonthName: String,
    onPrevMonth: () -> Unit,
    onNextMonth: () -> Unit,
    onSelectDate: (String) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(BambooSurface)
            .border(1.dp, BambooBorder, RoundedCornerShape(20.dp))
            .padding(16.dp)
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onPrevMonth) {
                    Icon(Icons.Default.ChevronLeft, contentDescription = "Previous Month", tint = BambooTextPrimary)
                }

                Text(
                    text = "$currentMonthName $selectedYear",
                    color = BambooTextPrimary,
                    fontFamily = PoppinsFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )

                IconButton(onClick = onNextMonth) {
                    Icon(Icons.Default.ChevronRight, contentDescription = "Next Month", tint = BambooTextPrimary)
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                listOf("S", "M", "T", "W", "T", "F", "S").forEach { label ->
                    Text(
                        text = label,
                        color = BambooTextMuted,
                        fontFamily = PoppinsFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            AnimatedContent(
                targetState = "$selectedYear-$selectedMonth",
                transitionSpec = {
                    (fadeIn(tween(250)) + slideInHorizontally { width -> width / 4 }) togetherWith
                            (fadeOut(tween(250)) + slideOutHorizontally { width -> -width / 4 })
                },
                label = "CalendarMonthTransition"
            ) { state ->
                val parts = state.split("-")
                val yr = parts[0].toInt()
                val mo = parts[1].toInt()
                val calForGrid = Calendar.getInstance().apply { 
                    set(Calendar.YEAR, yr)
                    set(Calendar.MONTH, mo - 1)
                    set(Calendar.DAY_OF_MONTH, 1) 
                }
                val fDow = calForGrid.get(Calendar.DAY_OF_WEEK)
                val off = fDow - 1
                val mDays = calForGrid.getActualMaximum(Calendar.DAY_OF_MONTH)
                val tCells = mDays + off
                val tRows = (tCells + 6) / 7

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    for (row in 0 until tRows) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            for (col in 0 until 7) {
                                val index = row * 7 + col
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .aspectRatio(1f),
                                    contentAlignment = Alignment.Center
                                ) {
                                    if (index in off until (off + mDays)) {
                                        val dayNum = (index - off) + 1
                                        val dateStr = String.format(Locale.US, "%04d-%02d-%02d", yr, mo, dayNum)
                                        val isSelected = dateStr == selectedDate
                                        val isHoliday = AcademicData.isHoliday(dateStr)

                                        val cellBg = when {
                                            isSelected -> MaterialTheme.colorScheme.primary
                                            isHoliday -> BambooHolidayYellow.copy(alpha = 0.25f)
                                            else -> BambooElevated
                                        }

                                        val textColor = when {
                                            isSelected -> BambooBg
                                            isHoliday -> BambooHolidayYellow
                                            else -> BambooTextPrimary
                                        }

                                        Box(
                                            modifier = Modifier
                                                .fillMaxSize()
                                                .clip(RoundedCornerShape(8.dp))
                                                .background(cellBg)
                                                .border(
                                                    width = if (isSelected) 1.5.dp else 1.dp,
                                                    color = if (isSelected) MaterialTheme.colorScheme.secondary else if (isHoliday) BambooHolidayYellow else BambooBorder,
                                                    shape = RoundedCornerShape(8.dp)
                                                )
                                                .pressClick { onSelectDate(dateStr) },
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                                Text(
                                                    text = dayNum.toString(),
                                                    color = textColor,
                                                    fontFamily = PoppinsFontFamily,
                                                    fontWeight = FontWeight.Bold,
                                                    fontSize = 12.sp
                                                )
                                                if (isHoliday && !isSelected) {
                                                    Text(
                                                        text = "H",
                                                        color = BambooHolidayYellow,
                                                        fontFamily = PoppinsFontFamily,
                                                        fontWeight = FontWeight.Bold,
                                                        fontSize = 8.sp
                                                    )
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                LegendIndicator(color = BambooPrimaryGreen, label = "Selected")
                LegendIndicator(color = BambooHolidayYellow, label = "Holiday")
            }
        }
    }
}

@Composable
private fun DateAttendanceDetailCard(
    selectedDate: String,
    dayOfWeekName: String,
    isHoliday: Boolean,
    holidayTitle: String,
    scheduledEntries: List<TimetableEntry>,
    attendanceMap: Map<String, DayStatus>,
    isSaving: Boolean = false,
    onMarkAttendance: (courseCode: String, status: DayStatus) -> Unit,
    onMarkAllPresent: () -> Unit,
    onMarkAllAbsent: () -> Unit,
    onResetAttendance: () -> Unit = {}
) {
    var showMarkAllPresentDialog by remember { mutableStateOf(false) }
    var showMarkAllAbsentDialog by remember { mutableStateOf(false) }

    if (showMarkAllPresentDialog) {
        AlertDialog(
            onDismissRequest = { showMarkAllPresentDialog = false },
            title = { Text("Mark All Present", fontFamily = PoppinsFontFamily, fontWeight = FontWeight.Bold) },
            text = { Text("Are you sure you want to mark all scheduled classes for $selectedDate as Present? Cancelled classes will not be modified.", fontFamily = PoppinsFontFamily) },
            confirmButton = {
                TextButton(onClick = {
                    showMarkAllPresentDialog = false
                    onMarkAllPresent()
                }) {
                    Text("Confirm", fontFamily = PoppinsFontFamily, fontWeight = FontWeight.Bold, color = BambooPrimaryGreen)
                }
            },
            dismissButton = {
                TextButton(onClick = { showMarkAllPresentDialog = false }) {
                    Text("Cancel", fontFamily = PoppinsFontFamily, color = BambooTextSecondary)
                }
            },
            containerColor = BambooSurface
        )
    }

    if (showMarkAllAbsentDialog) {
        AlertDialog(
            onDismissRequest = { showMarkAllAbsentDialog = false },
            title = { Text("Mark All Absent", fontFamily = PoppinsFontFamily, fontWeight = FontWeight.Bold) },
            text = { Text("Are you sure you want to mark all scheduled classes for $selectedDate as Absent? Cancelled classes will not be modified.", fontFamily = PoppinsFontFamily) },
            confirmButton = {
                TextButton(onClick = {
                    showMarkAllAbsentDialog = false
                    onMarkAllAbsent()
                }) {
                    Text("Confirm", fontFamily = PoppinsFontFamily, fontWeight = FontWeight.Bold, color = BambooAbsentRed)
                }
            },
            dismissButton = {
                TextButton(onClick = { showMarkAllAbsentDialog = false }) {
                    Text("Cancel", fontFamily = PoppinsFontFamily, color = BambooTextSecondary)
                }
            },
            containerColor = BambooSurface
        )
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(BambooSurface)
            .border(1.dp, BambooBorderHighlight, RoundedCornerShape(20.dp))
            .padding(18.dp)
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "SELECTED DATE",
                        color = BambooTextMuted,
                        fontFamily = PoppinsFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 10.sp,
                        letterSpacing = 1.sp
                    )
                    Text(
                        text = "$selectedDate ($dayOfWeekName)",
                        color = BambooTextPrimary,
                        fontFamily = PoppinsFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                }

                if (!isHoliday && scheduledEntries.isNotEmpty()) {
                    Row(
                        modifier = Modifier.padding(vertical = 4.dp),
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(
                            onClick = onResetAttendance,
                            enabled = !isSaving,
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(if (isSaving) BambooElevated.copy(alpha = 0.5f) else BambooElevated)
                                .border(1.dp, BambooBorder, CircleShape)
                        ) {
                            Icon(
                                imageVector = Icons.Default.RestartAlt,
                                contentDescription = "Reset Attendance",
                                tint = BambooTextMuted,
                                modifier = Modifier.size(18.dp)
                            )
                        }

                        Button(
                            onClick = { showMarkAllPresentDialog = true },
                            enabled = !isSaving,
                            modifier = Modifier
                                .height(44.dp)
                                .testTag("mark_all_present_date_button"),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (isSaving) BambooPresentGreen.copy(alpha = 0.5f) else BambooPresentGreen,
                                contentColor = BambooBg
                            ),
                            shape = RoundedCornerShape(12.dp),
                            contentPadding = PaddingValues(horizontal = 16.dp)
                        ) {
                            Text(
                                text = "ALL PRESENT",
                                fontFamily = PoppinsFontFamily,
                                fontWeight = FontWeight.Bold,
                                fontSize = 11.sp,
                                letterSpacing = 0.5.sp
                            )
                        }

                        Button(
                            onClick = { showMarkAllAbsentDialog = true },
                            enabled = !isSaving,
                            modifier = Modifier
                                .height(44.dp)
                                .testTag("mark_all_absent_date_button"),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (isSaving) BambooAbsentRed.copy(alpha = 0.5f) else BambooAbsentRed,
                                contentColor = BambooTextPrimary
                            ),
                            shape = RoundedCornerShape(12.dp),
                            contentPadding = PaddingValues(horizontal = 16.dp)
                        ) {
                            Text(
                                text = "ALL ABSENT",
                                fontFamily = PoppinsFontFamily,
                                fontWeight = FontWeight.Bold,
                                fontSize = 11.sp,
                                letterSpacing = 0.5.sp
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            if (isHoliday) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(BambooHolidayYellow.copy(alpha = 0.2f))
                        .border(1.dp, BambooHolidayYellow, RoundedCornerShape(12.dp))
                        .padding(14.dp)
                ) {
                    Column {
                        Text(
                            text = "ACADEMIC HOLIDAY",
                            color = BambooHolidayYellow,
                            fontFamily = PoppinsFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp,
                            letterSpacing = 1.sp
                        )
                        Text(
                            text = holidayTitle,
                            color = BambooTextPrimary,
                            fontFamily = PoppinsFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp
                        )
                        Text(
                            text = "Holidays are excluded from attendance percentage calculations.",
                            color = BambooTextSecondary,
                            fontFamily = PoppinsFontFamily,
                            fontSize = 11.sp
                        )
                    }
                }
            } else if (scheduledEntries.isEmpty()) {
                Text(
                    text = "No classes scheduled on $dayOfWeekName.",
                    color = BambooTextMuted,
                    fontFamily = PoppinsFontFamily,
                    fontSize = 12.sp
                )
            } else {
                scheduledEntries.forEach { entry ->
                    val status = attendanceMap.getOrDefault(entry.subjectCode, DayStatus.NONE)
                    ClassAttendanceMarkRow(
                        entry = entry,
                        status = status,
                        isSaving = isSaving,
                        onSelectStatus = { newStatus -> 
                            onMarkAttendance(entry.subjectCode, newStatus) 
                        }
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                }
            }
        }
    }
}

@Composable
private fun ClassAttendanceMarkRow(
    entry: TimetableEntry,
    status: DayStatus,
    isSaving: Boolean = false,
    onSelectStatus: (DayStatus) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(BambooElevated)
            .border(1.dp, BambooBorder, RoundedCornerShape(14.dp))
            .padding(12.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f).padding(end = 8.dp)) {
                    Text(
                        text = entry.subjectTitle,
                        color = BambooTextPrimary,
                        fontFamily = PoppinsFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        softWrap = true
                    )
                    Text(
                        text = entry.time,
                        color = BambooTextMuted,
                        fontFamily = PoppinsFontFamily,
                        fontSize = 11.sp
                    )
                }

                if (status != DayStatus.NONE) {
                    val badgeColor = when (status) {
                        DayStatus.PRESENT -> BambooPresentGreen
                        DayStatus.ABSENT -> BambooAbsentRed
                        DayStatus.CANCELLED -> BambooHolidayYellow
                        else -> BambooTextMuted
                    }
                    val badgeText = when (status) {
                        DayStatus.PRESENT -> "● Present"
                        DayStatus.ABSENT -> "● Absent"
                        DayStatus.CANCELLED -> "● Cancelled"
                        else -> ""
                    }
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(badgeColor.copy(alpha = 0.2f))
                            .border(1.dp, badgeColor, RoundedCornerShape(8.dp))
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = badgeText,
                            color = badgeColor,
                            fontFamily = PoppinsFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 10.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .heightIn(min = 38.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(if (status == DayStatus.PRESENT) BambooPresentGreen else BambooSurface)
                        .border(
                            1.dp,
                            if (status == DayStatus.PRESENT) BambooPresentGreen else BambooBorder,
                            RoundedCornerShape(10.dp)
                        )
                        .pressClick(enabled = !isSaving) { onSelectStatus(if (status == DayStatus.PRESENT) DayStatus.NONE else DayStatus.PRESENT) },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "PRESENT",
                        color = if (status == DayStatus.PRESENT) BambooBg else BambooTextSecondary,
                        fontFamily = PoppinsFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 10.sp,
                        letterSpacing = 0.5.sp
                    )
                }

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .heightIn(min = 38.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(if (status == DayStatus.ABSENT) BambooAbsentRed else BambooSurface)
                        .border(
                            1.dp,
                            if (status == DayStatus.ABSENT) BambooAbsentRed else BambooBorder,
                            RoundedCornerShape(10.dp)
                        )
                        .pressClick(enabled = !isSaving) { onSelectStatus(if (status == DayStatus.ABSENT) DayStatus.NONE else DayStatus.ABSENT) },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "ABSENT",
                        color = if (status == DayStatus.ABSENT) BambooTextPrimary else BambooTextSecondary,
                        fontFamily = PoppinsFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 10.sp,
                        letterSpacing = 0.5.sp
                    )
                }

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .heightIn(min = 38.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(if (status == DayStatus.CANCELLED) BambooHolidayYellow else BambooSurface)
                        .border(
                            1.dp,
                            if (status == DayStatus.CANCELLED) BambooHolidayYellow else BambooBorder,
                            RoundedCornerShape(10.dp)
                        )
                        .pressClick(enabled = !isSaving) { onSelectStatus(if (status == DayStatus.CANCELLED) DayStatus.NONE else DayStatus.CANCELLED) },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "CANCELLED",
                        color = if (status == DayStatus.CANCELLED) BambooBg else BambooTextSecondary,
                        fontFamily = PoppinsFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 10.sp,
                        letterSpacing = 0.5.sp
                    )
                }
            }
        }
    }
}

@Composable
private fun SubjectAttendanceCard(
    subject: SubjectAttendance,
    onAttendanceChange: (attendedDelta: Int, totalDelta: Int) -> Unit
) {
    val calculation = remember(subject.attendedClasses, subject.totalClasses) {
        AttendanceEngine.calculate(subject.attendedClasses, subject.totalClasses)
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(BambooSurface)
            .border(1.dp, BambooBorder, RoundedCornerShape(20.dp))
            .padding(18.dp)
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f).padding(end = 12.dp)) {
                    Text(
                        text = subject.subjectName.uppercase(),
                        color = BambooTextPrimary,
                        fontFamily = PoppinsFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        letterSpacing = 0.5.sp,
                        softWrap = true,
                        overflow = TextOverflow.Clip
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = if (subject.totalClasses == 0) "0 / 0 classes"
                               else "${subject.attendedClasses} / ${subject.totalClasses} classes",
                        color = BambooTextMuted,
                        fontFamily = PoppinsFontFamily,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 11.sp,
                        letterSpacing = 0.5.sp
                    )
                }

                Text(
                    text = if (subject.totalClasses == 0) "0%"
                           else "${String.format(Locale.US, "%.0f", calculation.percentage)}%",
                    color = if (subject.totalClasses == 0) BambooTextMuted
                           else if (calculation.percentage >= 75f) BambooSoftGreen else BambooAbsentRed,
                    fontFamily = PoppinsFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            LinearProgressIndicator(
                progress = { if (subject.totalClasses == 0) 0f else (calculation.percentage / 100f).coerceIn(0f, 1f) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
                    .clip(CircleShape),
                color = if (subject.totalClasses == 0) BambooElevated
                       else if (calculation.percentage >= 75f) MaterialTheme.colorScheme.primary else BambooAbsentRed,
                trackColor = BambooElevated
            )

            // Dynamic Target Insight
            val targetInsightText = when {
                subject.totalClasses == 0 -> "Mark attendance to calculate target %"
                calculation.percentage < 75f -> "Need ${calculation.classesNeededForTarget} more consecutive classes to reach 75%"
                calculation.classesCanSkip > 0 -> "Can skip ${calculation.classesCanSkip} class(es) and maintain 75%"
                else -> "On target at 75%"
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = targetInsightText,
                color = if (calculation.percentage >= 75f) BambooSoftGreen else BambooHolidayYellow,
                fontFamily = PoppinsFontFamily,
                fontWeight = FontWeight.SemiBold,
                fontSize = 11.sp
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Action Controls to manually adjust attendance (+ Attended / + Missed)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedButton(
                    onClick = { onAttendanceChange(1, 1) },
                    modifier = Modifier.weight(1f).height(36.dp),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = BambooSoftGreen),
                    border = androidx.compose.foundation.BorderStroke(1.dp, BambooSoftGreen.copy(alpha = 0.5f)),
                    contentPadding = PaddingValues(horizontal = 8.dp)
                ) {
                    Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("ATTENDED", fontSize = 10.sp, fontWeight = FontWeight.Bold)
                }

                OutlinedButton(
                    onClick = { onAttendanceChange(0, 1) },
                    modifier = Modifier.weight(1f).height(36.dp),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = BambooAbsentRed),
                    border = androidx.compose.foundation.BorderStroke(1.dp, BambooAbsentRed.copy(alpha = 0.5f)),
                    contentPadding = PaddingValues(horizontal = 8.dp)
                ) {
                    Icon(Icons.Default.Close, contentDescription = null, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("MISSED", fontSize = 10.sp, fontWeight = FontWeight.Bold)
                }

                if (subject.totalClasses > 0) {
                    IconButton(
                        onClick = { onAttendanceChange(-1, -1) },
                        modifier = Modifier.size(36.dp).clip(RoundedCornerShape(10.dp)).background(BambooElevated)
                    ) {
                        Icon(Icons.Default.Remove, contentDescription = "Undo", tint = BambooTextMuted, modifier = Modifier.size(16.dp))
                    }
                }
            }
        }
    }
}

@Composable
private fun LegendIndicator(color: Color, label: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(8.dp)
                .clip(CircleShape)
                .background(color)
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(
            text = label,
            color = BambooTextMuted,
            fontFamily = PoppinsFontFamily,
            fontSize = 11.sp
        )
    }
}
