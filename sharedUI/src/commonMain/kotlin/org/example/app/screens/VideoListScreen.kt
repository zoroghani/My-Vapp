package org.example.app.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
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
import io.github.vinceglb.filekit.compose.rememberFilePickerLauncher
import io.github.vinceglb.filekit.core.PickerType
import org.example.app.DummyVideo
import org.example.app.dummyVideos
   import br.com.devsrsouza.compose.icons.feather.Feather

// 👇 composeIcons کے imports (اگر آپ کی لائبریری کا پیکج تھوڑا مختلف ہے تو IDE خود ٹھیک کر دے گا)
import com.devicons.compose.feather.Feather

class VideoListScreen : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        var selectedTab by remember { mutableStateOf(0) } // Bottom Nav کے لیے اسٹیٹ

        // فائل پکر لانچر
        val picker = rememberFilePickerLauncher(
            type = PickerType.Video,
            title = "ویڈیو منتخب کریں"
        ) { file ->
            val videoPath = file?.path
            if (videoPath != null) {
                navigator.push(VideoPlayerScreen(videoPath, "آپ کی اپ لوڈ کردہ ویڈیو"))
            }
        }

        Scaffold(
            // 1️⃣ ٹاپ بار: لوگو + سرچ + نوٹیفکیشن
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
                        IconButton(onClick = {
                           navigator.push(SearchScreen())
                        }) {
                            Icon(
                                imageVector = Feather.Search,
                                contentDescription = "Search",
                                tint = Color(0xFF1F2937)
                            )
                        }
                        IconButton(onClick = { /* نوٹیفکیشن اسکرین پر جائیں */ }) {
                            Icon(
                                imageVector = Feather.Bell,
                                contentDescription = "Notifications",
                                tint = Color(0xFF1F2937)
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color(0xFFE8EAF6).copy(alpha = 0.8f)
                    )
                )
            },
            // 2️⃣ بوٹم نیویگیشن بار
            bottomBar = {
                NavigationBar(
                    containerColor = Color.White.copy(alpha = 0.8f),
                    tonalElevation = 0.dp
                ) {
                    NavigationBarItem(
                        icon = { Icon(Feather.Home, contentDescription = "Home") },
                        label = { Text("Home", fontSize = 10.sp) },
                        selected = selectedTab == 0,
                        onClick = { selectedTab = 0 }
                    )
                    NavigationBarItem(
                        icon = { Icon(Feather.Video, contentDescription = "Reels") },
                        label = { Text("Reels", fontSize = 10.sp) },
                        selected = selectedTab == 1,
                        onClick = { selectedTab = 1 }
                    )
                    NavigationBarItem(
                        icon = { 
                            Icon(
                                Feather.Plus, 
                                contentDescription = "Add",
                                modifier = Modifier.size(28.dp) // تھوڑا بڑا تاکہ نمایاں ہو
                            ) 
                        },
                        label = { },
                        selected = false,
                        onClick = { 
                            selectedTab = 2
                            picker.launch() // 👈 یہاں اپ لوڈ پکر کھلے گا
                        }
                    )
                    NavigationBarItem(
                        icon = { Icon(Feather.Mail, contentDescription = "Inbox") },
                        label = { Text("Inbox", fontSize = 10.sp) },
                        selected = selectedTab == 3,
                        onClick = { selectedTab = 3 }
                    )
                    NavigationBarItem(
                        icon = { Icon(Feather.User, contentDescription = "Profile") },
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

// 3️⃣ گلاس اسٹائل ویڈیو کارڈ (تھمب نیل کے ساتھ)
@Composable
fun VideoCard(video: DummyVideo, onClick: () -> Unit) {
    Card(
        onClick = onClick,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White.copy(alpha = 0.5f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        border = BorderStroke(1.dp, Color.White.copy(alpha = 0.8f))
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            // ✅ اصل تھمب نیل تصویر (Coil کے ذریعے)
            AsyncImage(
                model = video.thumbnailUrl,
                contentDescription = video.title,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)),
                contentScale = ContentScale.Crop
            )
            
            Column(modifier = Modifier.padding(12.dp)) {
                Text(
                    text = video.title,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 2,
                    color = Color(0xFF1F2937),
                    fontWeight = FontWeight.Medium
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = video.channel,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color(0xFF4B5563)
                )
            }
        }
    }
}
