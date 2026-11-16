package org.example.app.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow

// 🎯 ایک جنرک ComingSoon سکرین جو title اور icon لیتی ہے
class ComingSoonScreen(
    private val title: String,
    private val icon: ImageVector,
    private val message: String = "This feature is coming soon in the next update. Stay tuned!"
) : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow

        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            title,
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
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    )
                )
            },
            containerColor = MaterialTheme.colorScheme.background
        ) { padding ->
            Box(
                modifier = Modifier.fillMaxSize().padding(padding).padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // بڑا آئیکن (گول پس منظر کے ساتھ)
                    Box(
                        modifier = Modifier.size(120.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Surface(
                            modifier = Modifier.fillMaxSize(),
                            shape = MaterialTheme.shapes.extraLarge,
                            color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
                        ) {}
                        Icon(
                            imageVector = icon,
                            contentDescription = null,
                            modifier = Modifier.size(64.dp),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }

                    // "Coming Soon" ٹائٹل
                    Text(
                        text = "Coming Soon",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    // پیغام
                    Text(
                        text = message,
                        fontSize = 15.sp,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Notify Me بٹن
                    OutlinedButton(
                        onClick = {
                            // بعد میں notification logic
                        },
                        shape = MaterialTheme.shapes.medium
                    ) {
                        Icon(Icons.Default.Notifications, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Notify me when ready", fontWeight = FontWeight.Medium)
                    }
                }
            }
        }
    }
}

// 🎬 Reels Screen — ComingSoonScreen سے بنی ہوئی
class ReelsScreen : Screen {
    private val inner = ComingSoonScreen(
        title = "Reels",
        icon = Icons.Default.PlayCircle,
        message = "Short-form vertical videos are coming in Version 2.0. Swipe, watch, and enjoy!"
    )
    
    @Composable
    override fun Content() = inner.Content()
}

// 📬 Inbox Screen — ComingSoonScreen سے بنی ہوئی
class InboxScreen : Screen {
    private val inner = ComingSoonScreen(
        title = "Inbox",
        icon = Icons.Default.Mail,
        message = "Direct messages and private chats will be available in the next major update."
    )
    
    @Composable
    override fun Content() = inner.Content()
}
