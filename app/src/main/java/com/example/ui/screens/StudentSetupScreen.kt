package com.example.ui.screens

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.School
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.AcademicData
import com.example.data.TimetableSection
import com.example.ui.theme.*

@Composable
fun StudentSetupScreen(
    onCompleteSetup: (name: String, branch: String, year: String, section: TimetableSection) -> Unit
) {
    var studentName by remember { mutableStateOf("") }
    var selectedBranch by remember { mutableStateOf("CS (AI)") }
    var selectedYear by remember { mutableStateOf("1st Year") }
    var selectedSection by remember { mutableStateOf(TimetableSection.SECTION_1) }

    var isBranchDropdownExpanded by remember { mutableStateOf(false) }
    var isYearDropdownExpanded by remember { mutableStateOf(false) }
    var isSectionDropdownExpanded by remember { mutableStateOf(false) }

    var errorMessage by remember { mutableStateOf<String?>(null) }

    val scaleAnim = remember { Animatable(0.94f) }

    LaunchedEffect(Unit) {
        scaleAnim.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 500, easing = FastOutSlowInEasing)
        )
    }

    val branches = remember { AcademicData.BRANCHES.map { it.code } }
    val years = remember { listOf("1st Year", "2nd Year", "3rd Year", "4th Year") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(BambooBg, BambooSurface, BambooBg)
                )
            )
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .scale(scaleAnim.value),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Header Badge
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .clip(CircleShape)
                    .background(BambooElevated)
                    .border(1.dp, BambooBorder, CircleShape)
                    .padding(horizontal = 14.dp, vertical = 6.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.AutoAwesome,
                    contentDescription = null,
                    tint = BambooPrimaryGreen,
                    modifier = Modifier.size(14.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "BAMBOO • ACADEMIC OS",
                    color = BambooSoftGreen,
                    fontFamily = PoppinsFontFamily,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 11.sp,
                    letterSpacing = 1.5.sp
                )
            }

            Spacer(modifier = Modifier.height(28.dp))

            Text(
                text = "Set up your Academic OS",
                color = BambooBrightGreen,
                fontFamily = PoppinsFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp,
                letterSpacing = 1.sp
            )

            Text(
                text = "Local device profile setup • No login required",
                color = BambooTextMuted,
                fontFamily = PoppinsFontFamily,
                fontWeight = FontWeight.Medium,
                fontSize = 13.sp
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Form Card
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(24.dp))
                    .background(BambooSurface)
                    .border(1.dp, BambooBorder, RoundedCornerShape(24.dp))
                    .padding(20.dp)
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    // Student Name
                    Column {
                        Text(
                            text = "STUDENT NAME",
                            color = BambooTextMuted,
                            fontFamily = PoppinsFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 10.sp,
                            letterSpacing = 1.sp,
                            modifier = Modifier.padding(bottom = 6.dp)
                        )
                        OutlinedTextField(
                            value = studentName,
                            onValueChange = {
                                studentName = it
                                errorMessage = null
                            },
                            placeholder = {
                                Text("e.g. Rahul", color = BambooTextMuted, fontFamily = PoppinsFontFamily)
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("setup_name_input"),
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedContainerColor = BambooElevated,
                                unfocusedContainerColor = BambooElevated,
                                focusedBorderColor = BambooPrimaryGreen,
                                unfocusedBorderColor = BambooBorder,
                                focusedTextColor = BambooTextPrimary,
                                unfocusedTextColor = BambooTextPrimary
                            ),
                            shape = RoundedCornerShape(12.dp)
                        )
                    }

                    // Branch Dropdown
                    Column {
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
                                    .clickable { isBranchDropdownExpanded = true }
                                    .padding(horizontal = 16.dp, vertical = 14.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = selectedBranch,
                                    color = BambooTextPrimary,
                                    fontFamily = PoppinsFontFamily,
                                    fontSize = 14.sp
                                )
                                Icon(
                                    Icons.Default.ArrowDropDown,
                                    contentDescription = null,
                                    tint = BambooPrimaryGreen
                                )
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
                                            selectedBranch = b
                                            isBranchDropdownExpanded = false
                                        }
                                    )
                                }
                            }
                        }
                    }

                    // Year Dropdown
                    Column {
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
                                    .padding(horizontal = 16.dp, vertical = 14.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = selectedYear,
                                    color = BambooTextPrimary,
                                    fontFamily = PoppinsFontFamily,
                                    fontSize = 14.sp
                                )
                                Icon(
                                    Icons.Default.ArrowDropDown,
                                    contentDescription = null,
                                    tint = BambooPrimaryGreen
                                )
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
                                            selectedYear = yr
                                            isYearDropdownExpanded = false
                                        }
                                    )
                                }
                            }
                        }
                    }

                    // Section Dropdown
                    Column {
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
                                    .padding(horizontal = 16.dp, vertical = 14.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                val sectionLabel = when (selectedSection) {
                                    TimetableSection.SECTION_1 -> "Section A"
                                    TimetableSection.SECTION_2 -> "Section B"
                                    TimetableSection.COMMON -> "Common"
                                }
                                Text(
                                    text = sectionLabel,
                                    color = BambooTextPrimary,
                                    fontFamily = PoppinsFontFamily,
                                    fontSize = 14.sp
                                )
                                Icon(
                                    Icons.Default.ArrowDropDown,
                                    contentDescription = null,
                                    tint = BambooPrimaryGreen
                                )
                            }

                            DropdownMenu(
                                expanded = isSectionDropdownExpanded,
                                onDismissRequest = { isSectionDropdownExpanded = false },
                                modifier = Modifier.background(BambooElevated)
                            ) {
                                DropdownMenuItem(
                                    text = { Text("Section A", color = BambooTextPrimary, fontFamily = PoppinsFontFamily) },
                                    onClick = {
                                        selectedSection = TimetableSection.SECTION_1
                                        isSectionDropdownExpanded = false
                                    }
                                )
                                DropdownMenuItem(
                                    text = { Text("Section B", color = BambooTextPrimary, fontFamily = PoppinsFontFamily) },
                                    onClick = {
                                        selectedSection = TimetableSection.SECTION_2
                                        isSectionDropdownExpanded = false
                                    }
                                )
                                DropdownMenuItem(
                                    text = { Text("Common", color = BambooTextPrimary, fontFamily = PoppinsFontFamily) },
                                    onClick = {
                                        selectedSection = TimetableSection.COMMON
                                        isSectionDropdownExpanded = false
                                    }
                                )
                            }
                        }
                    }

                    if (errorMessage != null) {
                        Text(
                            text = errorMessage!!,
                            color = BambooAbsentRed,
                            fontFamily = PoppinsFontFamily,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Button(
                        onClick = {
                            val trimmedName = studentName.trim()
                            if (trimmedName.isBlank()) {
                                errorMessage = "Please enter your name"
                            } else {
                                onCompleteSetup(trimmedName, selectedBranch, selectedYear, selectedSection)
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp)
                            .testTag("setup_submit_button"),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = BambooPrimaryGreen,
                            contentColor = BambooBg
                        ),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.School, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "GET STARTED",
                                fontFamily = PoppinsFontFamily,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                                letterSpacing = 1.2.sp
                            )
                        }
                    }
                }
            }
        }
    }
}
