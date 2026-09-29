package com.example.ui.screens.academics

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.OfficialHoliday
import com.example.ui.state.HolidayProgress
import com.example.ui.theme.*
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun HolidaysSubScreen(
    holidays: List<OfficialHoliday>,
    progress: HolidayProgress,
    onSaveHoliday: (OfficialHoliday) -> Unit,
    onDeleteHoliday: (String) -> Unit,
    calculateDaysToCome: (String) -> String,
    modifier: Modifier = Modifier
) {
    var showAddDialog by remember { mutableStateOf(false) }
    var holidayToEdit by remember { mutableStateOf<OfficialHoliday?>(null) }
    var holidayToDelete by remember { mutableStateOf<OfficialHoliday?>(null) }

    val todayStr = SimpleDateFormat("yyyy-MM-dd", Locale.US).format(Date())
    
    val sortedHolidays = holidays.sortedBy { it.startDate }
    val upcoming = sortedHolidays.filter { it.startDate >= todayStr }
    val past = sortedHolidays.filter { it.startDate < todayStr }.sortedByDescending { it.startDate }

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
                Spacer(modifier = Modifier.height(24.dp))
                
                // Progress Section
                HolidayProgressSection(progress)
                
                Spacer(modifier = Modifier.height(24.dp))
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "HOLIDAYS",
                            color = BambooPrimaryGreen,
                            fontFamily = PoppinsFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp,
                            letterSpacing = 1.sp
                        )
                        Text(
                            text = "Academic Calendar 2026",
                            color = BambooTextSecondary,
                            fontFamily = PoppinsFontFamily,
                            fontSize = 12.sp
                        )
                    }
                    
                    // Smaller Add Button
                    Surface(
                        onClick = { showAddDialog = true },
                        color = BambooPrimaryGreen,
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.height(32.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Icon(Icons.Default.Add, null, modifier = Modifier.size(14.dp), tint = BambooBg)
                            Spacer(Modifier.width(4.dp))
                            Text("ADD", color = BambooBg, fontWeight = FontWeight.Bold, fontSize = 11.sp)
                        }
                    }
                }
            }

            if (upcoming.isNotEmpty()) {
                item {
                    Text("UPCOMING BREAKS", color = BambooTextSecondary, fontWeight = FontWeight.Bold, fontSize = 11.sp, letterSpacing = 1.sp)
                }
                items(upcoming, key = { it.id }) { item ->
                    HolidayCard(
                        holiday = item,
                        isPast = false,
                        daysLeft = calculateDaysToCome(item.startDate),
                        onEdit = { holidayToEdit = it },
                        onDelete = { holidayToDelete = it }
                    )
                }
            }

            if (past.isNotEmpty()) {
                item {
                    Text("COMPLETED", color = BambooTextMuted, fontWeight = FontWeight.Bold, fontSize = 11.sp, letterSpacing = 1.sp)
                }
                items(past, key = { it.id }) { item ->
                    HolidayCard(
                        holiday = item,
                        isPast = true,
                        daysLeft = "Completed",
                        onEdit = { holidayToEdit = it },
                        onDelete = { holidayToDelete = it }
                    )
                }
            }

            item { Spacer(Modifier.height(120.dp)) }
        }

        if (showAddDialog) {
            HolidayAddEditDialog(onDismiss = { showAddDialog = false }, onSave = onSaveHoliday)
        }
        if (holidayToEdit != null) {
            HolidayAddEditDialog(holiday = holidayToEdit, onDismiss = { holidayToEdit = null }, onSave = onSaveHoliday)
        }
        if (holidayToDelete != null) {
            AlertDialog(
                onDismissRequest = { holidayToDelete = null },
                containerColor = BambooElevated,
                title = { Text("Delete Holiday?", color = Color.Red, fontWeight = FontWeight.Bold) },
                text = { Text("Are you sure you want to delete '${holidayToDelete?.name}'?", color = BambooTextPrimary) },
                confirmButton = {
                    Button(onClick = { onDeleteHoliday(holidayToDelete!!.id); holidayToDelete = null }, colors = ButtonDefaults.buttonColors(containerColor = Color.Red)) {
                        Text("DELETE", fontWeight = FontWeight.Bold)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { holidayToDelete = null }) { Text("CANCEL", color = BambooTextSecondary) }
                }
            )
        }
    }
}

@Composable
private fun HolidayProgressSection(progress: HolidayProgress) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .background(BambooSurface)
            .border(1.dp, BambooBorder, RoundedCornerShape(18.dp))
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "Year Progress",
                color = BambooTextPrimary,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp
            )
            Text(
                "${(progress.percentage * 100).toInt()}% Done",
                color = BambooPrimaryGreen,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp
            )
        }
        Spacer(modifier = Modifier.height(12.dp))
        LinearProgressIndicator(
            progress = { progress.percentage },
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
                .clip(CircleShape),
            color = BambooPrimaryGreen,
            trackColor = BambooBorder
        )
        Spacer(modifier = Modifier.height(12.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            HolidayStatItem("Total", progress.total.toString(), BambooTextSecondary)
            HolidayStatItem("Completed", progress.completed.toString(), BambooSoftGreen)
            HolidayStatItem("Upcoming", progress.upcoming.toString(), BambooBrightGreen)
        }
    }
}

@Composable
private fun HolidayStatItem(label: String, value: String, color: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(value, color = color, fontWeight = FontWeight.Bold, fontSize = 16.sp)
        Text(label.uppercase(), color = BambooTextMuted, fontWeight = FontWeight.Bold, fontSize = 9.sp, letterSpacing = 0.5.sp)
    }
}

@Composable
private fun HolidayCard(
    holiday: OfficialHoliday,
    isPast: Boolean,
    daysLeft: String,
    onEdit: (OfficialHoliday) -> Unit,
    onDelete: (OfficialHoliday) -> Unit
) {
    val isCustom = holiday.id.startsWith("custom_") || !holiday.id.startsWith("h")

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .background(BambooSurface)
            .border(1.dp, BambooBorder, RoundedCornerShape(18.dp))
            .padding(16.dp)
    ) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.Top) {
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(holiday.name, color = if (isPast) BambooTextMuted else BambooTextPrimary, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    if (isCustom) {
                        Spacer(Modifier.width(8.dp))
                        Box(modifier = Modifier.clip(RoundedCornerShape(4.dp)).background(BambooPrimaryGreen.copy(alpha = 0.2f)).padding(horizontal = 6.dp, vertical = 2.dp)) {
                            Text("CUSTOM", color = BambooPrimaryGreen, fontSize = 8.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
                Text("${holiday.startDate}${if (holiday.endDate != holiday.startDate) " to ${holiday.endDate}" else ""} • ${holiday.dayOfWeek}", color = if (isPast) BambooTextMuted else BambooSoftGreen, fontSize = 12.sp)
                Spacer(Modifier.height(8.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(holiday.category.uppercase(), color = if (isPast) BambooTextMuted else BambooPrimaryGreen, fontWeight = FontWeight.Bold, fontSize = 10.sp)
                    Spacer(Modifier.width(12.dp))
                    Text(daysLeft, color = if (isPast) BambooTextMuted else BambooBrightGreen, fontWeight = FontWeight.SemiBold, fontSize = 11.sp)
                }
            }
            
            var expanded by remember { mutableStateOf(false) }
            Box {
                IconButton(onClick = { expanded = true }, modifier = Modifier.size(24.dp)) {
                    Icon(Icons.Default.MoreVert, null, tint = BambooTextMuted)
                }
                DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }, modifier = Modifier.background(BambooElevated)) {
                    DropdownMenuItem(
                        text = { Text("Edit") },
                        onClick = { expanded = false; onEdit(holiday) },
                        leadingIcon = { Icon(Icons.Default.Edit, null, modifier = Modifier.size(18.dp)) }
                    )
                    DropdownMenuItem(
                        text = { Text("Delete", color = Color.Red) },
                        onClick = { expanded = false; onDelete(holiday) },
                        leadingIcon = { Icon(Icons.Default.Delete, null, tint = Color.Red, modifier = Modifier.size(18.dp)) }
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HolidayAddEditDialog(
    holiday: OfficialHoliday? = null,
    onDismiss: () -> Unit,
    onSave: (OfficialHoliday) -> Unit
) {
    var name by remember { mutableStateOf(holiday?.name ?: "") }
    var startDate by remember { mutableStateOf(holiday?.startDate ?: "") }
    var endDate by remember { mutableStateOf(holiday?.endDate ?: holiday?.startDate ?: "") }
    var category by remember { mutableStateOf(holiday?.category ?: "Festival") }
    var notes by remember { mutableStateOf(holiday?.notes ?: "") }

    val categories = listOf("Festival", "National", "College Holiday", "Exam Holiday", "Personal Holiday", "Gazetted")
    var catExpanded by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = BambooElevated,
        title = { Text(if (holiday == null) "ADD HOLIDAY" else "EDIT HOLIDAY", color = BambooPrimaryGreen, fontWeight = FontWeight.Bold) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Holiday Name") }, modifier = Modifier.fillMaxWidth(), colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = BambooPrimaryGreen))
                OutlinedTextField(value = startDate, onValueChange = { startDate = it }, label = { Text("Start Date (YYYY-MM-DD)") }, placeholder = { Text("e.g. 2026-08-28") }, modifier = Modifier.fillMaxWidth(), colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = BambooPrimaryGreen))
                OutlinedTextField(value = endDate, onValueChange = { endDate = it }, label = { Text("End Date (YYYY-MM-DD)") }, placeholder = { Text("Optional") }, modifier = Modifier.fillMaxWidth(), colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = BambooPrimaryGreen))
                
                Box {
                    OutlinedTextField(value = category, onValueChange = {}, label = { Text("Category") }, modifier = Modifier.fillMaxWidth().clickable { catExpanded = true }, enabled = false, colors = OutlinedTextFieldDefaults.colors(disabledBorderColor = BambooBorder, disabledTextColor = BambooTextPrimary, disabledLabelColor = BambooTextSecondary), trailingIcon = { Icon(Icons.Default.ArrowDropDown, null) })
                    DropdownMenu(expanded = catExpanded, onDismissRequest = { catExpanded = false }, modifier = Modifier.background(BambooElevated)) {
                        categories.forEach { cat -> DropdownMenuItem(text = { Text(cat) }, onClick = { category = cat; catExpanded = false }) }
                    }
                }
                OutlinedTextField(value = notes, onValueChange = { notes = it }, label = { Text("Description (Optional)") }, modifier = Modifier.fillMaxWidth(), colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = BambooPrimaryGreen))
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.US)
                    val dayOfWeek = try {
                        val date = sdf.parse(startDate)
                        SimpleDateFormat("EEEE", Locale.US).format(date!!)
                    } catch (e: Exception) { "Unknown" }
                    onSave(OfficialHoliday(id = holiday?.id ?: "custom_${UUID.randomUUID().toString()}", name = name, startDate = startDate, endDate = if (endDate.isBlank()) startDate else endDate, dayOfWeek = dayOfWeek, category = category, notes = notes))
                    onDismiss()
                },
                enabled = name.isNotBlank() && startDate.isNotBlank(),
                colors = ButtonDefaults.buttonColors(containerColor = BambooPrimaryGreen)
            ) { Text("SAVE", fontWeight = FontWeight.Bold) }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("CANCEL", color = BambooTextSecondary) } }
    )
}
