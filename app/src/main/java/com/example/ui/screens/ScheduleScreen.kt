package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.AcademicData
import com.example.data.TimetableEntry
import com.example.ui.components.pressClick
import com.example.ui.state.ClassSession
import com.example.ui.state.SessionStatus
import com.example.ui.theme.*

@Composable
fun ScheduleScreen(
    selectedDay: String,
    selectedBranch: String,
    studentSection: com.example.data.TimetableSection?,
    onDaySelected: (String) -> Unit,
    onBranchSelected: (String) -> Unit,
    onSelectSession: (ClassSession) -> Unit,
    modifier: Modifier = Modifier
) {
    var isBranchDropdownExpanded by remember { mutableStateOf(false) }
    val daysList = listOf("MONDAY", "TUESDAY", "WEDNESDAY", "THURSDAY", "FRIDAY", "SATURDAY")

    val dayEntries = remember(selectedBranch, selectedDay, studentSection) {
        val allEntries = AcademicData.TIMETABLE[selectedBranch]?.get(selectedDay) ?: emptyList()
        AcademicData.getFilteredTimetable(allEntries, studentSection)
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
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Spacer(modifier = Modifier.height(16.dp))
                Text("ACADEMIC TIMETABLE", color = BambooTextPrimary, fontFamily = PoppinsFontFamily, fontWeight = FontWeight.Bold, fontSize = 22.sp, letterSpacing = 1.5.sp)
                Text("IET B.TECH 2026-27 • SEMESTER 1", color = BambooTextSecondary, fontFamily = PoppinsFontFamily, fontWeight = FontWeight.SemiBold, fontSize = 11.sp, letterSpacing = 1.sp)
            }

            item {
                Box {
                    Row(
                        modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(16.dp)).background(BambooSurface).border(1.dp, BambooBorder, RoundedCornerShape(16.dp)).pressClick { isBranchDropdownExpanded = true }.padding(horizontal = 16.dp, vertical = 14.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text("SELECT BRANCH", color = BambooTextMuted, fontFamily = PoppinsFontFamily, fontWeight = FontWeight.Bold, fontSize = 10.sp)
                            Text(selectedBranch, color = BambooTextPrimary, fontFamily = PoppinsFontFamily, fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
                        }
                        Icon(Icons.Default.ArrowDropDown, null, tint = MaterialTheme.colorScheme.primary)
                    }

                    DropdownMenu(expanded = isBranchDropdownExpanded, onDismissRequest = { isBranchDropdownExpanded = false }, modifier = Modifier.background(BambooElevated)) {
                        AcademicData.BRANCHES.forEach { branch ->
                            DropdownMenuItem(
                                text = { Text(branch.code, color = BambooTextPrimary) },
                                onClick = {
                                    onBranchSelected(branch.code)
                                    isBranchDropdownExpanded = false
                                }
                            )
                        }
                    }
                }
            }

            item {
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(daysList) { dayName ->
                        val isSelected = dayName == selectedDay
                        Box(
                            modifier = Modifier.clip(RoundedCornerShape(14.dp)).background(if (isSelected) BambooElevated else BambooSurface).border(1.dp, if (isSelected) BambooPrimaryGreen else BambooBorder, RoundedCornerShape(14.dp)).pressClick { onDaySelected(dayName) }.padding(horizontal = 14.dp, vertical = 10.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(dayName, color = if (isSelected) BambooSoftGreen else BambooTextMuted, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                        }
                    }
                }
            }

            if (studentSection == null) {
                item {
                    Box(modifier = Modifier.fillMaxWidth().padding(vertical = 32.dp).clip(RoundedCornerShape(16.dp)).background(BambooElevated).border(1.dp, BambooPrimaryGreen, RoundedCornerShape(16.dp)).padding(24.dp), contentAlignment = Alignment.Center) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("⚠️ SECTION NOT SELECTED", color = BambooPrimaryGreen, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                            Spacer(Modifier.height(8.dp))
                            Text("Select your section in Profile settings to view your complete timetable.", color = BambooTextSecondary, textAlign = androidx.compose.ui.text.style.TextAlign.Center, fontSize = 13.sp)
                        }
                    }
                }
            } else if (dayEntries.isEmpty()) {
                item {
                    Box(modifier = Modifier.fillMaxWidth().padding(vertical = 32.dp).clip(RoundedCornerShape(16.dp)).background(BambooSurface).padding(24.dp), contentAlignment = Alignment.Center) {
                        Text("NO CLASSES SCHEDULED", color = BambooTextMuted, fontSize = 13.sp)
                    }
                }
            } else {
                items(dayEntries) { entry ->
                    TimetableEntryCard(
                        entry = entry,
                        onClick = {
                            onSelectSession(ClassSession(
                                id = "${entry.subjectCode}_${entry.period}",
                                time = entry.time,
                                subject = entry.subjectTitle,
                                courseCode = entry.subjectCode,
                                room = entry.room,
                                teacher = entry.teacherName,
                                status = SessionStatus.UPCOMING
                            ))
                        }
                    )
                }
            }

            item { Spacer(modifier = Modifier.height(100.dp)) }
        }
    }
}

@Composable
private fun TimetableEntryCard(entry: TimetableEntry, onClick: () -> Unit) {
    Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.Top) {
        Column(modifier = Modifier.width(80.dp).padding(top = 10.dp)) {
            Text("PERIOD ${entry.period}", color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold, fontSize = 11.sp)
            Text(entry.time, color = BambooTextMuted, fontSize = 10.sp)
        }
        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.width(20.dp)) {
            Box(modifier = Modifier.padding(top = 14.dp).size(6.dp).clip(CircleShape).background(MaterialTheme.colorScheme.primary))
            Box(modifier = Modifier.width(1.dp).height(100.dp).background(BambooBorder))
        }
        Spacer(modifier = Modifier.width(8.dp))
        Box(modifier = Modifier.weight(1f).clip(RoundedCornerShape(20.dp)).background(BambooSurface).border(1.dp, BambooBorder, RoundedCornerShape(20.dp)).pressClick { onClick() }.padding(16.dp)) {
            Column {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text(entry.subjectCode, color = BambooSoftGreen, fontWeight = FontWeight.Bold, fontSize = 10.sp)
                    Text(entry.type.uppercase(), color = BambooBrightGreen, fontWeight = FontWeight.Bold, fontSize = 9.sp)
                }
                Spacer(modifier = Modifier.height(10.dp))
                Text(entry.subjectTitle, color = BambooTextPrimary, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                Spacer(modifier = Modifier.height(12.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.LocationOn, null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(entry.room, color = MaterialTheme.colorScheme.secondary, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                }
            }
        }
    }
}
