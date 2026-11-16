package org.example.app.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow

// نوٹیفیکیشن کی قسم
enum class NotificationType {
    LIKE, COMMENT, FOLLOW, UPLOAD
}

// نوٹیفیکیشن کا ماڈل
data class Notification(
    val id: String,
    val type: NotificationType,
    val title: String,
    val message: String,
    val timeAgo: String,
    var isRead: Boolean = false
)

// ڈمی نوٹیفیکیشنز
val dummyNotifications = listOf(
    Notification("1", NotificationType.LIKE, "Sarah Khan", "liked your video 'Kotlin Multiplatform Tutorial'", "2m ago"),
    Notification("2", NotificationType.COMMENT, "Ali Ahmed", "commented: 'Great tutorial! Very helpful.'", "15m ago"),
    Notification("3", NotificationType.FOLLOW, "Tech World", "started following you", "1h ago"),
    Notification("4", NotificationType.UPLOAD, "Android Dev", "uploaded: 'Jetpack Compose Advanced'", "3h ago"),
    Notification("5", NotificationType.LIKE, "Maria Garcia", "and 12 others liked your video", "5h ago"),
    Notification("6", NotificationType.COMMENT, "John Smith", "replied to your comment", "1d ago"),
    Notification("7", NotificationType.FOLLOW, "Code Academy", "started following you", "2d ago")
)

class NotificationsScreen : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        
        // نوٹیفیکیشنز کی mutable list (تاکہ "mark as read" کام کرے)
        val notifications = remember {
            mutableStateListOf<Notification>().apply {
                addAll(dummyNotifications)
            }
        }
        
        val snackbarHostState = remember { SnackbarHostState() }

        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            "Notifications",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = { navigator.pop() }) {
                            Icon(
                                Icons.Default.ArrowBack,
                                contentDescription = "Back",
                                tint = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    },
                    actions = {
                        // "Mark all as read" بٹن
                        TextButton(onClick = {
                            notifications.forEach { it.isRead = true }
                        }) {
                            Text(
                                "Mark all read",
                                color = MaterialTheme.colorScheme.primary,
                                fontSize = 14.sp
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    )
                )
            },
            snackbarHost = { SnackbarHost(snackbarHostState) },
            containerColor = MaterialTheme.colorScheme.background
        ) { padding ->
            if (notifications.isEmpty()) {
                // Empty State
                Box(
                    modifier = Modifier.fillMaxSize().padding(padding),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            Icons.Default.Notifications,
                            contentDescription = null,
                            modifier = Modifier.size(64.dp),
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            "No notifications yet",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            "When you get notifications, they'll show up here",
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            } else {
                // نوٹیفیکیشنز کی لسٹ
                LazyColumn(
                    modifier = Modifier.fillMaxSize().padding(padding),
                    contentPadding = PaddingValues(vertical = 8.dp)
                ) {
                    items(notifications) { notification ->
                        NotificationItem(
                            notification = notification,
                            onClick = {
                                notification.isRead = true
                            }
                        )
                        HorizontalDivider(
                            modifier = Modifier.padding(horizontal = 16.dp),
                            color = MaterialTheme.colorScheme.outline.copy(alpha = 0.1f)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun NotificationItem(
    notification: Notification,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                if (notification.isRead)
                    MaterialTheme.colorScheme.surface
                else
                    MaterialTheme.colorScheme.primary.copy(alpha = 0.05f)
            )
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.Top
    ) {
        // آئیکن
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(getNotificationColor(notification.type)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = getNotificationIcon(notification.type),
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(24.dp)
            )
        }
        
        Spacer(modifier = Modifier.width(12.dp))
        
        // مواد
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = notification.title,
                fontSize = 15.sp,
                fontWeight = if (notification.isRead) FontWeight.Normal else FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = notification.message,
                fontSize = 13.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 2
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = notification.timeAgo,
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
            )
        }
        
        // Unread indicator (چھوٹا سا ڈاٹ)
        if (!notification.isRead) {
            Spacer(modifier = Modifier.width(8.dp))
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary)
            )
        }
    }
}

// نوٹیفیکیشن کی قسم کے مطابق آئیکن
private fun getNotificationIcon(type: NotificationType): ImageVector {
    return when (type) {
        NotificationType.LIKE -> Icons.Default.Favorite
        NotificationType.COMMENT -> Icons.Default.Comment
        NotificationType.FOLLOW -> Icons.Default.PersonAdd
        NotificationType.UPLOAD -> Icons.Default.PlayCircle
    }
}

// نوٹیفیکیشن کی قسم کے مطابق رنگ
private fun getNotificationColor(type: NotificationType): Color {
    return when (type) {
        NotificationType.LIKE -> Color(0xFFE53935)      // Red
        NotificationType.COMMENT -> Color(0xFF1E88E5)  // Blue
        NotificationType.FOLLOW -> Color(0xFF43A047)   // Green
        NotificationType.UPLOAD -> Color(0xFFFFA726)   // Orange
    }
}
