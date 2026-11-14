package org.example.app.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import coil3.compose.AsyncImage
import org.example.app.DummyVideo
import org.example.app.dummyVideos
import org.example.app.core.platform.rememberVideoPicker

class VideoListScreen : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        var selectedTab by remember { mutableStateOf(0) }

        val launchVideoPicker = rememberVideoPicker { videoPath ->
            navigator.push(VideoPlayerScreen(videoPath, "آپ کی اپ لوڈ کردہ ویڈیو"))
        }

        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            text = "LOG",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 2.sp,
                            color = Color(0xFF6200EE)
                        )
                    },
                    actions = {
                        IconButton(onClick = { navigator.push(SearchScreen()) }) {
                            Icon(Icons.Default.Search, contentDescription = "Search", tint = Color(0xFF1F2937))
                        }
                        IconButton(onClick = { /* Notifications */ }) {
                            Icon(Icons.Default.Notifications, contentDescription = "Notifications", tint = Color(0xFF1F2937))
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFFE8EAF6).copy(alpha = 0.6f))
                )
            },
            bottomBar = {
                NavigationBar(
                    containerColor = Color.White.copy(alpha = 0.6f),
                    tonalElevation = 0.dp
                ) {
                    NavigationBarItem(
                        icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
                        label = { Text("Home", fontSize = 10.sp) },
                        selected = selectedTab == 0,
                        onClick = { selectedTab = 0 }
                    )
                    NavigationBarItem(
                        icon = { Icon(Icons.Default.PlayCircle, contentDescription = "Reels") },
                        label = { Text("Clips", fontSize = 10.sp) },
                        selected = selectedTab == 1,
                        onClick = { selectedTab = 1 }
                    )
                    NavigationBarItem(
                        icon = { Icon(Icons.Default.Add, contentDescription = "Add", modifier = Modifier.size(28.dp)) },
                        label = { },
                        selected = false,
                        onClick = { 
                            selectedTab = 2
                            launchVideoPicker() // 👈 یہاں نیا فنکشن کال ہوگا (picker.launch() نہیں)
                        }
                    )
                    NavigationBarItem(
                        icon = { Icon(Icons.Default.Mail, contentDescription = "Inbox") },
                        label = { Text("Inbox", fontSize = 10.sp) },
                        selected = selectedTab == 3,
                        onClick = { selectedTab = 3 }
                    )
                    NavigationBarItem(
                        icon = { Icon(Icons.Default.Person, contentDescription = "Profile") },
                        label = { Text("Profile", fontSize = 10.sp) },
                        selected = selectedTab == 4,
                        onClick = { selectedTab = 4 }
                    )
                }
            },
            containerColor = Color(0xFFE8EAF6)
        ) { padding ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(dummyVideos) { video ->
                    VideoCard(video = video) {
                        navigator.push(VideoPlayerScreen(video.videoUrl, video.title))
                    }
                }
            }
        }
    }
}
