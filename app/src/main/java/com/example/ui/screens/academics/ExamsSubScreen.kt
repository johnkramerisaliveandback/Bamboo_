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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Event
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.Exam
import com.example.data.ExamType
import com.example.ui.components.pressClick
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
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExamsSubScreen(
    exams: List<Exam>,
    onSaveExam: (Exam) -> Unit,
    onDeleteExam: (String) -> Unit,
    onToggleCompleted: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var showAddDialog by remember { mutableStateOf(false) }
    var editingExam by remember { mutableStateOf<Exam?>(null) }

    val sortedExams = remember(exams) {
        exams.sortedBy { it.date }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(BambooBg)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp)
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            if (sortedExams.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            imageVector = Icons.Default.Event,
                            contentDescription = null,
                            tint = BambooTextMuted,
                            modifier = Modifier.size(48.dp)
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "NO EXAMS SCHEDULED",
                            color = BambooTextMuted,
                            fontFamily = PoppinsFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp,
                            letterSpacing = 1.sp
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Tap + below to add mid-sems, quizzes, or practicals.",
                            color = BambooTextMuted,
                            fontFamily = PoppinsFontFamily,
                            fontSize = 12.sp
                        )
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    items(sortedExams, key = { it.id }) { exam ->
                        ExamCard(
                            exam = exam,
                            onToggleCompleted = { onToggleCompleted(exam.id) },
                            onEdit = { editingExam = exam },
                            onDelete = { onDeleteExam(exam.id) }
                        )
                    }
                    item {
                        Spacer(modifier = Modifier.height(80.dp))
                    }
                }
            }
        }

        // FAB to Add Exam
        FloatingActionButton(
            onClick = {
                editingExam = null
                showAddDialog = true
            },
            containerColor = BambooPrimaryGreen,
            contentColor = BambooBg,
            shape = CircleShape,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(bottom = 24.dp, end = 20.dp)
        ) {
            Icon(imageVector = Icons.Default.Add, contentDescription = "Add Exam")
        }
    }

    if (showAddDialog || editingExam != null) {
        ExamDialog(
            initialExam = editingExam,
            onDismiss = {
                showAddDialog = false
                editingExam = null
            },
            onSave = { newExam ->
                onSaveExam(newExam)
                showAddDialog = false
                editingExam = null
            }
        )
    }
}

@Composable
private fun ExamCard(
    exam: Exam,
    onToggleCompleted: () -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    val todayStr = SimpleDateFormat("yyyy-MM-dd", Locale.US).format(Date())
    val isPast = exam.date < todayStr || exam.isCompleted

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
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(BambooDarkGreen)
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = exam.examType.label.uppercase(),
                        color = BambooSoftGreen,
                        fontFamily = PoppinsFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 11.sp
                    )
                }

                Text(
                    text = if (isPast) "PASSED" else exam.date,
                    color = if (isPast) BambooTextMuted else BambooPrimaryGreen,
                    fontFamily = PoppinsFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = exam.subject,
                color = BambooTextPrimary,
                fontFamily = PoppinsFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 17.sp
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "Time: ${exam.startTime} - ${exam.endTime}" + if (exam.venue.isNotBlank()) " • Venue: ${exam.venue}" else "",
                color = BambooTextSecondary,
                fontFamily = PoppinsFontFamily,
                fontSize = 13.sp
            )

            if (exam.notes.isNotBlank()) {
                Spacer(modifier = Modifier.height(8.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .background(BambooElevated)
                        .padding(10.dp)
                ) {
                    Text(
                        text = exam.notes,
                        color = BambooTextMuted,
                        fontFamily = PoppinsFontFamily,
                        fontSize = 12.sp
                    )
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextButton(
                    onClick = onEdit,
                    modifier = Modifier.height(36.dp)
                ) {
                    Icon(Icons.Default.Edit, null, modifier = Modifier.size(16.dp))
                    Spacer(Modifier.width(4.dp))
                    Text("EDIT", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
                Spacer(Modifier.width(8.dp))
                IconButton(
                    onClick = onDelete,
                    modifier = Modifier.size(36.dp).clip(CircleShape).background(BambooPrimaryGreen.copy(alpha = 0.1f))
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete",
                        tint = BambooPrimaryGreen,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ExamDialog(
    initialExam: Exam?,
    onDismiss: () -> Unit,
    onSave: (Exam) -> Unit
) {
    val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.US)
    val defaultDate = sdf.format(Date())

    var subject by remember { mutableStateOf(initialExam?.subject ?: "") }
    var examType by remember { mutableStateOf(initialExam?.examType ?: ExamType.MID_SEM) }
    var date by remember { mutableStateOf(initialExam?.date ?: defaultDate) }
    var startTime by remember { mutableStateOf(initialExam?.startTime ?: "09:30") }
    var endTime by remember { mutableStateOf(initialExam?.endTime ?: "12:30") }
    var venue by remember { mutableStateOf(initialExam?.venue ?: "") }
    var notes by remember { mutableStateOf(initialExam?.notes ?: "") }
    var errorMessage by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = BambooSurface,
        title = {
            Text(
                text = if (initialExam == null) "NEW EXAM" else "EDIT EXAM",
                color = BambooTextPrimary,
                fontFamily = PoppinsFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                if (errorMessage.isNotBlank()) {
                    Text(
                        text = errorMessage,
                        color = BambooPrimaryGreen,
                        fontFamily = PoppinsFontFamily,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                OutlinedTextField(
                    value = subject,
                    onValueChange = { subject = it },
                    label = { Text("Subject Name / Code *") },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = BambooPrimaryGreen,
                        unfocusedBorderColor = BambooBorder,
                        focusedLabelColor = BambooPrimaryGreen
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                Text(
                    text = "Exam Type",
                    color = BambooTextSecondary,
                    fontFamily = PoppinsFontFamily,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    ExamType.values().take(4).forEach { t ->
                        val isSelected = examType == t
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (isSelected) BambooPrimaryGreen else BambooElevated)
                                .pressClick { examType = t }
                                .padding(vertical = 8.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = t.label,
                                color = if (isSelected) BambooBg else BambooTextPrimary,
                                fontFamily = PoppinsFontFamily,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                OutlinedTextField(
                    value = date,
                    onValueChange = { date = it },
                    label = { Text("Date (YYYY-MM-DD) *") },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = BambooPrimaryGreen,
                        unfocusedBorderColor = BambooBorder
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = startTime,
                        onValueChange = { startTime = it },
                        label = { Text("Start Time") },
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = BambooPrimaryGreen,
                            unfocusedBorderColor = BambooBorder
                        ),
                        modifier = Modifier.weight(1f)
                    )
                    OutlinedTextField(
                        value = endTime,
                        onValueChange = { endTime = it },
                        label = { Text("End Time") },
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = BambooPrimaryGreen,
                            unfocusedBorderColor = BambooBorder
                        ),
                        modifier = Modifier.weight(1f)
                    )
                }

                OutlinedTextField(
                    value = venue,
                    onValueChange = { venue = it },
                    label = { Text("Venue / Hall Number") },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = BambooPrimaryGreen,
                        unfocusedBorderColor = BambooBorder
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = notes,
                    onValueChange = { notes = it },
                    label = { Text("Syllabus / Notes") },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = BambooPrimaryGreen,
                        unfocusedBorderColor = BambooBorder
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    if (subject.isBlank()) {
                        errorMessage = "Subject cannot be empty"
                        return@TextButton
                    }
                    if (date.isBlank()) {
                        errorMessage = "Date cannot be empty"
                        return@TextButton
                    }

                    val newExam = Exam(
                        id = initialExam?.id ?: "exam_${System.currentTimeMillis()}",
                        subject = subject.trim(),
                        examType = examType,
                        date = date.trim(),
                        startTime = startTime.trim(),
                        endTime = endTime.trim(),
                        venue = venue.trim(),
                        notes = notes.trim(),
                        isCompleted = initialExam?.isCompleted ?: false,
                        reminderTiming = initialExam?.reminderTiming ?: "1_DAY"
                    )

                    onSave(newExam)
                }
            ) {
                Text(
                    text = "SAVE",
                    color = BambooPrimaryGreen,
                    fontFamily = PoppinsFontFamily,
                    fontWeight = FontWeight.Bold
                )
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(
                    text = "CANCEL",
                    color = BambooTextMuted,
                    fontFamily = PoppinsFontFamily
                )
            }
        }
    )
}
