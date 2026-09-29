package com.example.ui.screens.academics

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.FileProvider
import com.example.core.ui.EmptyState
import com.example.data.Note
import com.example.ui.state.Attachment
import com.example.ui.theme.*
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun NotesSubScreen(
    notes: List<Note>,
    onSaveNote: (Note) -> Unit,
    onDeleteNote: (String) -> Unit,
    onRenameAttachment: (String, String, String) -> Unit = { _, _, _ -> },
    onDeleteAttachment: (String, String) -> Unit = { _, _ -> }
) {
    var showAddDialog by remember { mutableStateOf(false) }
    
    val groupedNotes = remember(notes) {
        notes.groupBy { it.date }.toSortedMap(compareByDescending { it })
    }

    Box(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        if (notes.isEmpty()) {
            EmptyState(
                title = "No Notes Found",
                description = "Tap + to add a lecture note, PDF, or image."
            )
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                groupedNotes.forEach { (date, dateNotes) ->
                    item {
                        Text(
                            text = formatDateLabel(date),
                            color = BambooPrimaryGreen,
                            fontFamily = PoppinsFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp,
                            letterSpacing = 1.sp
                        )
                    }
                    items(dateNotes) { note ->
                        NoteItem(
                            note = note, 
                            onDelete = { onDeleteNote(note.id) },
                            onRenameAttachment = { attachId, newName -> onRenameAttachment(note.id, attachId, newName) },
                            onDeleteAttachment = { attachId -> onDeleteAttachment(note.id, attachId) }
                        )
                    }
                }
            }
        }

        FloatingActionButton(
            onClick = { showAddDialog = true },
            modifier = Modifier.align(Alignment.BottomEnd),
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = BambooBg
        ) {
            Icon(Icons.Default.Add, contentDescription = "Add Note")
        }
    }

    if (showAddDialog) {
        AddNoteDialog(
            onDismiss = { showAddDialog = false },
            onSave = { title, content, attachments ->
                val newNote = Note(
                    id = UUID.randomUUID().toString(),
                    title = title,
                    content = content,
                    date = SimpleDateFormat("yyyy-MM-dd", Locale.US).format(Date()),
                    attachments = attachments
                )
                onSaveNote(newNote)
                showAddDialog = false
            }
        )
    }
}

private fun formatDateLabel(dateStr: String): String {
    return try {
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.US)
        val date = sdf.parse(dateStr)
        val today = SimpleDateFormat("yyyy-MM-dd", Locale.US).format(Date())
        if (dateStr == today) "TODAY"
        else SimpleDateFormat("EEEE, MMM dd", Locale.US).format(date!!).uppercase()
    } catch (_: Exception) {
        dateStr.uppercase()
    }
}

private fun importFileToLocalStorage(context: Context, sourceUri: Uri, originalName: String): String? {
    return try {
        val notesDir = File(context.filesDir, "notes_files")
        if (!notesDir.exists()) notesDir.mkdirs()
        val extension = originalName.substringAfterLast(".", "")
        val localFileName = "note_${UUID.randomUUID()}.${if (extension.isNotBlank()) extension else "dat"}"
        val localFile = File(notesDir, localFileName)
        
        context.contentResolver.openInputStream(sourceUri)?.use { input ->
            FileOutputStream(localFile).use { output ->
                input.copyTo(output)
            }
        }
        localFile.absolutePath
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}

@Composable
fun NoteItem(
    note: Note,
    onDelete: () -> Unit,
    onRenameAttachment: (String, String) -> Unit = { _, _ -> },
    onDeleteAttachment: (String) -> Unit = { _ -> }
) {
    val context = LocalContext.current
    var showRenameDialog by remember { mutableStateOf<Attachment?>(null) }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = BambooSurface),
        border = androidx.compose.foundation.BorderStroke(1.dp, BambooBorder)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = note.title,
                        color = BambooTextPrimary,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        fontFamily = PoppinsFontFamily
                    )
                    if (note.subjectCode != null) {
                        Text(
                            text = note.subjectCode,
                            color = BambooPrimaryGreen,
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp,
                            fontFamily = PoppinsFontFamily
                        )
                    }
                }
                IconButton(onClick = onDelete) {
                    Icon(Icons.Default.Delete, contentDescription = "Delete", tint = BambooAbsentRed)
                }
            }
            Text(
                text = note.date,
                color = BambooTextMuted,
                fontSize = 12.sp,
                fontFamily = PoppinsFontFamily
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = note.content,
                color = BambooTextSecondary,
                fontSize = 14.sp,
                fontFamily = PoppinsFontFamily
            )

            if (note.attachments.isNotEmpty()) {
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "ATTACHMENTS (${note.attachments.size})",
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold,
                    fontSize = 10.sp,
                    letterSpacing = 1.sp,
                    fontFamily = PoppinsFontFamily
                )
                Spacer(modifier = Modifier.height(6.dp))
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    note.attachments.forEach { attach ->
                        AttachmentRow(
                            attach = attach,
                            onOpen = {
                                try {
                                    val uri = if (attach.uri.startsWith("content://")) {
                                        Uri.parse(attach.uri)
                                    } else {
                                        val file = File(attach.uri)
                                        if (!file.exists()) {
                                            Toast.makeText(context, "File is no longer available.", Toast.LENGTH_SHORT).show()
                                            return@AttachmentRow
                                        }
                                        FileProvider.getUriForFile(
                                            context,
                                            "${context.packageName}.provider",
                                            file
                                        )
                                    }

                                    val mimeType = when {
                                        attach.type == "image" || attach.displayName.endsWith(".png", true) -> "image/png"
                                        attach.displayName.endsWith(".jpg", true) || attach.displayName.endsWith(".jpeg", true) -> "image/jpeg"
                                        attach.displayName.endsWith(".pdf", true) -> "application/pdf"
                                        else -> context.contentResolver.getType(uri) ?: "*/*"
                                    }

                                    val intent = Intent(Intent.ACTION_VIEW).apply {
                                        setDataAndType(uri, mimeType)
                                        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                                    }
                                    context.startActivity(intent)
                                } catch (e: android.content.ActivityNotFoundException) {
                                    Toast.makeText(context, "No app found to open this file type.", Toast.LENGTH_SHORT).show()
                                } catch (e: Exception) {
                                    Toast.makeText(context, "Unable to open file.", Toast.LENGTH_SHORT).show()
                                }
                            },
                            onRename = { showRenameDialog = attach },
                            onDelete = { onDeleteAttachment(attach.id) }
                        )
                    }
                }
            }
        }
    }

    if (showRenameDialog != null) {
        val attachment = showRenameDialog!!
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
                    val ext = attachment.displayName.substringAfterLast(".", "")
                    val finalName = if (ext.isNotBlank()) "$newName.$ext" else newName
                    onRenameAttachment(attachment.id, finalName)
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
private fun AttachmentRow(
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
            .background(BambooElevated)
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

@Composable
fun AddNoteDialog(onDismiss: () -> Unit, onSave: (String, String, List<Attachment>) -> Unit) {
    var title by remember { mutableStateOf("") }
    var content by remember { mutableStateOf("") }
    val attachments = remember { mutableStateListOf<Attachment>() }
    val context = LocalContext.current

    fun getFileName(uri: Uri): String {
        var result: String? = null
        if (uri.scheme == "content") {
            val cursor = context.contentResolver.query(uri, null, null, null, null)
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    val index = cursor.getColumnIndex(android.provider.OpenableColumns.DISPLAY_NAME)
                    if (index != -1) result = cursor.getString(index)
                }
            } finally { cursor?.close() }
        }
        if (result == null) {
            result = uri.path
            val cut = result?.lastIndexOf('/') ?: -1
            if (cut != -1) result = result?.substring(cut + 1)
        }
        return result ?: "document"
    }

    val imagePicker = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let {
            val fileName = getFileName(it)
            val localPath = importFileToLocalStorage(context, it, fileName)
            if (localPath != null) {
                attachments.add(Attachment(uri = localPath, type = "image", name = fileName, displayName = fileName))
            } else {
                Toast.makeText(context, "Unable to import image", Toast.LENGTH_SHORT).show()
            }
        }
    }

    val pdfPicker = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let {
            val fileName = getFileName(it)
            val localPath = importFileToLocalStorage(context, it, fileName)
            if (localPath != null) {
                attachments.add(Attachment(uri = localPath, type = "pdf", name = fileName, displayName = fileName))
            } else {
                Toast.makeText(context, "Unable to import PDF", Toast.LENGTH_SHORT).show()
            }
        }
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = BambooSurface,
        title = { Text("New Note", color = BambooTextPrimary, fontFamily = PoppinsFontFamily) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                TextField(
                    value = title,
                    onValueChange = { title = it },
                    placeholder = { Text("Title") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = BambooElevated,
                        unfocusedContainerColor = BambooElevated,
                        focusedTextColor = BambooTextPrimary,
                        unfocusedTextColor = BambooTextPrimary
                    )
                )
                TextField(
                    value = content,
                    onValueChange = { content = it },
                    placeholder = { Text("Content") },
                    modifier = Modifier.fillMaxWidth().height(120.dp),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = BambooElevated,
                        unfocusedContainerColor = BambooElevated,
                        focusedTextColor = BambooTextPrimary,
                        unfocusedTextColor = BambooTextPrimary
                    )
                )

                Spacer(modifier = Modifier.height(8.dp))
                Text("ATTACHMENTS", color = BambooTextMuted, fontSize = 10.sp, fontWeight = FontWeight.Bold)

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedButton(
                        onClick = { imagePicker.launch("image/*") },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Icon(Icons.Default.Image, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("IMG", fontSize = 10.sp)
                    }
                    OutlinedButton(
                        onClick = { pdfPicker.launch("application/pdf") },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Icon(Icons.Default.Description, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("PDF", fontSize = 10.sp)
                    }
                }

                if (attachments.isNotEmpty()) {
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        attachments.forEach { attach ->
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.CheckCircle, contentDescription = null, tint = BambooPrimaryGreen, modifier = Modifier.size(12.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(attach.displayName, color = BambooTextSecondary, fontSize = 10.sp)
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = { if (title.isNotBlank() || content.isNotBlank()) onSave(title, content, attachments.toList()) },
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
            ) {
                Text("Save Note", color = BambooBg)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel", color = BambooTextSecondary)
            }
        }
    )
}
