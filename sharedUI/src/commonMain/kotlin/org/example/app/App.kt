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
import org.example.app.core.platform.VideoPlayerView // ہمارا نیا کمپوننٹ

// 1. ڈیٹا ماڈل
data class DummyVideo(
    val id: String,
    val title: String,
    val channel: String,
    val thumbnailUrl: String
)

val dummyVideos = listOf(
    DummyVideo("1", "Kotlin Multiplatform Tutorial", "Tech Channel", "https://picsum.photos/seed/video1/500/300"),
    DummyVideo("2", "Build Your First App in 10 Mins", "Coding Life", "https://picsum.photos/seed/video2/500/300"),
    DummyVideo("3", "Compose UI Basics for Beginners", "Android Dev", "https://picsum.photos/seed/video3/500/300")
)

// 2. مین ایپ فنکشن
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

// 3. اسکرین جس میں پلیر اور لسٹ دونوں ہیں
@Composable
fun VideoPlayerDemoScreen() {
    var isPlaying by remember { mutableStateOf(false) }
    
    // ایک مفت اور اوپن سورس ٹیسٹ ویڈیو (Big Buck Bunny)
    val sampleVideoUrl = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4"

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("My Video") })
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            // 👇 یہاں ہمارا VideoPlayerView استعمال ہو رہا ہے
            VideoPlayerView(
                videoUrl = sampleVideoUrl,
                isPlaying = isPlaying,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp)
            )

            // پلے / پوز بٹن
            Button(
                onClick = { isPlaying = !isPlaying },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(if (isPlaying) "Pause ⏸️" else "Play ▶️")
            }

            // ویڈیو لسٹ کا ہیڈر
            Text(
                text = "Recommended Videos",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )

            // لسٹ
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

// 4. ویڈیو کارڈ (Placeholder کے ساتھ تاکہ UI ٹوٹے نہیں)
@Composable
fun VideoCard(video: DummyVideo) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            // عارضی تھمب نیل (جب تک Coil مکمل کنفیگر نہ ہو)
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
