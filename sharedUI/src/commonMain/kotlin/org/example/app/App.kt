package org.example.app

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.navigator.Navigator
import org.example.app.screens.VideoListScreen

// 1. ڈیٹا ماڈل (یہاں ہونا ضروری ہے)
data class DummyVideo(
    val id: String,
    val title: String,
    val channel: String,
    val thumbnailUrl: String
)

// 2. ڈمی ڈیٹا لسٹ (یہاں ہونا ضروری ہے)
val dummyVideos = listOf(
    DummyVideo("1", "Kotlin Multiplatform Tutorial", "Tech Channel",
        "https://picsum.photos/seed/video1/500/300",
        "https://storage.googleapis.com/exoplayer-test-media-0/BigBuckBunny_320x180.mp4"),
    DummyVideo("2", "Build Your First App in 10 Mins", "Coding Life",
        "https://picsum.photos/seed/video2/500/300",
        "https://storage.googleapis.com/exoplayer-test-media-0/ForBiggerBlazes.mp4"),
    DummyVideo("3", "Compose UI Basics for Beginners", "Android Dev",
        "https://picsum.photos/seed/video3/500/300",
        "https://storage.googleapis.com/exoplayer-test-media-0/ForBiggerEscapes.mp4")
)

// 3. مین ایپ فنکشن
@Composable
fun App(modifier: Modifier = Modifier) {
    MaterialTheme {
        Surface(modifier = modifier) {
            Navigator(VideoListScreen())
        }
    }
}
