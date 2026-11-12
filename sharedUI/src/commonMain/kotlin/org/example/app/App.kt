package org.example.app

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.layout.ContentScale
import coil3.compose.AsyncImage

data class DummyVideo(
    val id: String,
    val title: String,
    val channel: String,
    val thumbnailUrl: String
)

val dummyVideos = listOf(
    DummyVideo("1", "Kotlin Multiplatform Tutorial", "Tech Channel", "https://images.unsplash.com/photo-1587620962725-abab7fe55159?w=500&q=80"),
    DummyVideo("2", "Build Your First App in 10 Mins", "Coding Life", "https://images.unsplash.com/photo-1555066931-4365d14bab8c?w=500&q=80"),
    DummyVideo("3", "Compose UI Basics for Beginners", "Android Dev", "https://images.unsplash.com/photo-1526374965328-7f61d4dc18c5?w=500&q=80")
)

@Composable
fun App(modifier: Modifier = Modifier) {
    MaterialTheme {
        Surface(
            modifier = modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            VideoListScreen()
        }
    }
}

@Composable
fun VideoListScreen() {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("My Video") }
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(dummyVideos) { video ->
                VideoCard(video = video)
            }
        }
    }
}

@Composable
fun VideoCard(video: DummyVideo) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            
            AsyncImage(
                model = video.thumbnailUrl,
                contentDescription = "Video Thumbnail",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
            )
            
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
