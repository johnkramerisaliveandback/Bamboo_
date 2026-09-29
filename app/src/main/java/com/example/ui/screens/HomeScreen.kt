package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.EventNote
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.outlined.Book
import androidx.compose.material.icons.outlined.CheckCircleOutline
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.AcademicData
import com.example.data.Assignment
import com.example.data.Exam
import com.example.data.OfficialHoliday
import com.example.data.OverallAttendanceStats
import com.example.data.TimetableEntry
import com.example.ui.components.pressClick
import com.example.ui.state.ClassSession
import com.example.ui.state.SessionStatus
import com.example.ui.theme.*
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@Composable
fun HomeScreen(
    studentName: String,
    branch: String,
    studentYear: String = "1st Year",
    studentSection: com.example.data.TimetableSection? = null,
    todayEntries: List<TimetableEntry>,
    currentEntry: TimetableEntry?,
    nextEntry: TimetableEntry?,
    nextEntryMinutesLeft: Int?,
    todayHoliday: OfficialHoliday? = null,
    upcomingHoliday: OfficialHoliday? = null,
    nextUrgentAssignment: Assignment? = null,
    nextUpcomingExam: Exam? = null,
    guideCompleted: Boolean = false,
    isAllClassesDone: Boolean = false,
    congratsMessage: String? = null,
    currentQuote: String = "",
    overallAttendanceStats: OverallAttendanceStats,
    unreadNotificationsCount: Int = 0,
    isCloudSyncing: Boolean = false,
    onNavigateToSchedule: () -> Unit,
    onNavigateToAttendance: () -> Unit,
    onNavigateToNotifications: () -> Unit,
    onNavigateToGuide: () -> Unit = {},
    onSkipGuide: () -> Unit = {},
    onSelectSession: (ClassSession) -> Unit,
    modifier: Modifier = Modifier
) {
    var isVisible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        isVisible = true
    }

    val dateFormatted = remember {
        val sdf = SimpleDateFormat("EEEE • MMM dd, yyyy", Locale.US)
        sdf.format(Date()).uppercase()
    }

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
                HomeHeaderTopBar(
                    unreadCount = unreadNotificationsCount,
                    isSyncing = isCloudSyncing,
                    onNotificationClick = onNavigateToNotifications
                )
            }

            // Optional First-Run Tour Banner
            if (!guideCompleted) {
                item {
                    FirstRunTourCard(
                        onStartTour = onNavigateToGuide,
                        onSkip = onSkipGuide
                    )
                }
            }

            item {
                AnimatedVisibility(
                    visible = isVisible,
                    enter = fadeIn(tween(500)) + slideInVertically { it / 3 }
                ) {
                    HomeGreetingHeader(
                        studentName = studentName,
                        branch = branch,
                        studentYear = studentYear,
                        studentSection = studentSection,
                        dateFormatted = dateFormatted,
                        todayCount = if (todayHoliday != null) 0 else todayEntries.size
                    )
                }
            }

            item {
                MotivationCard(quote = currentQuote)
            }

            if (studentSection == null) {
                item {
                    Box(
                        modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(16.dp)).background(BambooElevated).border(1.dp, BambooPrimaryGreen, RoundedCornerShape(16.dp)).padding(16.dp)
                    ) {
                        Column {
                            Text("⚠️ SECTION NOT SELECTED", color = BambooPrimaryGreen, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                            Text("Complete your profile setup to view your timetable.", color = BambooTextSecondary, fontSize = 12.sp)
                        }
                    }
                }
            }

            if (congratsMessage != null && todayHoliday == null) {
                item {
                    CongratsBanner(message = congratsMessage)
                }
            }

            item {
                HomeMetricsOverviewRow(
                    todayCount = if (todayHoliday != null) 0 else todayEntries.size,
                    attendanceStats = overallAttendanceStats,
                    onNavigateToSchedule = onNavigateToSchedule,
                    onNavigateToAttendance = onNavigateToAttendance
                )
            }

            // MAIN CLASS SECTION — STRICT HOLIDAY SUPPRESSION PRIORITY
            when {
                todayHoliday != null -> {
                    item {
                        HomeHolidayCard(holiday = todayHoliday)
                    }
                }

                todayEntries.isNotEmpty() -> {
                    if (isAllClassesDone) {
                        item {
                            DoneForTheDayCard(studentName = studentName, quote = currentQuote)
                        }
                    } else {
                        if (currentEntry != null) {
                            item {
                                HomeCurrentClassSection(
                                    currentEntry = currentEntry,
                                    onClick = {
                                        onSelectSession(ClassSession(
                                            id = "${currentEntry.subjectCode}_cur",
                                            time = currentEntry.time,
                                            subject = currentEntry.subjectTitle,
                                            courseCode = currentEntry.subjectCode,
                                            room = currentEntry.room,
                                            teacher = currentEntry.teacherName,
                                            status = SessionStatus.CURRENT,
                                            tags = listOf("HAPPENING NOW", currentEntry.type)
                                        ))
                                    }
                                )
                            }
                        }

                        if (nextEntry != null) {
                            item {
                                HomeNextUpSection(
                                    nextEntry = nextEntry,
                                    minutesLeft = nextEntryMinutesLeft,
                                    onClick = {
                                        onSelectSession(ClassSession(
                                            id = "${nextEntry.subjectCode}_next",
                                            time = nextEntry.time,
                                            subject = nextEntry.subjectTitle,
                                            courseCode = nextEntry.subjectCode,
                                            room = nextEntry.room,
                                            teacher = nextEntry.teacherName,
                                            status = SessionStatus.UPCOMING,
                                            tags = listOf("NEXT UP", nextEntry.type)
                                        ))
                                    }
                                )
                            }
                        }

                        item {
                            Text("TODAY'S SCHEDULE", color = BambooTextPrimary, fontFamily = PoppinsFontFamily, fontWeight = FontWeight.Bold, fontSize = 16.sp, letterSpacing = 1.2.sp)
                        }

                        itemsIndexed(todayEntries) { index, entry ->
                            HomeTimelineItem(
                                entry = entry,
                                isCurrent = currentEntry?.subjectCode == entry.subjectCode && currentEntry?.period == entry.period,
                                isLast = index == todayEntries.lastIndex,
                                onClick = {
                                    onSelectSession(ClassSession(
                                        id = "${entry.subjectCode}_${entry.period}",
                                        time = entry.time,
                                        subject = entry.subjectTitle,
                                        courseCode = entry.subjectCode,
                                        room = entry.room,
                                        teacher = entry.teacherName,
                                        status = if (currentEntry?.subjectCode == entry.subjectCode && currentEntry.period == entry.period) SessionStatus.CURRENT else SessionStatus.UPCOMING
                                    ))
                                }
                            )
                        }
                    }
                }

                else -> {
                    item {
                        NoClassesCard()
                    }
                }
            }

            // UPCOMING EVENTS & DEADLINES SECTION (ALWAYS SHOWN REGARDLESS OF HOLIDAY)
            if (nextUrgentAssignment != null || nextUpcomingExam != null || upcomingHoliday != null) {
                item {
                    Text("UPCOMING DEADLINES & EVENTS", color = BambooTextPrimary, fontFamily = PoppinsFontFamily, fontWeight = FontWeight.Bold, fontSize = 16.sp, letterSpacing = 1.2.sp)
                }

                if (nextUrgentAssignment != null) {
                    item {
                        HomeAssignmentWidget(assignment = nextUrgentAssignment)
                    }
                }

                if (nextUpcomingExam != null) {
                    item {
                        HomeExamWidget(exam = nextUpcomingExam)
                    }
                }

                if (upcomingHoliday != null && todayHoliday == null) {
                    item {
                        UpcomingHolidayWidget(
                            holiday = upcomingHoliday,
                            daysLeft = calculateDaysLeft(upcomingHoliday.startDate)
                        )
                    }
                }
            }

            item { Spacer(Modifier.height(100.dp)) }
        }
    }
}

@Composable
private fun HomeAssignmentWidget(assignment: Assignment) {
    val subjectTitle = remember(assignment.subject) {
        AcademicData.resolveCourseTitle(assignment.subject)
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(BambooElevated)
            .border(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.5f), RoundedCornerShape(16.dp))
            .padding(16.dp)
    ) {
        Row(verticalAlignment = Alignment.Top) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.Assignment, null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(20.dp))
            }
            Spacer(modifier = Modifier.width(14.dp))
            Column(modifier = Modifier.weight(1f)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("ASSIGNMENT DUE", color = MaterialTheme.colorScheme.primary, fontSize = 10.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.2.sp)
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(BambooPrimaryGreen.copy(alpha = 0.2f))
                            .padding(horizontal = 8.dp, vertical = 2.dp)
                    ) {
                        Text(assignment.priority.label.uppercase(), color = BambooPrimaryGreen, fontWeight = FontWeight.Bold, fontSize = 9.sp)
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = assignment.title,
                    color = BambooTextPrimary,
                    fontFamily = PoppinsFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Text(
                    text = "$subjectTitle • Due ${assignment.dueDate} at ${assignment.dueTime}",
                    color = BambooSoftGreen,
                    fontFamily = PoppinsFontFamily,
                    fontSize = 11.sp
                )

                if (assignment.description.isNotBlank()) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = assignment.description,
                        color = BambooTextSecondary,
                        fontFamily = PoppinsFontFamily,
                        fontSize = 11.sp,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }
}

@Composable
private fun HomeExamWidget(exam: Exam) {
    val subjectTitle = remember(exam.subject) {
        AcademicData.resolveCourseTitle(exam.subject)
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(BambooElevated)
            .border(1.dp, BambooHolidayYellow.copy(alpha = 0.6f), RoundedCornerShape(16.dp))
            .padding(16.dp)
    ) {
        Row(verticalAlignment = Alignment.Top) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(BambooHolidayYellow.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.EventNote, null, tint = BambooHolidayYellow, modifier = Modifier.size(20.dp))
            }
            Spacer(modifier = Modifier.width(14.dp))
            Column(modifier = Modifier.weight(1f)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("UPCOMING EXAM", color = BambooHolidayYellow, fontSize = 10.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.2.sp)
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(BambooHolidayYellow.copy(alpha = 0.2f))
                            .padding(horizontal = 8.dp, vertical = 2.dp)
                    ) {
                        Text(exam.examType.label.uppercase(), color = BambooHolidayYellow, fontWeight = FontWeight.Bold, fontSize = 9.sp)
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = subjectTitle,
                    color = BambooTextPrimary,
                    fontFamily = PoppinsFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Text(
                    text = "Date: ${exam.date} • ${exam.startTime} - ${exam.endTime}" + if (exam.venue.isNotBlank()) " • Venue: ${exam.venue}" else "",
                    color = BambooSoftGreen,
                    fontFamily = PoppinsFontFamily,
                    fontSize = 11.sp
                )
            }
        }
    }
}

@Composable
private fun FirstRunTourCard(
    onStartTour: () -> Unit,
    onSkip: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(Brush.linearGradient(listOf(BambooDarkGreen, BambooSurface)))
            .border(1.dp, MaterialTheme.colorScheme.primary, RoundedCornerShape(20.dp))
            .padding(18.dp)
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.AutoAwesome, null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("WELCOME TO ACADEMIC OS", color = MaterialTheme.colorScheme.primary, fontFamily = PoppinsFontFamily, fontWeight = FontWeight.Bold, fontSize = 11.sp, letterSpacing = 1.2.sp)
                }

                IconButton(onClick = onSkip, modifier = Modifier.size(24.dp)) {
                    Icon(Icons.Default.Close, null, tint = BambooTextMuted, modifier = Modifier.size(16.dp))
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text("Take a quick 1-minute tour of your local workspace.", color = BambooTextPrimary, fontFamily = PoppinsFontFamily, fontWeight = FontWeight.Bold, fontSize = 15.sp)

            Spacer(modifier = Modifier.height(12.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                Button(
                    onClick = onStartTour,
                    modifier = Modifier.weight(1f).height(40.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary, contentColor = BambooBg),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text("START TOUR", fontFamily = PoppinsFontFamily, fontWeight = FontWeight.Bold, fontSize = 11.sp)
                }

                OutlinedButton(
                    onClick = onSkip,
                    modifier = Modifier.weight(1f).height(40.dp),
                    shape = RoundedCornerShape(10.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, BambooBorder)
                ) {
                    Text("SKIP", color = BambooTextSecondary, fontFamily = PoppinsFontFamily, fontWeight = FontWeight.Bold, fontSize = 11.sp)
                }
            }
        }
    }
}

@Composable
private fun MotivationCard(quote: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(BambooElevated)
            .border(1.dp, BambooBorder, RoundedCornerShape(16.dp))
            .padding(18.dp)
    ) {
        Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.AutoAwesome, null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text("DAILY MOTIVATION", color = MaterialTheme.colorScheme.primary, fontFamily = PoppinsFontFamily, fontWeight = FontWeight.Bold, fontSize = 10.sp, letterSpacing = 1.5.sp)
            }
            Spacer(modifier = Modifier.height(10.dp))
            Text("\"$quote\"", color = BambooTextPrimary, fontFamily = PoppinsFontFamily, fontSize = 14.sp, fontWeight = FontWeight.Medium, lineHeight = 20.sp, fontStyle = androidx.compose.ui.text.font.FontStyle.Italic)
        }
    }
}

@Composable
private fun CongratsBanner(message: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f))
            .border(1.dp, MaterialTheme.colorScheme.primary, RoundedCornerShape(16.dp))
            .padding(16.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Outlined.CheckCircleOutline, null, tint = MaterialTheme.colorScheme.secondary, modifier = Modifier.size(20.dp))
            Spacer(modifier = Modifier.width(12.dp))
            Text(message, color = BambooTextPrimary, fontFamily = PoppinsFontFamily, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
        }
    }
}

@Composable
private fun NoClassesCard() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(BambooSurface)
            .border(1.dp, BambooBorder, RoundedCornerShape(20.dp))
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("🌤️", fontSize = 40.sp)
            Spacer(modifier = Modifier.height(12.dp))
            Text("NO CLASSES TODAY", color = BambooTextPrimary, fontFamily = PoppinsFontFamily, fontWeight = FontWeight.Bold, fontSize = 18.sp)
            Text("You don't have any classes scheduled today.", color = BambooTextSecondary, fontSize = 13.sp, textAlign = TextAlign.Center)
        }
    }
}

@Composable
private fun HomeHolidayCard(holiday: OfficialHoliday) {
    val festiveIcon = when (holiday.category.lowercase()) {
        "festival" -> "🌸"
        "national" -> "🇮🇳"
        "religious" -> "🕌"
        else -> "🎉"
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .background(Brush.linearGradient(listOf(BambooDarkGreen, BambooBg)))
            .border(1.5.dp, BambooPrimaryGreen, RoundedCornerShape(24.dp))
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(festiveIcon, fontSize = 48.sp)
            Spacer(modifier = Modifier.height(12.dp))
            Text("HOLIDAY TODAY", color = BambooPrimaryGreen, fontFamily = PoppinsFontFamily, fontWeight = FontWeight.Bold, fontSize = 12.sp, letterSpacing = 2.sp)
            Text(holiday.name, color = BambooTextPrimary, fontFamily = PoppinsFontFamily, fontWeight = FontWeight.Bold, fontSize = 28.sp, textAlign = TextAlign.Center)
            Spacer(modifier = Modifier.height(12.dp))
            Text("No classes today due to ${holiday.name}.", color = BambooSoftGreen, fontSize = 15.sp, fontWeight = FontWeight.SemiBold, textAlign = TextAlign.Center)
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
private fun DoneForTheDayCard(studentName: String, quote: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .background(Brush.linearGradient(listOf(BambooPrimaryGreen, BambooSoftGreen)))
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("✅", fontSize = 40.sp)
            Spacer(modifier = Modifier.height(12.dp))
            Text("DONE FOR THE DAY", color = BambooBg, fontFamily = PoppinsFontFamily, fontWeight = FontWeight.Bold, fontSize = 20.sp)
            Text("You've completed all your classes today.", color = BambooBg.copy(alpha = 0.9f), fontSize = 14.sp)
            Spacer(modifier = Modifier.height(8.dp))
            Text("Great work, ${studentName.split(" ").first()}! 🎉", color = BambooBg, fontWeight = FontWeight.Bold, fontSize = 16.sp)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(BambooBg.copy(alpha = 0.2f))
                    .padding(12.dp)
            ) {
                Text("\"$quote\"", color = BambooBg, fontSize = 13.sp, fontStyle = androidx.compose.ui.text.font.FontStyle.Italic, textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth())
            }
        }
    }
}

@Composable
private fun UpcomingHolidayWidget(holiday: OfficialHoliday, daysLeft: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(BambooElevated)
            .border(1.dp, BambooBorder, RoundedCornerShape(16.dp))
            .padding(16.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(BambooDarkGreen),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.Schedule, null, tint = BambooPrimaryGreen, modifier = Modifier.size(20.dp))
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text("UPCOMING HOLIDAY", color = BambooTextMuted, fontSize = 10.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
                Text(holiday.name, color = BambooTextPrimary, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                Text(holiday.startDate, color = BambooSoftGreen, fontSize = 12.sp)
            }
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(BambooPrimaryGreen.copy(alpha = 0.2f))
                    .padding(horizontal = 10.dp, vertical = 6.dp)
            ) {
                Text(daysLeft, color = BambooPrimaryGreen, fontWeight = FontWeight.Bold, fontSize = 12.sp)
            }
        }
    }
}

private fun calculateDaysLeft(startDate: String): String {
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
            else -> "$days Days Left"
        }
    } catch (e: Exception) { "" }
}

@Composable
private fun HomeHeaderTopBar(
    unreadCount: Int,
    isSyncing: Boolean,
    onNotificationClick: () -> Unit
) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(modifier = Modifier.size(40.dp).clip(RoundedCornerShape(14.dp)).background(BambooElevated).border(1.dp, BambooBorder, RoundedCornerShape(14.dp)), contentAlignment = Alignment.Center) {
                if (isSyncing) {
                    CircularProgressIndicator(modifier = Modifier.size(18.dp), strokeWidth = 2.dp, color = BambooPrimaryGreen)
                } else {
                    Icon(Icons.Default.GridView, null, tint = BambooPrimaryGreen, modifier = Modifier.size(22.dp))
                }
            }
            Spacer(modifier = Modifier.width(14.dp))
            Text("BAMBOO", color = BambooSoftGreen, fontFamily = PoppinsFontFamily, fontWeight = FontWeight.Bold, fontSize = 20.sp, letterSpacing = 2.sp)
        }
        
        Box(modifier = Modifier.size(44.dp), contentAlignment = Alignment.Center) {
            IconButton(onClick = onNotificationClick) {
                BadgedBox(
                    badge = {
                        if (unreadCount > 0) {
                            Badge(
                                containerColor = Color.Red,
                                contentColor = Color.White
                            ) {
                                Text(unreadCount.toString())
                            }
                        }
                    }
                ) {
                    Icon(Icons.Default.Notifications, null, tint = BambooTextPrimary, modifier = Modifier.size(24.dp))
                }
            }
        }
    }
}

@Composable
fun HomeGreetingHeader(
    studentName: String,
    branch: String,
    studentYear: String = "1st Year",
    studentSection: com.example.data.TimetableSection? = null,
    dateFormatted: String,
    todayCount: Int
) {
    val secLabel = when (studentSection) {
        com.example.data.TimetableSection.SECTION_1 -> "SECTION A"
        com.example.data.TimetableSection.SECTION_2 -> "SECTION B"
        else -> "COMMON"
    }

    Column(modifier = Modifier.fillMaxWidth()) {
        Text(dateFormatted, color = BambooTextMuted, fontFamily = PoppinsFontFamily, fontWeight = FontWeight.Bold, fontSize = 10.sp, letterSpacing = 1.5.sp)
        Spacer(modifier = Modifier.height(16.dp))
        Text("WELCOME BACK,", color = BambooTextMuted, fontFamily = PoppinsFontFamily, fontWeight = FontWeight.Bold, fontSize = 12.sp, letterSpacing = 2.sp)
        Text(
            if (studentName.isNotBlank()) studentName.uppercase() else "STUDENT",
            color = MaterialTheme.colorScheme.primary,
            fontFamily = PoppinsFontFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 32.sp,
            lineHeight = 36.sp
        )
        Spacer(modifier = Modifier.height(6.dp))
        Text("$branch • ${studentYear.uppercase()} • $secLabel • $todayCount SESSIONS TODAY", color = BambooTextSecondary, fontFamily = PoppinsFontFamily, fontWeight = FontWeight.SemiBold, fontSize = 11.sp)
    }
}

@Composable
private fun HomeMetricsOverviewRow(todayCount: Int, attendanceStats: OverallAttendanceStats, onNavigateToSchedule: () -> Unit, onNavigateToAttendance: () -> Unit) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
        Box(modifier = Modifier.weight(1f).clip(RoundedCornerShape(20.dp)).background(BambooSurface).border(1.dp, BambooBorder, RoundedCornerShape(20.dp)).pressClick { onNavigateToSchedule() }.padding(16.dp)) {
            Column {
                Icon(Icons.Outlined.Book, null, tint = BambooPrimaryGreen, modifier = Modifier.size(18.dp))
                Spacer(Modifier.height(10.dp))
                Text("$todayCount", color = BambooTextPrimary, fontWeight = FontWeight.Bold, fontSize = 30.sp)
                Text("Today's Classes", color = BambooTextSecondary, fontSize = 11.sp)
            }
        }
        Box(modifier = Modifier.weight(1f).clip(RoundedCornerShape(20.dp)).background(BambooSurface).border(1.dp, BambooBorderHighlight, RoundedCornerShape(20.dp)).pressClick { onNavigateToAttendance() }.padding(16.dp)) {
            Column {
                Icon(Icons.Outlined.CheckCircleOutline, null, tint = BambooBrightGreen, modifier = Modifier.size(18.dp))
                Spacer(Modifier.height(10.dp))
                Text("${String.format(Locale.US, "%.0f", attendanceStats.percentage)}%", color = BambooTextPrimary, fontWeight = FontWeight.Bold, fontSize = 30.sp)
                Text(attendanceStats.statusStanding, color = BambooSoftGreen, fontSize = 10.sp, fontWeight = FontWeight.SemiBold)
            }
        }
    }
}

@Composable
private fun HomeCurrentClassSection(currentEntry: TimetableEntry, onClick: () -> Unit) {
    Column {
        Text("CURRENT CLASS", color = BambooTextPrimary, fontFamily = PoppinsFontFamily, fontWeight = FontWeight.Bold, fontSize = 16.sp, modifier = Modifier.padding(bottom = 8.dp))
        Box(modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(20.dp)).background(BambooElevated).border(1.dp, BambooPrimaryGreen.copy(alpha = 0.6f), RoundedCornerShape(20.dp)).pressClick { onClick() }.padding(18.dp)) {
            Column {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("HAPPENING NOW", color = BambooBrightGreen, fontWeight = FontWeight.Bold, fontSize = 10.sp)
                    Text(currentEntry.time, color = BambooSoftGreen, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                }
                Spacer(Modifier.height(10.dp))
                Text(currentEntry.subjectTitle, color = BambooTextPrimary, fontWeight = FontWeight.Bold, fontSize = 20.sp)
                Text("${currentEntry.subjectCode} • ${currentEntry.room}", color = BambooTextSecondary, fontSize = 12.sp)
            }
        }
    }
}

@Composable
private fun HomeNextUpSection(nextEntry: TimetableEntry, minutesLeft: Int?, onClick: () -> Unit) {
    Column {
        Text("NEXT UP", color = BambooTextPrimary, fontFamily = PoppinsFontFamily, fontWeight = FontWeight.Bold, fontSize = 16.sp, modifier = Modifier.padding(bottom = 8.dp))
        Box(modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(20.dp)).background(BambooElevated).border(1.dp, BambooBorderHighlight, RoundedCornerShape(20.dp)).pressClick { onClick() }.padding(18.dp)) {
            Column {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text(nextEntry.time, color = BambooSoftGreen, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                    if (minutesLeft != null) Text("Starts in ${minutesLeft}m", color = BambooTextPrimary, fontWeight = FontWeight.Bold, fontSize = 11.sp)
                }
                Spacer(Modifier.height(10.dp))
                Text(nextEntry.subjectTitle, color = BambooTextPrimary, fontWeight = FontWeight.Bold, fontSize = 20.sp)
                Text("${nextEntry.room} • ${nextEntry.teacherName}", color = BambooTextSecondary, fontSize = 12.sp)
            }
        }
    }
}

@Composable
private fun HomeTimelineItem(entry: TimetableEntry, isCurrent: Boolean, isLast: Boolean, onClick: () -> Unit) {
    Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.Top) {
        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.width(24.dp)) {
            Box(modifier = Modifier.size(if (isCurrent) 12.dp else 8.dp).clip(CircleShape).background(if (isCurrent) BambooPrimaryGreen else BambooTextMuted))
            if (!isLast) Box(modifier = Modifier.width(1.dp).height(64.dp).background(BambooBorder))
        }
        Spacer(Modifier.width(10.dp))
        Box(modifier = Modifier.weight(1f).clip(RoundedCornerShape(16.dp)).background(if (isCurrent) BambooElevated else BambooSurface).border(1.dp, if (isCurrent) BambooPrimaryGreen else BambooBorder, RoundedCornerShape(16.dp)).pressClick { onClick() }.padding(14.dp)) {
            Column {
                Text(entry.time, color = BambooSoftGreen, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                Text(entry.subjectTitle, color = BambooTextPrimary, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                Text("${entry.room} • ${entry.teacherName}", color = BambooTextMuted, fontSize = 11.sp)
            }
        }
    }
}
