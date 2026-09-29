package com.example.ui.screens

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.automirrored.filled.HelpOutline
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.core.ui.ComingSoonCard
import com.example.data.AcademicData
import com.example.data.NotificationSettings
import com.example.data.TimetableSection
import com.example.ui.components.pressClick
import com.example.ui.state.BambooThemeColor
import com.example.ui.state.GradientIntensity
import com.example.ui.theme.*

@Composable
fun MoreScreen(
    studentName: String,
    branch: String,
    studentYear: String,
    studentSection: TimetableSection?,
    notificationSettings: NotificationSettings,
    themeColor: BambooThemeColor,
    gradientIntensity: GradientIntensity,
    hapticsEnabled: Boolean,
    isNotificationPermissionGranted: Boolean,
    isBatteryOptimizationEnabled: Boolean,
    onSaveProfile: (name: String, branch: String, year: String, section: TimetableSection) -> Unit,
    onResetOnboarding: () -> Unit,
    onUpdateNotificationSettings: (NotificationSettings) -> Unit,
    onUpdateThemeColor: (BambooThemeColor) -> Unit,
    onUpdateGradientIntensity: (GradientIntensity) -> Unit,
    onOpenAppGuide: () -> Unit,
    onOpenAbout: () -> Unit,
    onShowComingSoon: (String) -> Unit,
    onTestNotification: () -> Unit,
    onOpenAppSettings: () -> Unit,
    onToggleHaptics: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    var nameInput by remember { mutableStateOf(studentName) }
    var selectedBranchInput by remember { mutableStateOf(branch) }
    var selectedYearInput by remember { mutableStateOf(studentYear) }
    var selectedSectionInput by remember { mutableStateOf(studentSection ?: TimetableSection.SECTION_1) }

    var isBranchDropdownExpanded by remember { mutableStateOf(false) }
    var isYearDropdownExpanded by remember { mutableStateOf(false) }
    var isSectionDropdownExpanded by remember { mutableStateOf(false) }
    var showResetDialog by remember { mutableStateOf(false) }

    val branches = remember { AcademicData.BRANCHES.map { it.code } }
    val years = remember { listOf("1st Year", "2nd Year", "3rd Year", "4th Year") }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(BambooBg)
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            item {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "SETTINGS & PROFILE",
                    color = BambooTextPrimary,
                    fontFamily = PoppinsFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 22.sp,
                    letterSpacing = 1.5.sp
                )
                Text(
                    text = "STUDENT PROFILE • LOCAL DEVICE CONFIGURATION",
                    color = BambooTextSecondary,
                    fontFamily = PoppinsFontFamily,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 11.sp,
                    letterSpacing = 1.sp
                )
            }

            // Student Profile Section
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
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Person, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(20.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "STUDENT PROFILE",
                                color = BambooTextPrimary,
                                fontFamily = PoppinsFontFamily,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                                letterSpacing = 1.sp
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Student Name
                        Text(
                            text = "NAME",
                            color = BambooTextMuted,
                            fontFamily = PoppinsFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 10.sp,
                            letterSpacing = 1.sp,
                            modifier = Modifier.padding(bottom = 6.dp)
                        )

                        OutlinedTextField(
                            value = nameInput,
                            onValueChange = { nameInput = it },
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("more_name_input"),
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedContainerColor = BambooElevated,
                                unfocusedContainerColor = BambooElevated,
                                focusedBorderColor = MaterialTheme.colorScheme.primary,
                                unfocusedBorderColor = BambooBorder,
                                focusedTextColor = BambooTextPrimary,
                                unfocusedTextColor = BambooTextPrimary
                            ),
                            shape = RoundedCornerShape(12.dp)
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        // Branch
                        Text(
                            text = "BRANCH / COURSE",
                            color = BambooTextMuted,
                            fontFamily = PoppinsFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 10.sp,
                            letterSpacing = 1.sp,
                            modifier = Modifier.padding(bottom = 6.dp)
                        )

                        Box {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(BambooElevated)
                                    .border(1.dp, BambooBorder, RoundedCornerShape(12.dp))
                                    .pressClick { isBranchDropdownExpanded = true }
                                    .padding(horizontal = 14.dp, vertical = 14.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = selectedBranchInput,
                                    color = BambooTextPrimary,
                                    fontFamily = PoppinsFontFamily,
                                    fontSize = 14.sp
                                )
                                Icon(Icons.Default.ArrowDropDown, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                            }

                            DropdownMenu(
                                expanded = isBranchDropdownExpanded,
                                onDismissRequest = { isBranchDropdownExpanded = false },
                                modifier = Modifier.background(BambooElevated)
                            ) {
                                branches.forEach { b ->
                                    DropdownMenuItem(
                                        text = { Text(b, color = BambooTextPrimary, fontFamily = PoppinsFontFamily) },
                                        onClick = {
                                            selectedBranchInput = b
                                            isBranchDropdownExpanded = false
                                        }
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        // Year
                        Text(
                            text = "YEAR",
                            color = BambooTextMuted,
                            fontFamily = PoppinsFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 10.sp,
                            letterSpacing = 1.sp,
                            modifier = Modifier.padding(bottom = 6.dp)
                        )

                        Box {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(BambooElevated)
                                    .border(1.dp, BambooBorder, RoundedCornerShape(12.dp))
                                    .clickable { isYearDropdownExpanded = true }
                                    .padding(horizontal = 14.dp, vertical = 14.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = selectedYearInput,
                                    color = BambooTextPrimary,
                                    fontFamily = PoppinsFontFamily,
                                    fontSize = 14.sp
                                )
                                Icon(Icons.Default.ArrowDropDown, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                            }

                            DropdownMenu(
                                expanded = isYearDropdownExpanded,
                                onDismissRequest = { isYearDropdownExpanded = false },
                                modifier = Modifier.background(BambooElevated)
                            ) {
                                years.forEach { yr ->
                                    DropdownMenuItem(
                                        text = { Text(yr, color = BambooTextPrimary, fontFamily = PoppinsFontFamily) },
                                        onClick = {
                                            selectedYearInput = yr
                                            isYearDropdownExpanded = false
                                        }
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        // Section
                        Text(
                            text = "SECTION",
                            color = BambooTextMuted,
                            fontFamily = PoppinsFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 10.sp,
                            letterSpacing = 1.sp,
                            modifier = Modifier.padding(bottom = 6.dp)
                        )

                        Box {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(BambooElevated)
                                    .border(1.dp, BambooBorder, RoundedCornerShape(12.dp))
                                    .clickable { isSectionDropdownExpanded = true }
                                    .padding(horizontal = 16.dp, vertical = 12.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                val sectionText = when (selectedSectionInput) {
                                    TimetableSection.SECTION_1 -> "Section A"
                                    TimetableSection.SECTION_2 -> "Section B"
                                    TimetableSection.COMMON -> "Common"
                                }
                                Text(
                                    text = sectionText,
                                    color = BambooTextPrimary,
                                    fontFamily = PoppinsFontFamily,
                                    fontSize = 14.sp
                                )
                                Icon(Icons.Default.ArrowDropDown, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                            }

                            DropdownMenu(
                                expanded = isSectionDropdownExpanded,
                                onDismissRequest = { isSectionDropdownExpanded = false },
                                modifier = Modifier.background(BambooElevated)
                            ) {
                                DropdownMenuItem(
                                    text = { Text("Section A", color = BambooTextPrimary, fontFamily = PoppinsFontFamily) },
                                    onClick = {
                                        selectedSectionInput = TimetableSection.SECTION_1
                                        isSectionDropdownExpanded = false
                                    }
                                )
                                DropdownMenuItem(
                                    text = { Text("Section B", color = BambooTextPrimary, fontFamily = PoppinsFontFamily) },
                                    onClick = {
                                        selectedSectionInput = TimetableSection.SECTION_2
                                        isSectionDropdownExpanded = false
                                    }
                                )
                                DropdownMenuItem(
                                    text = { Text("Common", color = BambooTextPrimary, fontFamily = PoppinsFontFamily) },
                                    onClick = {
                                        selectedSectionInput = TimetableSection.COMMON
                                        isSectionDropdownExpanded = false
                                    }
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(18.dp))

                        Button(
                            onClick = { onSaveProfile(nameInput, selectedBranchInput, selectedYearInput, selectedSectionInput) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(46.dp)
                                .testTag("save_profile_button"),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.primary,
                                contentColor = BambooBg
                            ),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text(
                                text = "SAVE PROFILE",
                                fontFamily = PoppinsFontFamily,
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp,
                                letterSpacing = 1.sp
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        OutlinedButton(
                            onClick = { showResetDialog = true },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, BambooAbsentRed.copy(alpha = 0.6f))
                        ) {
                            Icon(
                                imageVector = Icons.Default.RestartAlt,
                                contentDescription = null,
                                tint = BambooAbsentRed,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "RESET PROFILE",
                                color = BambooAbsentRed,
                                fontFamily = PoppinsFontFamily,
                                fontWeight = FontWeight.Bold,
                                fontSize = 11.sp
                            )
                        }
                    }
                }
            }

            // Appearance: Accent & Gradients
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
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Palette, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(20.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "ACCENT & GRADIENTS",
                                color = BambooTextPrimary,
                                fontFamily = PoppinsFontFamily,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                                letterSpacing = 1.sp
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = "ACCENT COLOR PRESETS",
                            color = BambooTextMuted,
                            fontFamily = PoppinsFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 10.sp,
                            letterSpacing = 1.sp,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            BambooThemeColor.entries.forEach { color ->
                                val colorValue = when (color) {
                                    BambooThemeColor.GREEN -> BambooPrimaryGreen
                                    BambooThemeColor.PURPLE -> PurplePrimary
                                    BambooThemeColor.BLUE -> BluePrimary
                                    BambooThemeColor.CYAN -> CyanPrimary
                                    BambooThemeColor.RED -> RedPrimary
                                    BambooThemeColor.PINK -> PinkPrimary
                                    BambooThemeColor.ORANGE -> OrangePrimary
                                }
                                Box(
                                    modifier = Modifier
                                        .size(38.dp)
                                        .clip(CircleShape)
                                        .background(colorValue)
                                        .border(
                                            width = if (themeColor == color) 3.dp else 0.dp,
                                            color = BambooTextPrimary,
                                            shape = CircleShape
                                        )
                                        .pressClick { onUpdateThemeColor(color) }
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = "GRADIENT INTENSITY",
                            color = BambooTextMuted,
                            fontFamily = PoppinsFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 10.sp,
                            letterSpacing = 1.sp,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            GradientIntensity.entries.forEach { intensity ->
                                val isSelected = gradientIntensity == intensity
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .clip(RoundedCornerShape(10.dp))
                                        .background(if (isSelected) MaterialTheme.colorScheme.primary else BambooElevated)
                                        .border(
                                            1.dp,
                                            if (isSelected) MaterialTheme.colorScheme.secondary else BambooBorder,
                                            RoundedCornerShape(10.dp)
                                        )
                                        .pressClick { onUpdateGradientIntensity(intensity) }
                                        .padding(vertical = 10.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = intensity.label.uppercase(),
                                        color = if (isSelected) BambooBg else BambooTextSecondary,
                                        fontFamily = PoppinsFontFamily,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 11.sp
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Parental Controls (Coming Soon)
            item {
                ComingSoonCard(
                    featureName = "Parental Controls",
                    description = "Family access and sharing permissions.",
                    onClick = { onShowComingSoon("Parental Controls") }
                )
            }

            // App Guide & Instructions Links
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(20.dp))
                        .background(BambooSurface)
                        .border(1.dp, BambooBorder, RoundedCornerShape(20.dp))
                        .padding(20.dp)
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.AutoMirrored.Filled.HelpOutline, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(20.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "GUIDE & DOCUMENTATION",
                                color = BambooTextPrimary,
                                fontFamily = PoppinsFontFamily,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                                letterSpacing = 1.sp
                            )
                        }

                        Spacer(modifier = Modifier.height(4.dp))

                        Button(
                            onClick = onOpenAppGuide,
                            modifier = Modifier.fillMaxWidth().height(46.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = BambooElevated, contentColor = BambooSoftGreen),
                            shape = RoundedCornerShape(12.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, BambooBorder)
                        ) {
                            Icon(Icons.Default.MenuBook, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("OPEN APP GUIDE", fontFamily = PoppinsFontFamily, fontWeight = FontWeight.Bold, fontSize = 11.sp)
                        }
                    }
                }
            }

            // Settings -> Instructions (Accordion)
            item {
                InstructionsSection()
            }

            // Central Notification Settings Section
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
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Notifications, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(20.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "NOTIFICATION ENGINE",
                                color = BambooTextPrimary,
                                fontFamily = PoppinsFontFamily,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                                letterSpacing = 1.sp
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        NotificationToggleRow(
                            label = "Class Schedule Alerts",
                            checked = notificationSettings.classesEnabled,
                            onCheckedChange = {
                                onUpdateNotificationSettings(notificationSettings.copy(classesEnabled = it))
                            }
                        )

                        NotificationToggleRow(
                            label = "Assignment Due Reminders",
                            checked = notificationSettings.assignmentsEnabled,
                            onCheckedChange = {
                                onUpdateNotificationSettings(notificationSettings.copy(assignmentsEnabled = it))
                            }
                        )

                        NotificationToggleRow(
                            label = "Exam Countdown Alerts",
                            checked = notificationSettings.examsEnabled,
                            onCheckedChange = {
                                onUpdateNotificationSettings(notificationSettings.copy(examsEnabled = it))
                            }
                        )

                        NotificationToggleRow(
                            label = "College Holiday Notifications",
                            checked = notificationSettings.holidaysEnabled,
                            onCheckedChange = {
                                onUpdateNotificationSettings(notificationSettings.copy(holidaysEnabled = it))
                            }
                        )
                    }
                }
            }

            // Sound & Feedback Section
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
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.AutoMirrored.Filled.VolumeUp, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(20.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "SYSTEM FEEDBACK",
                                color = BambooTextPrimary,
                                fontFamily = PoppinsFontFamily,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                                letterSpacing = 1.sp
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        NotificationToggleRow(
                            label = "Haptic Vibration",
                            checked = hapticsEnabled,
                            onCheckedChange = onToggleHaptics
                        )
                    }
                }
            }

            // About Section Link
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(20.dp))
                        .background(BambooSurface)
                        .border(1.dp, BambooBorder, RoundedCornerShape(20.dp))
                        .clickable(onClick = onOpenAbout)
                        .padding(22.dp)
                ) {
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Info, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(20.dp))
                                Spacer(modifier = Modifier.width(10.dp))
                                Text(
                                    text = "ABOUT ACADEMIC OS",
                                    color = BambooTextPrimary,
                                    fontFamily = PoppinsFontFamily,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp,
                                    letterSpacing = 1.sp
                                )
                            }
                            Icon(Icons.Default.ChevronRight, contentDescription = null, tint = BambooTextMuted)
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Text("ACADEMIC OS • BAMBOO", color = MaterialTheme.colorScheme.tertiary, fontFamily = PoppinsFontFamily, fontWeight = FontWeight.Bold, fontSize = 14.sp, letterSpacing = 1.sp)
                        Text("RELEASE IDENTITY • BAMBOO", color = BambooTextMuted, fontFamily = PoppinsFontFamily, fontWeight = FontWeight.Bold, fontSize = 10.sp, letterSpacing = 1.sp)
                    }
                }
            }

            // Footer Branding directly at the bottom of Settings page
            item {
                Spacer(modifier = Modifier.height(8.dp))
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "made by johnkramerisback",
                        color = BambooTextMuted.copy(alpha = 0.6f),
                        fontFamily = PoppinsFontFamily,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium,
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "we shud always be humble",
                        color = BambooTextMuted.copy(alpha = 0.4f),
                        fontFamily = PoppinsFontFamily,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Normal,
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                    )
                }
            }

            item {
                Spacer(modifier = Modifier.height(100.dp))
            }
        }
    }

    if (showResetDialog) {
        AlertDialog(
            onDismissRequest = { showResetDialog = false },
            containerColor = BambooSurface,
            title = {
                Text(
                    text = "RESET PROFILE & DATA?",
                    color = BambooTextPrimary,
                    fontFamily = PoppinsFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            },
            text = {
                Text(
                    text = "This will clear your local student profile and attendance records, returning you to the student setup screen.",
                    color = BambooTextSecondary,
                    fontFamily = PoppinsFontFamily,
                    fontSize = 13.sp
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        showResetDialog = false
                        onResetOnboarding()
                    }
                ) {
                    Text(
                        text = "RESET",
                        color = BambooAbsentRed,
                        fontFamily = PoppinsFontFamily,
                        fontWeight = FontWeight.Bold
                    )
                }
            },
            dismissButton = {
                TextButton(onClick = { showResetDialog = false }) {
                    Text(
                        text = "CANCEL",
                        color = BambooTextMuted,
                        fontFamily = PoppinsFontFamily
                    )
                }
            }
        )
    }
}

@Composable
private fun InstructionsSection() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(BambooSurface)
            .border(1.dp, BambooBorder, RoundedCornerShape(20.dp))
            .padding(20.dp)
    ) {
        Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.AutoMirrored.Filled.HelpOutline, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(20.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "INSTRUCTIONS & HELP",
                    color = BambooTextPrimary,
                    fontFamily = PoppinsFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    letterSpacing = 1.sp
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            InstructionAccordionItem(
                title = "How Attendance Works",
                content = "Attendance percentage is calculated dynamically using the formula:\n\nAttendance % = (Attended Classes ÷ Total Classes) × 100\n\nExample: 8 attended out of 10 total = (8/10) × 100 = 80%."
            )

            InstructionAccordionItem(
                title = "How Attendance Changes",
                content = "When you attend a new class, both Attended Classes and Total Classes increase by 1:\n• 8/10 → 9/11 (81.8%)\n\nWhen you miss a class, only Total Classes increases by 1:\n• 8/10 → 8/11 (72.7%)."
            )

            InstructionAccordionItem(
                title = "How Timetable Works",
                content = "The timetable automatically selects today's day of the week on launch. It displays scheduled lectures and labs filtered specifically by your Branch, Year, and Section."
            )

            InstructionAccordionItem(
                title = "How Syllabus Works",
                content = "Syllabus outlines are organized by Semester, Subject, Units, and Topics according to official curriculum definitions for your branch and year."
            )

            InstructionAccordionItem(
                title = "How Student Profile Works",
                content = "Your student profile stores your Name, Branch, Year, and Section locally on this device. Editing your branch or section updates your timetable, subjects, and syllabus instantly."
            )

            InstructionAccordionItem(
                title = "What Happens When Changing Phones",
                content = "Because the app operates 100% offline without Firebase or cloud accounts, your profile and data remain strictly on this device. Installing the app on a new phone will prompt setup again."
            )

            InstructionAccordionItem(
                title = "Data & Privacy",
                content = "Your profile, attendance logs, notes, and assignments are stored exclusively on your phone in local storage. No personal data or credentials are sent to external cloud servers."
            )
        }
    }
}

@Composable
private fun InstructionAccordionItem(title: String, content: String) {
    var expanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(BambooElevated)
            .clickable { expanded = !expanded }
            .padding(14.dp)
            .animateContentSize()
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = title,
                color = BambooSoftGreen,
                fontFamily = PoppinsFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp
            )
            Icon(
                imageVector = if (expanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                contentDescription = null,
                tint = BambooPrimaryGreen,
                modifier = Modifier.size(18.dp)
            )
        }

        if (expanded) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = content,
                color = BambooTextSecondary,
                fontFamily = PoppinsFontFamily,
                fontSize = 12.sp,
                lineHeight = 18.sp
            )
        }
    }
}

@Composable
private fun NotificationToggleRow(
    label: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            color = BambooTextPrimary,
            fontFamily = PoppinsFontFamily,
            fontSize = 13.sp
        )
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = BambooBg,
                checkedTrackColor = MaterialTheme.colorScheme.primary,
                uncheckedThumbColor = BambooTextMuted,
                uncheckedTrackColor = BambooElevated
            )
        )
    }
}
