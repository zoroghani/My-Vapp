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
import io.github.vinceglb.filekit.compose.rememberFilePickerLauncher
import io.github.vinceglb.filekit.core.PickerType

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
fun VideoPlayerDemoScreen() {
    // 1. FileKit کا لانچر یاد رکھیں (Remember)
    val picker = rememberFilePickerLauncher(
        type = PickerType.Video, // صرف ویڈیوز دکھائے گا
        title = "ویڈیو منتخب کریں"
    ) { file ->
        if (file != null) {
            // جب صارف ویڈیو منتخب کر لے، تو اس کا پاتھ یہاں مل جائے گا
            println("✅ منتخب شدہ ویڈیو: ${file.path}")
            selectedVideoUrl = file.path // اسے نیچے والے متغیر میں محفوظ کریں
        }
    }

    // 2. منتخب شدہ ویڈیو کا پاتھ (ڈیفالٹ طور پر ہماری ٹیسٹ ویڈیو)
    var selectedVideoUrl by remember { mutableStateOf(sampleVideoUrl) }

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
            // 3. ویڈیو پلیر (اب یہ ڈیفالٹ یا منتخب شدہ ویڈیو چلائے گا)
            VideoPlayerView(
                videoUrl = selectedVideoUrl,
                isPlaying = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp)
            )

            // 4. ویڈیو اپ لوڈ / منتخب کرنے کا بٹن
            Button(
                onClick = { picker.launch() }, // 👈 یہ بٹن دبائیں گے تو گیلری کھلے گی
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text("📁 Select video")
            }

            // لسٹ کا ہیڈر
            Text(
                text = "Recommended videos",
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
