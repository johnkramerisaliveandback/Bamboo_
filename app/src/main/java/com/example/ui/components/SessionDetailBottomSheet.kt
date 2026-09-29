package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.OpenInNew
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.Note
import com.example.ui.screens.academics.AddNoteDialog
import com.example.ui.state.ClassSession
import com.example.ui.state.SessionStatus
import com.example.ui.theme.*
import java.text.SimpleDateFormat
import java.util.*
import com.example.ui.state.Attachment

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SessionDetailBottomSheet(
    session: ClassSession,
    notes: List<Note> = emptyList(),
    isSaving: Boolean = false,
    onDismiss: () -> Unit,
    onMarkAttended: () -> Unit = {},
    onAddNote: (String, String, List<com.example.ui.state.Attachment>) -> Unit = { _, _, _ -> },
    onDeleteNote: (String) -> Unit = {},
    onRenameAttachment: (String, String, String) -> Unit = { _, _, _ -> },
    onDeleteAttachment: (String, String) -> Unit = { _, _ -> }
) {
    val context = LocalContext.current
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = false)
    var showAddNoteDialog by remember { mutableStateOf(false) }
    var showRenameDialog by remember { mutableStateOf<Pair<String, Attachment>?>(null) } // noteId, attachment

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = BambooSurface,
        scrimColor = BambooBg.copy(alpha = 0.75f),
        shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
        dragHandle = {
            Box(
                modifier = Modifier
                    .padding(top = 12.dp, bottom = 8.dp)
                    .width(48.dp)
                    .height(4.dp)
                    .clip(CircleShape)
                    .background(BambooBorderHighlight)
            )
        }
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .padding(horizontal = 24.dp)
        ) {
            item {
                Spacer(modifier = Modifier.height(8.dp))
                // Header Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(BambooElevated)
                            .border(1.dp, BambooBorder, RoundedCornerShape(8.dp))
                            .padding(horizontal = 10.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = session.courseCode,
                            color = BambooPrimaryGreen,
                            fontFamily = PoppinsFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp,
                            letterSpacing = 1.sp
                        )
                    }

                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(BambooElevated)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close",
                            tint = BambooTextMuted,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Subject Title
                Text(
                    text = session.subject,
                    color = BambooTextPrimary,
                    fontFamily = PoppinsFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 22.sp,
                    lineHeight = 28.sp
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Info Grid
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(BambooElevated)
                        .border(1.dp, BambooBorder, RoundedCornerShape(16.dp))
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Time
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Schedule,
                            contentDescription = null,
                            tint = BambooPrimaryGreen,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "SESSION TIME",
                                color = BambooTextMuted,
                                fontFamily = PoppinsFontFamily,
                                fontWeight = FontWeight.Bold,
                                fontSize = 9.sp,
                                letterSpacing = 1.sp
                            )
                            Text(
                                text = session.time,
                                color = BambooTextPrimary,
                                fontFamily = PoppinsFontFamily,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 13.sp
                            )
                        }
                    }

                    // Location Room
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.LocationOn,
                            contentDescription = null,
                            tint = BambooBrightGreen,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "HALL / VENUE (ROOM)",
                                color = BambooBrightGreen,
                                fontFamily = PoppinsFontFamily,
                                fontWeight = FontWeight.Bold,
                                fontSize = 10.sp,
                                letterSpacing = 1.sp
                            )
                            Text(
                                text = session.room,
                                color = BambooTextPrimary,
                                fontFamily = PoppinsFontFamily,
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp
                            )
                        }
                    }

                    // Instructor
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = null,
                            tint = BambooPrimaryGreen,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "FACULTY / INSTRUCTOR",
                                color = BambooTextMuted,
                                fontFamily = PoppinsFontFamily,
                                fontWeight = FontWeight.Bold,
                                fontSize = 9.sp,
                                letterSpacing = 1.sp
                            )
                            Text(
                                text = session.teacher,
                                color = BambooTextPrimary,
                                fontFamily = PoppinsFontFamily,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 13.sp
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))
            }

            // LECTURE NOTES Section
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "LECTURE NOTES",
                        color = BambooTextPrimary,
                        fontFamily = PoppinsFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        letterSpacing = 1.sp
                    )
                    TextButton(onClick = { showAddNoteDialog = true }) {
                        Icon(Icons.Default.Add, null, modifier = Modifier.size(16.dp))
                        Spacer(Modifier.width(4.dp))
                        Text("Add Note", fontSize = 12.sp)
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
            }

            if (notes.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(80.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(BambooElevated)
                            .border(1.dp, BambooBorder, RoundedCornerShape(12.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("No notes for this lecture.", color = BambooTextMuted, fontSize = 12.sp)
                    }
                }
            } else {
                items(notes) { note ->
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 12.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .background(BambooElevated)
                            .border(1.dp, BambooBorder, RoundedCornerShape(16.dp))
                            .padding(16.dp)
                    ) {
                        Column {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = note.title,
                                    color = BambooTextPrimary,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 15.sp,
                                    fontFamily = PoppinsFontFamily
                                )
                                IconButton(
                                    onClick = { onDeleteNote(note.id) },
                                    modifier = Modifier.size(28.dp)
                                ) {
                                    Icon(
                                        Icons.Default.Delete,
                                        contentDescription = "Delete Note",
                                        tint = BambooAbsentRed,
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                            }
                            
                            if (note.content.isNotBlank()) {
                                Text(
                                    text = note.content,
                                    color = BambooTextSecondary,
                                    fontSize = 13.sp,
                                    fontFamily = PoppinsFontFamily,
                                    modifier = Modifier.padding(top = 4.dp)
                                )
                            }
                            
                            if (note.attachments.isNotEmpty()) {
                                Spacer(Modifier.height(12.dp))
                                note.attachments.forEach { attach ->
                                    AttachmentItemRow(
                                        attach = attach,
                                        onOpen = {
                                            try {
                                                val intent = android.content.Intent(android.content.Intent.ACTION_VIEW).apply {
                                                    val file = java.io.File(attach.uri)
                                                    val uri = androidx.core.content.FileProvider.getUriForFile(
                                                        context,
                                                        "${context.packageName}.provider",
                                                        file
                                                    )
                                                    setDataAndType(uri, if (attach.type == "image") "image/*" else "application/pdf")
                                                    addFlags(android.content.Intent.FLAG_GRANT_READ_URI_PERMISSION)
                                                }
                                                context.startActivity(intent)
                                            } catch (e: Exception) {
                                                android.widget.Toast.makeText(context, "Unable to open file", android.widget.Toast.LENGTH_SHORT).show()
                                            }
                                        },
                                        onRename = { showRenameDialog = Pair(note.id, attach) },
                                        onDelete = { onDeleteAttachment(note.id, attach.id) }
                                    )
                                }
                            }
                        }
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(24.dp))

                // Mark Attended Button
                Button(
                    onClick = {
                        onMarkAttended()
                    },
                    enabled = !isSaving,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                        .testTag("session_mark_attended_button"),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isSaving) BambooPrimaryGreen.copy(alpha = 0.5f) else BambooPrimaryGreen,
                        contentColor = BambooBg
                    )
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        if (isSaving) {
                             CircularProgressIndicator(modifier = Modifier.size(18.dp), color = BambooBg, strokeWidth = 2.dp)
                        } else {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (isSaving) "SAVING..." else "MARK AS ATTENDED",
                            fontFamily = PoppinsFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp,
                            letterSpacing = 0.5.sp
                        )
                    }
                }
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }

    if (showAddNoteDialog) {
        AddNoteDialog(
            onDismiss = { showAddNoteDialog = false },
            onSave = { title, content, attachments ->
                onAddNote(title, content, attachments)
                showAddNoteDialog = false
            }
        )
    }

    if (showRenameDialog != null) {
        val (noteId, attachment) = showRenameDialog!!
        var newName by remember { mutableStateOf(attachment.displayName.substringBeforeLast(".")) }
        
        AlertDialog(
            onDismissRequest = { showRenameDialog = null },
            title = { Text("Rename File") },
            text = {
                OutlinedTextField(
                    value = newName,
                    onValueChange = { newName = it },
                    label = { Text("New Name") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
            },
            confirmButton = {
                TextButton(onClick = {
                    onRenameAttachment(noteId, attachment.id, newName)
                    showRenameDialog = null
                }) {
                    Text("SAVE", color = BambooPrimaryGreen)
                }
            },
            dismissButton = {
                TextButton(onClick = { showRenameDialog = null }) {
                    Text("CANCEL", color = BambooTextMuted)
                }
            }
        )
    }
}

@Composable
private fun AttachmentItemRow(
    attach: Attachment,
    onOpen: () -> Unit,
    onRename: () -> Unit,
    onDelete: () -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(BambooElevated.copy(alpha = 0.5f))
            .clickable(onClick = onOpen)
            .padding(start = 8.dp, end = 4.dp, top = 4.dp, bottom = 4.dp)
    ) {
        Icon(
            imageVector = if (attach.type == "image") Icons.Default.Image else Icons.Default.Description,
            contentDescription = null,
            tint = BambooSoftGreen,
            modifier = Modifier.size(16.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = attach.displayName,
            color = BambooTextPrimary,
            fontSize = 11.sp,
            maxLines = 1,
            overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis,
            fontFamily = PoppinsFontFamily,
            modifier = Modifier.weight(1f)
        )
        
        Box {
            IconButton(
                onClick = { expanded = true },
                modifier = Modifier.size(24.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.MoreVert,
                    contentDescription = "Options",
                    tint = BambooTextMuted,
                    modifier = Modifier.size(14.dp)
                )
            }
            
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier.background(BambooSurface)
            ) {
                DropdownMenuItem(
                    text = { Text("Open", fontSize = 12.sp) },
                    onClick = { expanded = false; onOpen() },
                    leadingIcon = { Icon(Icons.AutoMirrored.Filled.OpenInNew, null, modifier = Modifier.size(16.dp)) }
                )
                DropdownMenuItem(
                    text = { Text("Rename", fontSize = 12.sp) },
                    onClick = { expanded = false; onRename() },
                    leadingIcon = { Icon(Icons.Default.Edit, null, modifier = Modifier.size(16.dp)) }
                )
                DropdownMenuItem(
                    text = { Text("Delete", fontSize = 12.sp, color = BambooAbsentRed) },
                    onClick = { expanded = false; onDelete() },
                    leadingIcon = { Icon(Icons.Default.Delete, null, tint = BambooAbsentRed, modifier = Modifier.size(16.dp)) }
                )
            }
        }
    }
}
