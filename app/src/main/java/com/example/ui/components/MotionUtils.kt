package com.example.ui.components

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.state.CalendarDay
import com.example.ui.state.ClassSession
import com.example.ui.state.DayStatus
import com.example.ui.theme.BambooBg
import com.example.ui.theme.BambooBorder
import com.example.ui.theme.BambooBorderHighlight
import com.example.ui.theme.BambooDarkGreen
import com.example.ui.theme.BambooElevated
import com.example.ui.theme.BambooPrimaryGreen
import com.example.ui.theme.BambooSoftGreen
import com.example.ui.theme.BambooSurface
import com.example.ui.theme.BambooTextMuted
import com.example.ui.theme.BambooTextPrimary
import com.example.ui.theme.BambooTextSecondary
import com.example.ui.theme.PoppinsFontFamily

fun Modifier.tactilePress(
    enabled: Boolean = true,
    onClick: (() -> Unit)? = null
): Modifier = composed {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val scale by animateFloatAsState(
        targetValue = if (isPressed && enabled) 0.96f else 1f,
        animationSpec = spring(stiffness = Spring.StiffnessMediumLow, dampingRatio = Spring.DampingRatioMediumBouncy),
        label = "tactileScale"
    )

    this
        .scale(scale)
        .then(
            if (onClick != null) {
                Modifier.clickable(
                    interactionSource = interactionSource,
                    indication = null,
                    enabled = enabled,
                    onClick = onClick
                )
            } else Modifier
        )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClassSessionDetailBottomSheet(
    session: ClassSession,
    onDismiss: () -> Unit,
    sheetState: SheetState
) {
    var reminderSet by remember { mutableStateOf(false) }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = BambooSurface,
        contentColor = BambooTextPrimary,
        shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .padding(bottom = 32.dp)
        ) {
            // Drag Handle & Close Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(BambooElevated)
                            .padding(horizontal = 10.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = session.courseCode,
                            color = BambooPrimaryGreen,
                            fontFamily = PoppinsFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = session.time,
                        color = BambooSoftGreen,
                        fontFamily = PoppinsFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp
                    )
                }

                IconButton(
                    onClick = onDismiss,
                    modifier = Modifier.testTag("close_sheet_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close",
                        tint = BambooTextMuted
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Title
            Text(
                text = session.subject,
                color = BambooTextPrimary,
                fontFamily = PoppinsFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Details Box
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(BambooElevated)
                    .border(1.dp, BambooBorder, RoundedCornerShape(16.dp))
                    .padding(16.dp)
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    DetailRow(icon = Icons.Default.LocationOn, label = "Venue", value = session.room)
                    DetailRow(icon = Icons.Default.Person, label = "Instructor", value = session.teacher)
                    DetailRow(icon = Icons.Default.Book, label = "Curriculum Module", value = "Module 4: Advanced Principles")
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "SESSION NOTES & PREPARATION",
                color = BambooTextMuted,
                fontFamily = PoppinsFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 10.sp,
                letterSpacing = 1.2.sp
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "Ensure required readings for Chapter 5 are reviewed prior to entering the lecture hall. Interactive problem sets will be distributed.",
                color = BambooTextSecondary,
                fontFamily = PoppinsFontFamily,
                fontSize = 13.sp,
                lineHeight = 18.sp
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Action Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Button(
                    onClick = { reminderSet = !reminderSet },
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp)
                        .testTag("reminder_button"),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (reminderSet) BambooDarkGreen else BambooElevated,
                        contentColor = BambooTextPrimary
                    ),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Notifications,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = if (reminderSet) "REMINDER SET" else "REMIND ME",
                        fontFamily = PoppinsFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 11.sp
                    )
                }

                Button(
                    onClick = onDismiss,
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp)
                        .testTag("dismiss_detail_button"),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = BambooPrimaryGreen,
                        contentColor = BambooBg
                    ),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "DONE",
                        fontFamily = PoppinsFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 11.sp
                    )
                }
            }
        }
    }
}

@Composable
private fun DetailRow(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    value: String
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(imageVector = icon, contentDescription = null, tint = BambooPrimaryGreen, modifier = Modifier.size(18.dp))
        Spacer(modifier = Modifier.width(10.dp))
        Column {
            Text(text = label, color = BambooTextMuted, fontFamily = PoppinsFontFamily, fontSize = 10.sp)
            Text(text = value, color = BambooTextPrimary, fontFamily = PoppinsFontFamily, fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalendarDayDetailBottomSheet(
    day: CalendarDay,
    monthYearText: String,
    onDismiss: () -> Unit,
    sheetState: SheetState
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = BambooSurface,
        contentColor = BambooTextPrimary,
        shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .padding(bottom = 32.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(imageVector = Icons.Default.Event, contentDescription = null, tint = BambooPrimaryGreen, modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "DAY DETAILS",
                        color = BambooTextPrimary,
                        fontFamily = PoppinsFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        letterSpacing = 1.sp
                    )
                }

                IconButton(onClick = onDismiss) {
                    Icon(imageVector = Icons.Default.Close, contentDescription = "Close", tint = BambooTextMuted)
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "$monthYearText ${day.dayNumber}",
                color = BambooSoftGreen,
                fontFamily = PoppinsFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp
            )

            val statusLabel = when (day.status) {
                DayStatus.PRESENT -> "STATUS: PRESENT (100% ATTENDANCE)"
                DayStatus.ABSENT -> "STATUS: ABSENT"
                DayStatus.HOLIDAY -> "STATUS: OFFICIAL ACADEMIC HOLIDAY"
                DayStatus.NONE -> "STATUS: NO SCHEDULED CLASSES"
                DayStatus.CANCELLED -> "STATUS: CLASS CANCELLED"
            }

            Text(
                text = statusLabel,
                color = if (day.status == DayStatus.PRESENT) BambooPrimaryGreen else BambooTextMuted,
                fontFamily = PoppinsFontFamily,
                fontWeight = FontWeight.SemiBold,
                fontSize = 12.sp
            )

            Spacer(modifier = Modifier.height(18.dp))

            Text(
                text = "SCHEDULED SESSIONS",
                color = BambooTextMuted,
                fontFamily = PoppinsFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 10.sp,
                letterSpacing = 1.sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                SessionStatusRow("08:30 AM", "CS-350 Advanced Algorithms", day.status == DayStatus.PRESENT)
                SessionStatusRow("10:15 AM", "PHY-401 Quantum Physics", day.status == DayStatus.PRESENT)
                SessionStatusRow("01:30 PM", "MATH-210 Linear Algebra", day.status == DayStatus.PRESENT)
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = onDismiss,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .testTag("calendar_detail_close_button"),
                colors = ButtonDefaults.buttonColors(
                    containerColor = BambooPrimaryGreen,
                    contentColor = BambooBg
                ),
                shape = RoundedCornerShape(14.dp)
            ) {
                Text(
                    text = "CLOSE",
                    fontFamily = PoppinsFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp,
                    letterSpacing = 1.sp
                )
            }
        }
    }
}

@Composable
private fun SessionStatusRow(time: String, title: String, isPresent: Boolean) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(BambooElevated)
            .border(1.dp, BambooBorder, RoundedCornerShape(12.dp))
            .padding(12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(text = time, color = BambooSoftGreen, fontFamily = PoppinsFontFamily, fontWeight = FontWeight.Bold, fontSize = 11.sp)
                Text(text = title, color = BambooTextPrimary, fontFamily = PoppinsFontFamily, fontSize = 13.sp)
            }

            Box(
                modifier = Modifier
                    .clip(CircleShape)
                    .background(if (isPresent) BambooPrimaryGreen.copy(alpha = 0.2f) else BambooDarkGreen.copy(alpha = 0.2f))
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Text(
                    text = if (isPresent) "PRESENT" else "ABSENT",
                    color = if (isPresent) BambooPrimaryGreen else BambooDarkGreen,
                    fontFamily = PoppinsFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 10.sp
                )
            }
        }
    }
}
