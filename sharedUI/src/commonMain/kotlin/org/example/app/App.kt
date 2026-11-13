package org.example.app

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.example.app.core.platform.VideoPlayerView

// 1. ڈیٹا ماڈل
data class DummyVideo(
    val id: String,
    val title: String,
    val channel: String,
    val thumbnailUrl: String
)

// 2. ڈمی ویڈیوز کی لسٹ
val dummyVideos = listOf(
    DummyVideo("1", "Kotlin Multiplatform Tutorial", "Tech Channel", "https://picsum.photos/seed/video1/500/300"),
    DummyVideo("2", "Build Your First App in 10 Mins", "Coding Life", "https://picsum.photos/seed/video2/500/300"),
    DummyVideo("3", "Compose UI Basics for Beginners", "Android Dev", "https://picsum.photos/seed/video3/500/300")
)

// 3. 100% ورکنگ ٹیسٹ ویڈیو URL (HTTPS)
val sampleVideoUrl = "https://storage.googleapis.com/exoplayer-test-media-0/BigBuckBunny_320x180.mp4"

// 4. مین ایپ فنکشن
@Composable
fun App(modifier: Modifier = Modifier) {
    MaterialTheme {
        Surface(
            modifier = modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            VideoPlayerDemoScreen()
        }
    }
}

// 5. مین اسکرین
@Composable
fun VideoPlayerDemoScreen() {
    Scaffold(
        topBar = {
            TopAppBar(title = { Text("میری ویڈیو ایپ") })
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            // 👇 ویڈیو پلیر (isPlaying = true رکھا ہے تاکہ فوراً لوڈ ہو جائے)
            VideoPlayerView(
                videoUrl = sampleVideoUrl,
                isPlaying = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp)
            )

            // لسٹ کا ہیڈر
            Text(
                text = "تجویز کردہ ویڈیوز",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )

            // ویڈیو لسٹ
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(dummyVideos) { video ->
                    VideoCard(video = video)
                }
            }
        }
    }
}

// 6. ویڈیو کارڈ (UI)
@Composable
fun VideoCard(video: DummyVideo) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
                    .background(MaterialTheme.colorScheme.primaryContainer),
                contentAlignment = Alignment.Center
            ) {
                Text("🖼️ Thumbnail", color = MaterialTheme.colorScheme.onPrimaryContainer)
            }

            Column(modifier = Modifier.padding(12.dp)) {
                Text(
                    text = video.title,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 2
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = video.channel,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}
