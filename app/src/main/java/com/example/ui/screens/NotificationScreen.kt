package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.AcademicNotification
import com.example.data.NotificationType
import com.example.ui.theme.*
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationScreen(
    notifications: List<AcademicNotification>,
    onMarkRead: (String) -> Unit,
    onMarkAllRead: () -> Unit,
    onDelete: (String) -> Unit,
    onViewRequest: (String) -> Unit,
    onBack: () -> Unit
) {
    val groupedNotifications = remember(notifications) {
        notifications.groupBy { formatGroupDate(it.timestamp) }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BambooBg)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = BambooTextPrimary)
                    }
                    Text(
                        text = "NOTIFICATIONS",
                        color = BambooTextPrimary,
                        fontFamily = PoppinsFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                }
                
                if (notifications.any { !it.isRead }) {
                    TextButton(onClick = onMarkAllRead) {
                        Text("Mark all as read", color = BambooPrimaryGreen, fontSize = 12.sp)
                    }
                }
            }

            if (notifications.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            Icons.Default.Notifications,
                            contentDescription = null,
                            modifier = Modifier.size(64.dp),
                            tint = BambooElevated
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text("No notifications yet", color = BambooTextMuted, fontSize = 14.sp)
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    groupedNotifications.forEach { (dateLabel, notifs) ->
                        item {
                            Text(
                                text = dateLabel,
                                color = BambooPrimaryGreen,
                                fontFamily = PoppinsFontFamily,
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp,
                                modifier = Modifier.padding(bottom = 8.dp)
                            )
                        }
                        
                        items(notifs, key = { it.id }) { notification ->
                            val dismissState = rememberSwipeToDismissBoxState(
                                confirmValueChange = {
                                    if (it == SwipeToDismissBoxValue.EndToStart || it == SwipeToDismissBoxValue.StartToEnd) {
                                        onDelete(notification.id)
                                        true
                                    } else false
                                }
                            )

                            SwipeToDismissBox(
                                state = dismissState,
                                backgroundContent = {
                                    val color = when (dismissState.dismissDirection) {
                                        SwipeToDismissBoxValue.EndToStart, SwipeToDismissBoxValue.StartToEnd -> Color.Red.copy(alpha = 0.5f)
                                        else -> Color.Transparent
                                    }
                                    Box(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .clip(RoundedCornerShape(16.dp))
                                            .background(color)
                                            .padding(horizontal = 20.dp),
                                        contentAlignment = Alignment.CenterEnd
                                    ) {
                                        Icon(Icons.Default.Delete, contentDescription = "Delete", tint = Color.White)
                                    }
                                }
                            ) {
                                NotificationItem(
                                    notification = notification,
                                    onClick = { 
                                        if (!notification.isRead) onMarkRead(notification.id)
                                        if (notification.type == NotificationType.PARENT_CONNECTION_REQUEST) {
                                            onViewRequest(notification.relatedId ?: "")
                                        }
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun NotificationItem(
    notification: AcademicNotification,
    onClick: () -> Unit
) {
    val backgroundColor = if (notification.isRead) BambooSurface else BambooElevated
    val iconColor = when (notification.type) {
        NotificationType.PARENT_CONNECTION_REQUEST -> Color(0xFF2196F3)
        NotificationType.PARENT_ACCEPTED -> BambooPrimaryGreen
        NotificationType.PARENT_REJECTED -> Color(0xFFF44336)
        NotificationType.CLASS_REMINDER -> Color(0xFFFFC107)
        else -> BambooPrimaryGreen
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(backgroundColor)
            .clickable(onClick = onClick)
            .padding(16.dp)
    ) {
        Row(verticalAlignment = Alignment.Top) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(iconColor.copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = when (notification.type) {
                        NotificationType.PARENT_CONNECTION_REQUEST -> "👨‍👩‍👦"
                        NotificationType.PARENT_ACCEPTED -> "✅"
                        NotificationType.PARENT_REJECTED -> "❌"
                        NotificationType.CLASS_REMINDER -> "📚"
                        else -> "🔔"
                    },
                    fontSize = 18.sp
                )
            }
            
            Spacer(modifier = Modifier.width(16.dp))
            
            Column(modifier = Modifier.weight(1f)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = notification.title,
                        color = if (notification.isRead) BambooTextSecondary else BambooTextPrimary,
                        fontWeight = if (notification.isRead) FontWeight.Bold else FontWeight.Black,
                        fontSize = 14.sp
                    )
                    
                    if (!notification.isRead) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .clip(CircleShape)
                                .background(BambooPrimaryGreen)
                        )
                    }
                }
                
                Text(
                    text = notification.body,
                    color = BambooTextSecondary,
                    fontSize = 12.sp,
                    lineHeight = 18.sp,
                    modifier = Modifier.padding(vertical = 4.dp)
                )

                if (notification.type == NotificationType.PARENT_CONNECTION_REQUEST) {
                    Button(
                        onClick = onClick,
                        modifier = Modifier.padding(top = 8.dp).height(32.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = BambooPrimaryGreen),
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 0.dp),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("VIEW REQUEST", fontSize = 10.sp, fontWeight = FontWeight.Bold)
                    }
                }
                
                Spacer(modifier = Modifier.height(4.dp))
                
                Text(
                    text = formatTimeOnly(notification.timestamp),
                    color = BambooTextMuted,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

private fun formatGroupDate(timestamp: Long): String {
    val date = Date(timestamp)
    val now = Date()
    val sdf = SimpleDateFormat("yyyyMMdd", Locale.US)
    val dateStr = sdf.format(date)
    val todayStr = sdf.format(now)
    
    val yesterday = Calendar.getInstance().apply { add(Calendar.DAY_OF_YEAR, -1) }.time
    val yesterdayStr = sdf.format(yesterday)
    
    return when (dateStr) {
        todayStr -> "TODAY"
        yesterdayStr -> "YESTERDAY"
        else -> SimpleDateFormat("MMM d, yyyy", Locale.US).format(date).uppercase()
    }
}

private fun formatTimeOnly(timestamp: Long): String {
    return SimpleDateFormat("h:mm a", Locale.US).format(Date(timestamp))
}
