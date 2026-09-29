package com.example.ui.screens.academics

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.BeachAccess
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.PieChart
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.Assignment
import com.example.data.Exam
import com.example.data.OfficialHoliday
import com.example.data.TimetableEntry
import com.example.ui.components.pressClick
import com.example.ui.state.AcademicsTab
import com.example.ui.theme.BambooBg
import com.example.ui.theme.BambooBorder
import com.example.ui.theme.BambooDarkGreen
import com.example.ui.theme.BambooElevated
import com.example.ui.theme.BambooPrimaryGreen
import com.example.ui.theme.BambooSoftGreen
import com.example.ui.theme.BambooSurface
import com.example.ui.theme.BambooTextMuted
import com.example.ui.theme.BambooTextPrimary
import com.example.ui.theme.BambooTextSecondary
import com.example.ui.theme.PoppinsFontFamily

@Composable
fun AcademicHubOverview(
    nextClass: TimetableEntry?,
    nextAssignment: Assignment?,
    nextExam: Exam?,
    nextHoliday: OfficialHoliday?,
    attendancePercentage: Float,
    onNavigateTab: (AcademicsTab) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(BambooBg)
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "ACADEMIC HUB",
                    color = BambooTextPrimary,
                    fontFamily = PoppinsFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 22.sp,
                    letterSpacing = 1.sp
                )
                Text(
                    text = "Central Command Center for your College Life",
                    color = BambooTextSecondary,
                    fontFamily = PoppinsFontFamily,
                    fontSize = 12.sp
                )
            }

            // Quick Category Launcher Grid
            item {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        QuickTabCard(
                            title = "TIMETABLE",
                            subtitle = "Daily Schedule",
                            icon = Icons.Default.Schedule,
                            onClick = { onNavigateTab(AcademicsTab.TIMETABLE) },
                            modifier = Modifier.weight(1f)
                        )
                        QuickTabCard(
                            title = "ATTENDANCE",
                            subtitle = "${attendancePercentage.toInt()}% Overall",
                            icon = Icons.Default.PieChart,
                            onClick = { onNavigateTab(AcademicsTab.ATTENDANCE) },
                            modifier = Modifier.weight(1f)
                        )
                    }
                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        QuickTabCard(
                            title = "ASSIGNMENTS",
                            subtitle = if (nextAssignment != null) "1 Due Soon" else "Up To Date",
                            icon = Icons.Default.Assignment,
                            onClick = { onNavigateTab(AcademicsTab.ASSIGNMENTS) },
                            modifier = Modifier.weight(1f)
                        )
                        QuickTabCard(
                            title = "EXAMS",
                            subtitle = if (nextExam != null) nextExam.date else "No Upcoming",
                            icon = Icons.Default.Event,
                            onClick = { onNavigateTab(AcademicsTab.EXAMS) },
                            modifier = Modifier.weight(1f)
                        )
                    }
                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        QuickTabCard(
                            title = "HOLIDAYS",
                            subtitle = if (nextHoliday != null) nextHoliday.name else "No Upcoming",
                            icon = Icons.Default.BeachAccess,
                            onClick = { onNavigateTab(AcademicsTab.HOLIDAYS) },
                            modifier = Modifier.weight(1f)
                        )
                        QuickTabCard(
                            title = "SYLLABUS",
                            subtitle = "Course Outlines",
                            icon = Icons.Default.Book,
                            onClick = { onNavigateTab(AcademicsTab.SYLLABUS) },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }

            // Highlights
            item {
                Text(
                    text = "UPCOMING HIGHLIGHTS",
                    color = BambooPrimaryGreen,
                    fontFamily = PoppinsFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp,
                    letterSpacing = 1.sp,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            // Next Class Highlight
            item {
                HighlightCard(
                    category = "NEXT CLASS",
                    title = nextClass?.subjectTitle ?: "No more classes today",
                    subtitle = if (nextClass != null) "${nextClass.period} • Room ${nextClass.room}" else "You're done for the day!",
                    icon = Icons.Default.Schedule,
                    onClick = { onNavigateTab(AcademicsTab.TIMETABLE) }
                )
            }

            // Next Assignment Highlight
            item {
                HighlightCard(
                    category = "NEXT ASSIGNMENT",
                    title = nextAssignment?.title ?: "All assignments completed",
                    subtitle = if (nextAssignment != null) "${nextAssignment.subject} • Due ${nextAssignment.dueDate}" else "Great job staying ahead!",
                    icon = Icons.Default.Assignment,
                    onClick = { onNavigateTab(AcademicsTab.ASSIGNMENTS) }
                )
            }

            // Next Exam Highlight
            item {
                HighlightCard(
                    category = "UPCOMING EXAM",
                    title = nextExam?.subject ?: "No exams scheduled",
                    subtitle = if (nextExam != null) "${nextExam.examType.label} • ${nextExam.date} at ${nextExam.startTime}" else "No upcoming exam dates",
                    icon = Icons.Default.Event,
                    onClick = { onNavigateTab(AcademicsTab.EXAMS) }
                )
            }

            item {
                Spacer(modifier = Modifier.height(100.dp))
            }
        }
    }
}

@Composable
private fun QuickTabCard(
    title: String,
    subtitle: String,
    icon: ImageVector,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(BambooSurface)
            .border(1.dp, BambooBorder, RoundedCornerShape(16.dp))
            .pressClick { onClick() }
            .padding(14.dp)
    ) {
        Column {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = BambooPrimaryGreen,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = title,
                color = BambooTextPrimary,
                fontFamily = PoppinsFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp
            )
            Text(
                text = subtitle,
                color = BambooTextMuted,
                fontFamily = PoppinsFontFamily,
                fontSize = 11.sp
            )
        }
    }
}

@Composable
private fun HighlightCard(
    category: String,
    title: String,
    subtitle: String,
    icon: ImageVector,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .background(BambooSurface)
            .border(1.dp, BambooBorder, RoundedCornerShape(18.dp))
            .pressClick { onClick() }
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier.weight(1f),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(BambooElevated)
                        .padding(10.dp)
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = BambooPrimaryGreen,
                        modifier = Modifier.size(20.dp)
                    )
                }

                Spacer(modifier = Modifier.width(14.dp))

                Column {
                    Text(
                        text = category,
                        color = BambooSoftGreen,
                        fontFamily = PoppinsFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 10.sp,
                        letterSpacing = 1.sp
                    )
                    Text(
                        text = title,
                        color = BambooTextPrimary,
                        fontFamily = PoppinsFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                    Text(
                        text = subtitle,
                        color = BambooTextMuted,
                        fontFamily = PoppinsFontFamily,
                        fontSize = 12.sp
                    )
                }
            }

            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = null,
                tint = BambooTextMuted
            )
        }
    }
}
