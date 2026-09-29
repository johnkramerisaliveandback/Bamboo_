package com.example.ui.screens.academics

import android.media.AudioAttributes
import android.media.MediaPlayer
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.Alarm
import com.example.data.SoundLibrary
import com.example.ui.theme.*
import java.util.*
import java.util.concurrent.TimeUnit

@Composable
fun ToolsSubScreen(
    stopwatchTime: Long,
    isStopwatchRunning: Boolean,
    stopwatchLaps: List<Long>,
    onStartStopwatch: () -> Unit,
    onPauseStopwatch: () -> Unit,
    onResetStopwatch: () -> Unit,
    onLapStopwatch: () -> Unit,
    
    timerRemaining: Long,
    isTimerRunning: Boolean,
    onStartTimer: (Long) -> Unit,
    onPauseTimer: () -> Unit,
    onResetTimer: () -> Unit,
    
    alarms: List<Alarm>,
    onSaveAlarm: (Alarm) -> Unit,
    onDeleteAlarm: (String) -> Unit,
    onToggleAlarm: (String) -> Unit
) {
    var selectedToolTab by remember { mutableStateOf(0) }
    val tabs = listOf("Stopwatch", "Timer", "Alarms")

    Column(modifier = Modifier.fillMaxSize()) {
        TabRow(
            selectedTabIndex = selectedToolTab,
            containerColor = BambooSurface,
            contentColor = BambooPrimaryGreen,
            divider = {}
        ) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = selectedToolTab == index,
                    onClick = { selectedToolTab = index },
                    text = { Text(title, fontFamily = PoppinsFontFamily, fontWeight = FontWeight.Bold, fontSize = 12.sp) }
                )
            }
        }

        when (selectedToolTab) {
            0 -> StopwatchView(stopwatchTime, isStopwatchRunning, stopwatchLaps, onStartStopwatch, onPauseStopwatch, onResetStopwatch, onLapStopwatch)
            1 -> TimerView(timerRemaining, isTimerRunning, onStartTimer, onPauseTimer, onResetTimer)
            2 -> AlarmsView(alarms, onSaveAlarm, onDeleteAlarm, onToggleAlarm)
        }
    }
}

@Composable
fun StopwatchView(
    time: Long,
    isRunning: Boolean,
    laps: List<Long>,
    onStart: () -> Unit,
    onPause: () -> Unit,
    onReset: () -> Unit,
    onLap: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = formatTime(time),
            color = BambooPrimaryGreen,
            fontFamily = PoppinsFontFamily,
            fontWeight = FontWeight.Black,
            fontSize = 64.sp
        )
        
        Spacer(modifier = Modifier.height(32.dp))
        
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            IconButton(
                onClick = onReset,
                modifier = Modifier.size(64.dp).clip(CircleShape).background(BambooElevated)
            ) {
                Icon(Icons.Default.RestartAlt, null, tint = Color.White)
            }
            
            IconButton(
                onClick = if (isRunning) onPause else onStart,
                modifier = Modifier.size(80.dp).clip(CircleShape).background(BambooPrimaryGreen)
            ) {
                Icon(if (isRunning) Icons.Default.Pause else Icons.Default.PlayArrow, null, tint = BambooBg, modifier = Modifier.size(40.dp))
            }
            
            IconButton(
                onClick = onLap,
                modifier = Modifier.size(64.dp).clip(CircleShape).background(BambooElevated),
                enabled = isRunning
            ) {
                Icon(Icons.Default.Flag, null, tint = if (isRunning) Color.White else Color.Gray)
            }
        }
        
        Spacer(modifier = Modifier.height(32.dp))
        
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(laps.size) { index ->
                val lapTime = laps[index]
                Row(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Lap ${laps.size - index}", color = BambooTextMuted)
                    Text(formatTime(lapTime), color = Color.White, fontWeight = FontWeight.Bold)
                }
                HorizontalDivider(color = BambooBorder)
            }
        }
    }
}

@Composable
fun TimerView(
    remaining: Long,
    isRunning: Boolean,
    onStart: (Long) -> Unit,
    onPause: () -> Unit,
    onReset: () -> Unit
) {
    var hours by remember { mutableStateOf(0) }
    var minutes by remember { mutableStateOf(0) }
    var seconds by remember { mutableStateOf(0) }

    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (!isRunning && remaining <= 0L) {
            // Picker
            Row(verticalAlignment = Alignment.CenterVertically) {
                NumberPicker(value = hours, onValueChange = { hours = it }, range = 0..23, label = "h")
                Text(":", color = BambooPrimaryGreen, fontSize = 32.sp)
                NumberPicker(value = minutes, onValueChange = { minutes = it }, range = 0..59, label = "m")
                Text(":", color = BambooPrimaryGreen, fontSize = 32.sp)
                NumberPicker(value = seconds, onValueChange = { seconds = it }, range = 0..59, label = "s")
            }
        } else {
            Text(
                text = formatTime(remaining),
                color = BambooPrimaryGreen,
                fontFamily = PoppinsFontFamily,
                fontWeight = FontWeight.Black,
                fontSize = 64.sp
            )
        }
        
        Spacer(modifier = Modifier.height(48.dp))
        
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            IconButton(
                onClick = onReset,
                modifier = Modifier.size(64.dp).clip(CircleShape).background(BambooElevated)
            ) {
                Icon(Icons.Default.RestartAlt, null, tint = Color.White)
            }
            
            IconButton(
                onClick = {
                    if (isRunning) onPause()
                    else {
                        val duration = (hours * 3600 + minutes * 60 + seconds) * 1000L
                        onStart(if (remaining > 0) remaining else duration)
                    }
                },
                modifier = Modifier.size(80.dp).clip(CircleShape).background(BambooPrimaryGreen)
            ) {
                Icon(if (isRunning) Icons.Default.Pause else Icons.Default.PlayArrow, null, tint = BambooBg, modifier = Modifier.size(40.dp))
            }
        }
    }
}

@Composable
fun AlarmsView(
    alarms: List<Alarm>,
    onSave: (Alarm) -> Unit,
    onDelete: (String) -> Unit,
    onToggle: (String) -> Unit
) {
    var showAddDialog by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(alarms) { alarm ->
                AlarmItem(alarm, onToggle, onDelete)
            }
        }
        
        FloatingActionButton(
            onClick = { showAddDialog = true },
            modifier = Modifier.align(Alignment.BottomEnd).padding(24.dp),
            containerColor = BambooPrimaryGreen,
            contentColor = BambooBg
        ) {
            Icon(Icons.Default.Add, "Add Alarm")
        }
    }

    if (showAddDialog) {
        AddAlarmDialog(
            onDismiss = { showAddDialog = false },
            onConfirm = { time, label, soundUrl ->
                onSave(Alarm(UUID.randomUUID().toString(), time, label, soundUri = soundUrl))
                showAddDialog = false
            }
        )
    }
}

@Composable
fun AlarmItem(alarm: Alarm, onToggle: (String) -> Unit, onDelete: (String) -> Unit) {
    Box(
        modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(16.dp)).background(BambooSurface).border(1.dp, BambooBorder, RoundedCornerShape(16.dp)).padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(alarm.time, color = if (alarm.isEnabled) BambooPrimaryGreen else BambooTextMuted, fontSize = 32.sp, fontWeight = FontWeight.Bold)
                Text(alarm.label, color = BambooTextSecondary, fontSize = 14.sp)
            }
            
            Row(verticalAlignment = Alignment.CenterVertically) {
                Switch(
                    checked = alarm.isEnabled,
                    onCheckedChange = { onToggle(alarm.id) },
                    colors = SwitchDefaults.colors(checkedTrackColor = BambooPrimaryGreen)
                )
                Spacer(modifier = Modifier.width(8.dp))
                IconButton(onClick = { onDelete(alarm.id) }) {
                    Icon(Icons.Default.Delete, null, tint = Color.Gray)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddAlarmDialog(onDismiss: () -> Unit, onConfirm: (String, String, String?) -> Unit) {
    val context = LocalContext.current
    val state = rememberTimePickerState()
    var label by remember { mutableStateOf("") }
    var selectedSoundUrl by remember { mutableStateOf<String?>(null) }
    var soundDropdownExpanded by remember { mutableStateOf(false) }
    
    var previewPlayer by remember { mutableStateOf<MediaPlayer?>(null) }

    DisposableEffect(Unit) {
        onDispose {
            previewPlayer?.release()
            previewPlayer = null
        }
    }

    fun playPreview(url: String?) {
        previewPlayer?.stop()
        previewPlayer?.release()
        previewPlayer = null
        
        if (url == null) return
        
        try {
            previewPlayer = MediaPlayer().apply {
                setDataSource(context, Uri.parse(url))
                setAudioAttributes(
                    AudioAttributes.Builder()
                        .setUsage(AudioAttributes.USAGE_ALARM)
                        .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                        .build()
                )
                prepareAsync()
                setOnPreparedListener { start() }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    AlertDialog(
        onDismissRequest = {
            previewPlayer?.stop()
            onDismiss()
        },
        containerColor = BambooSurface,
        confirmButton = {
            TextButton(onClick = {
                previewPlayer?.stop()
                val time = String.format(Locale.US, "%02d:%02d", state.hour, state.minute)
                onConfirm(time, label.ifBlank { "Alarm" }, selectedSoundUrl)
            }) {
                Text("SAVE", color = BambooPrimaryGreen)
            }
        },
        dismissButton = {
            TextButton(onClick = {
                previewPlayer?.stop()
                onDismiss()
            }) { Text("CANCEL", color = BambooTextMuted) }
        },
        title = { Text("Set Alarm", color = Color.White) },
        text = {
            Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                TimePicker(state = state, colors = TimePickerDefaults.colors(
                    clockDialColor = BambooElevated,
                    selectorColor = BambooPrimaryGreen,
                    containerColor = BambooSurface
                ))
                Spacer(modifier = Modifier.height(16.dp))
                OutlinedTextField(
                    value = label,
                    onValueChange = { label = it },
                    label = { Text("Label") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = BambooPrimaryGreen)
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Sound Selector
                Text("ALARM SOUND", color = BambooTextMuted, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                Box(modifier = Modifier.fillMaxWidth()) {
                    OutlinedButton(
                        onClick = { soundDropdownExpanded = true },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, BambooBorder)
                    ) {
                        val currentSoundName = SoundLibrary.ALARM_SOUNDS.find { it.url == selectedSoundUrl }?.name ?: "Default"
                        Text(currentSoundName, color = Color.White)
                        Icon(Icons.Default.ArrowDropDown, null, tint = BambooPrimaryGreen)
                    }
                    
                    DropdownMenu(
                        expanded = soundDropdownExpanded,
                        onDismissRequest = { soundDropdownExpanded = false },
                        modifier = Modifier.background(BambooElevated)
                    ) {
                        SoundLibrary.ALARM_SOUNDS.forEach { sound ->
                            DropdownMenuItem(
                                text = { Text(sound.name, color = Color.White) },
                                onClick = {
                                    selectedSoundUrl = sound.url
                                    soundDropdownExpanded = false
                                    playPreview(sound.url)
                                }
                            )
                        }
                    }
                }
            }
        }
    )
}

@Composable
fun NumberPicker(value: Int, onValueChange: (Int) -> Unit, range: IntRange, label: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        IconButton(onClick = { if (value < range.last) onValueChange(value + 1) }) {
            Icon(Icons.Default.ArrowDropUp, null, tint = BambooPrimaryGreen)
        }
        Text(String.format(Locale.US, "%02d%s", value, label), color = Color.White, fontSize = 24.sp, fontWeight = FontWeight.Bold)
        IconButton(onClick = { if (value > range.first) onValueChange(value - 1) }) {
            Icon(Icons.Default.ArrowDropDown, null, tint = BambooPrimaryGreen)
        }
    }
}

private fun formatTime(millis: Long): String {
    val hours = TimeUnit.MILLISECONDS.toHours(millis)
    val minutes = TimeUnit.MILLISECONDS.toMinutes(millis) % 60
    val seconds = TimeUnit.MILLISECONDS.toSeconds(millis) % 60
    val hundreds = (millis % 1000) / 10
    
    return if (hours > 0) {
        String.format(Locale.US, "%02d:%02d:%02d", hours, minutes, seconds)
    } else {
        String.format(Locale.US, "%02d:%02d.%02d", minutes, seconds, hundreds)
    }
}
