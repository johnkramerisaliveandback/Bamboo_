package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*

@Composable
fun AppGuideScreen(
    onBack: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BambooBg)
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(18.dp)
        ) {
            item {
                Spacer(modifier = Modifier.height(16.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = BambooTextPrimary
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text(
                            text = "APP GUIDE",
                            color = BambooTextPrimary,
                            fontFamily = PoppinsFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp,
                            letterSpacing = 1.2.sp
                        )
                        Text(
                            text = "GETTING STARTED WITH ACADEMIC OS",
                            color = BambooTextSecondary,
                            fontFamily = PoppinsFontFamily,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 10.sp,
                            letterSpacing = 1.sp
                        )
                    }
                }
            }

            // Current Scope Banner
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(20.dp))
                        .background(BambooElevated)
                        .border(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.5f), RoundedCornerShape(20.dp))
                        .padding(20.dp)
                ) {
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Info,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "CURRENT RELEASE SCOPE",
                                color = MaterialTheme.colorScheme.primary,
                                fontFamily = PoppinsFontFamily,
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp,
                                letterSpacing = 1.2.sp
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = "Academic OS is currently designed primarily for B.Tech 1st Year students. It focuses on core academic productivity: dynamic attendance tracking, daily timetables, common 1st year syllabus, local notes & document viewing, and deadline reminders.",
                            color = BambooTextSecondary,
                            fontFamily = PoppinsFontFamily,
                            fontSize = 12.sp,
                            lineHeight = 18.sp
                        )
                    }
                }
            }

            // Step 1
            item {
                GuideStepCard(
                    stepNumber = "STEP 1",
                    title = "Set Up Your Profile",
                    description = "Enter your Name, Branch (e.g., CSE AI), Year (1st Year), and Section (Section A or B). Changing your profile dynamically updates your timetable, subjects, and syllabus instantly.",
                    icon = Icons.Default.Person
                )
            }

            // Step 2
            item {
                GuideStepCard(
                    stepNumber = "STEP 2",
                    title = "Check Attendance & Targets",
                    description = "Track attended and total classes per subject. Academic OS calculates your exact percentage using (Attended ÷ Total) × 100 and tells you how many classes you must attend or can skip.",
                    icon = Icons.Default.CheckCircle
                )
            }

            // Step 3
            item {
                GuideStepCard(
                    stepNumber = "STEP 3",
                    title = "Check Today's Schedule",
                    description = "The app automatically selects today's day of the week on launch. View current lectures, upcoming labs, room numbers, and faculty names tailored to your section.",
                    icon = Icons.Default.CalendarMonth
                )
            }

            // Step 4
            item {
                GuideStepCard(
                    stepNumber = "STEP 4",
                    title = "Import & View Local Notes",
                    description = "Upload lecture PDFs, PNGs, and JPGs directly into your notes. Files are imported locally into Academic OS storage so you can open them anytime offline.",
                    icon = Icons.Default.Description
                )
            }

            // Step 5
            item {
                GuideStepCard(
                    stepNumber = "STEP 5",
                    title = "Track Assignments & Deadlines",
                    description = "Add coursework, lab reports, and project deadlines with due times. Receive local Android notifications before assignments are due.",
                    icon = Icons.Default.Assignment
                )
            }

            // Step 6
            item {
                GuideStepCard(
                    stepNumber = "STEP 6",
                    title = "Device-Local Privacy",
                    description = "All your data is saved 100% locally on your phone. No logins, passwords, or cloud servers are used. Installing the app on a new phone starts fresh.",
                    icon = Icons.Default.Security
                )
            }

            // What's Next
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(20.dp))
                        .background(BambooSurface)
                        .border(1.dp, BambooBorder, RoundedCornerShape(20.dp))
                        .padding(20.dp)
                ) {
                    Column {
                        Text(
                            text = "WHAT'S NEXT?",
                            color = BambooTextPrimary,
                            fontFamily = PoppinsFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            letterSpacing = 1.sp
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "Academic OS is continuously evolving. Additional academic tools and features will be added in future updates.",
                            color = BambooTextSecondary,
                            fontFamily = PoppinsFontFamily,
                            fontSize = 12.sp,
                            lineHeight = 18.sp
                        )
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(80.dp))
            }
        }
    }
}

@Composable
private fun GuideStepCard(
    stepNumber: String,
    title: String,
    description: String,
    icon: ImageVector
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(BambooSurface)
            .border(1.dp, BambooBorder, RoundedCornerShape(20.dp))
            .padding(20.dp)
    ) {
        Row(verticalAlignment = Alignment.Top) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(22.dp)
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = stepNumber,
                    color = MaterialTheme.colorScheme.primary,
                    fontFamily = PoppinsFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 10.sp,
                    letterSpacing = 1.5.sp
                )
                Text(
                    text = title,
                    color = BambooTextPrimary,
                    fontFamily = PoppinsFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = description,
                    color = BambooTextSecondary,
                    fontFamily = PoppinsFontFamily,
                    fontSize = 12.sp,
                    lineHeight = 18.sp
                )
            }
        }
    }
}
