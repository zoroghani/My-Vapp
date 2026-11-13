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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import io.github.vinceglb.filekit.compose.rememberFilePickerLauncher
import io.github.vinceglb.filekit.core.PickerType
import org.example.app.DummyVideo
import org.example.app.dummyVideos

class VideoListScreen : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        
        val picker = rememberFilePickerLauncher(
            type = PickerType.Video,
            title = "ویڈیو منتخب کریں"
        ) { file ->
            // 👇 یہاں Null Check لگا دیا ہے تاکہ ایرر نہ آئے
            if (file != null && file.path != null) {
                navigator.push(VideoPlayerScreen(file.path!!, "آپ کی اپ لوڈ کردہ ویڈیو"))
            }
        }

        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("میری ویڈیو ایپ") },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color.Transparent
                    )
                )
            },
            containerColor = Color(0xFFE8EAF6), 
            floatingActionButton = {
                FloatingActionButton(
                    onClick = { picker.launch() },
                    containerColor = Color.White.copy(alpha = 0.6f),
                    contentColor = Color(0xFF6200EE)
                ) {
                    Text("➕ اپ لوڈ")
                }
            }
        ) { padding ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(dummyVideos) { video ->
                    VideoCard(video = video) {
                        navigator.push(VideoPlayerScreen(video.thumbnailUrl, video.title))
                    }
                }
            }
        }
    }
}

@Composable
fun VideoCard(video: DummyVideo, onClick: () -> Unit) {
    Card(
        onClick = onClick,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White.copy(alpha = 0.4f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        border = BorderStroke(1.dp, Color.White.copy(alpha = 0.7f))
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
                    .background(Color(0xFFE0E7FF).copy(alpha = 0.5f)),
                contentAlignment = Alignment.Center
            ) {
                Text("🖼️ Thumbnail", color = Color(0xFF312E81))
            }
            Column(modifier = Modifier.padding(12.dp)) {
                Text(
                    text = video.title, 
                    style = MaterialTheme.typography.titleMedium, 
                    maxLines = 2,
                    color = Color(0xFF1F2937)
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
