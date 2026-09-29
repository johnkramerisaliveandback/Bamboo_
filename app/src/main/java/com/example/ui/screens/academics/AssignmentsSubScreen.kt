package com.example.ui.screens.academics

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
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
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.Assignment
import com.example.data.AssignmentPriority
import com.example.data.AssignmentStatus
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
import java.util.Calendar
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AssignmentsSubScreen(
    assignments: List<Assignment>,
    onSaveAssignment: (Assignment) -> Unit,
    onDeleteAssignment: (String) -> Unit,
    onToggleCompleted: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var filterTab by remember { mutableStateOf("PENDING") } // PENDING, COMPLETED, OVERDUE, ALL
    var showAddDialog by remember { mutableStateOf(false) }
    var editingAssignment by remember { mutableStateOf<Assignment?>(null) }

    val filteredList = remember(assignments, filterTab) {
        when (filterTab) {
            "PENDING" -> assignments.filter { it.status == AssignmentStatus.PENDING || it.status == AssignmentStatus.OVERDUE }
            "COMPLETED" -> assignments.filter { it.status == AssignmentStatus.COMPLETED }
            "OVERDUE" -> assignments.filter { it.status == AssignmentStatus.OVERDUE }
            else -> assignments
        }.sortedWith(compareBy({ it.dueDate }, { it.priority.ordinal }))
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

            // Sub-Filter Tabs
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(BambooSurface)
                    .border(1.dp, BambooBorder, RoundedCornerShape(16.dp))
                    .padding(4.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                listOf(
                    "PENDING" to "Pending",
                    "OVERDUE" to "Overdue",
                    "COMPLETED" to "Done",
                    "ALL" to "All"
                ).forEach { (key, label) ->
                    val isSelected = filterTab == key
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(12.dp))
                            .background(if (isSelected) BambooPrimaryGreen else BambooSurface)
                            .pressClick { filterTab = key }
                            .padding(vertical = 8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = label,
                            color = if (isSelected) BambooBg else BambooTextSecondary,
                            fontFamily = PoppinsFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (filteredList.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            imageVector = Icons.Default.Schedule,
                            contentDescription = null,
                            tint = BambooTextMuted,
                            modifier = Modifier.size(48.dp)
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = when (filterTab) {
                                "PENDING" -> "NO PENDING ASSIGNMENTS"
                                "OVERDUE" -> "NO OVERDUE ASSIGNMENTS"
                                "COMPLETED" -> "NO COMPLETED ASSIGNMENTS"
                                else -> "NO ASSIGNMENTS FOUND"
                            },
                            color = BambooTextMuted,
                            fontFamily = PoppinsFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp,
                            letterSpacing = 1.sp
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Tap + below to add a new course assignment.",
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
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(filteredList, key = { it.id }) { item ->
                        AssignmentCard(
                            assignment = item,
                            onToggleCompleted = { onToggleCompleted(item.id) },
                            onEdit = { editingAssignment = item },
                            onDelete = { onDeleteAssignment(item.id) }
                        )
                    }
                    item {
                        Spacer(modifier = Modifier.height(80.dp))
                    }
                }
            }
        }

        // FAB to Add Assignment
        FloatingActionButton(
            onClick = {
                editingAssignment = null
                showAddDialog = true
            },
            containerColor = BambooPrimaryGreen,
            contentColor = BambooBg,
            shape = CircleShape,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(bottom = 24.dp, end = 20.dp)
        ) {
            Icon(imageVector = Icons.Default.Add, contentDescription = "Add Assignment")
        }
    }

    if (showAddDialog || editingAssignment != null) {
        AssignmentDialog(
            initialAssignment = editingAssignment,
            onDismiss = {
                showAddDialog = false
                editingAssignment = null
            },
            onSave = { newAssignment ->
                onSaveAssignment(newAssignment)
                showAddDialog = false
                editingAssignment = null
            }
        )
    }
}

@Composable
private fun AssignmentCard(
    assignment: Assignment,
    onToggleCompleted: () -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    val isCompleted = assignment.status == AssignmentStatus.COMPLETED
    val isOverdue = assignment.status == AssignmentStatus.OVERDUE

    val priorityColor = when (assignment.priority) {
        AssignmentPriority.HIGH -> BambooPrimaryGreen
        AssignmentPriority.MEDIUM -> BambooSoftGreen
        AssignmentPriority.LOW -> BambooTextMuted
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .background(BambooSurface)
            .border(
                1.dp,
                if (isOverdue) BambooPrimaryGreen.copy(alpha = 0.6f) else BambooBorder,
                RoundedCornerShape(18.dp)
            )
            .padding(16.dp)
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = isCompleted,
                    onCheckedChange = { onToggleCompleted() },
                    colors = CheckboxDefaults.colors(
                        checkedColor = BambooPrimaryGreen,
                        uncheckedColor = BambooTextMuted,
                        checkmarkColor = BambooBg
                    )
                )

                Spacer(modifier = Modifier.width(8.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = assignment.title,
                        color = if (isCompleted) BambooTextMuted else BambooTextPrimary,
                        fontFamily = PoppinsFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        textDecoration = if (isCompleted) TextDecoration.LineThrough else TextDecoration.None
                    )
                    Text(
                        text = "${assignment.subject} • Due ${assignment.dueDate} at ${assignment.dueTime}",
                        color = if (isOverdue && !isCompleted) BambooPrimaryGreen else BambooTextSecondary,
                        fontFamily = PoppinsFontFamily,
                        fontSize = 12.sp,
                        fontWeight = if (isOverdue && !isCompleted) FontWeight.SemiBold else FontWeight.Normal
                    )
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(BambooElevated)
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = assignment.priority.label.uppercase(),
                        color = priorityColor,
                        fontFamily = PoppinsFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 10.sp
                    )
                }
            }

            if (assignment.description.isNotBlank()) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = assignment.description,
                    color = BambooTextMuted,
                    fontFamily = PoppinsFontFamily,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(start = 48.dp)
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp),
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
private fun AssignmentDialog(
    initialAssignment: Assignment?,
    onDismiss: () -> Unit,
    onSave: (Assignment) -> Unit
) {
    val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.US)
    val defaultDate = sdf.format(Date())

    var title by remember { mutableStateOf(initialAssignment?.title ?: "") }
    var subject by remember { mutableStateOf(initialAssignment?.subject ?: "") }
    var description by remember { mutableStateOf(initialAssignment?.description ?: "") }
    var dueDate by remember { mutableStateOf(initialAssignment?.dueDate ?: defaultDate) }
    var dueTime by remember { mutableStateOf(initialAssignment?.dueTime ?: "23:59") }
    var priority by remember { mutableStateOf(initialAssignment?.priority ?: AssignmentPriority.MEDIUM) }
    var reminderTiming by remember { mutableStateOf(initialAssignment?.reminderTiming ?: "1_DAY") }
    var errorMessage by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = BambooSurface,
        title = {
            Text(
                text = if (initialAssignment == null) "NEW ASSIGNMENT" else "EDIT ASSIGNMENT",
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
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Title *") },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = BambooPrimaryGreen,
                        unfocusedBorderColor = BambooBorder,
                        focusedLabelColor = BambooPrimaryGreen
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = subject,
                    onValueChange = { subject = it },
                    label = { Text("Subject / Course Code *") },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = BambooPrimaryGreen,
                        unfocusedBorderColor = BambooBorder,
                        focusedLabelColor = BambooPrimaryGreen
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = dueDate,
                        onValueChange = { dueDate = it },
                        label = { Text("Due Date (YYYY-MM-DD)") },
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = BambooPrimaryGreen,
                            unfocusedBorderColor = BambooBorder
                        ),
                        modifier = Modifier.weight(1f)
                    )
                    OutlinedTextField(
                        value = dueTime,
                        onValueChange = { dueTime = it },
                        label = { Text("Time (HH:mm)") },
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = BambooPrimaryGreen,
                            unfocusedBorderColor = BambooBorder
                        ),
                        modifier = Modifier.weight(1f)
                    )
                }

                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Notes / Instructions") },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = BambooPrimaryGreen,
                        unfocusedBorderColor = BambooBorder
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                Text(
                    text = "Priority Level",
                    color = BambooTextSecondary,
                    fontFamily = PoppinsFontFamily,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    AssignmentPriority.values().forEach { p ->
                        val isSelected = priority == p
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(10.dp))
                                .background(if (isSelected) BambooPrimaryGreen else BambooElevated)
                                .pressClick { priority = p }
                                .padding(vertical = 8.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = p.label,
                                color = if (isSelected) BambooBg else BambooTextPrimary,
                                fontFamily = PoppinsFontFamily,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    if (title.isBlank()) {
                        errorMessage = "Title cannot be empty"
                        return@TextButton
                    }
                    if (subject.isBlank()) {
                        errorMessage = "Subject cannot be empty"
                        return@TextButton
                    }

                    val newAssignment = Assignment(
                        id = initialAssignment?.id ?: "assign_${System.currentTimeMillis()}",
                        title = title.trim(),
                        subject = subject.trim(),
                        description = description.trim(),
                        assignedDate = initialAssignment?.assignedDate ?: defaultDate,
                        dueDate = dueDate.trim(),
                        dueTime = dueTime.trim(),
                        priority = priority,
                        status = initialAssignment?.status ?: AssignmentStatus.PENDING,
                        reminderTiming = reminderTiming
                    )

                    onSave(newAssignment)
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
