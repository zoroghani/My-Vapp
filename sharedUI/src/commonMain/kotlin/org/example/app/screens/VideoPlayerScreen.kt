package org.example.app.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import org.example.app.core.platform.VideoPlayerView

// یہ اسکرین ویڈیو کا URL اور ٹائٹل پیرامیٹر کے طور پر لیتی ہے
class VideoPlayerScreen(
    private val videoUrl: String,
    private val videoTitle: String
) : Screen {
    
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow

        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(videoTitle) },
                    navigationIcon = {
                        IconButton(onClick = { navigator.pop() }) { // 👈 واپس جانے کا بٹن
                            Text("⬅️") 
                        }
                    }
                )
            }
        ) { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
            ) {
                // ویڈیو پلیر
                VideoPlayerView(
                    videoUrl = videoUrl,
                    isPlaying = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(250.dp)
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                Text(
                    text = "ویڈیو کی تفصیلات یہاں آئیں گی...",
                    modifier = Modifier.padding(16.dp),
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    }
}
