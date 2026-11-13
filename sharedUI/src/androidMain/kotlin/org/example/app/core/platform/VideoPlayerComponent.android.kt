package org.example.app.core.platform

import android.view.ViewGroup
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView

@Composable
actual fun VideoPlayerView(
    videoUrl: String,
    isPlaying: Boolean,
    modifier: Modifier
) {
    val context = LocalContext.current
    
    // 1. ExoPlayer کو ایک بار بنائیں اور یاد رکھیں (Remember)
    val exoPlayer = remember(videoUrl) {
        ExoPlayer.Builder(context).build().apply {
            setMediaItem(MediaItem.fromUri(videoUrl))
            prepare() // ویڈیو کو بفر کرنا شروع کر دے
            playWhenReady = isPlaying // 👈 یہ جادوئی لائن ہے!
        }
    }
    
    // 2. جب اسکرین بند ہو، تو پلیر کو میموری سے صاف کر دیں
    DisposableEffect(Unit) {
        onDispose {
            exoPlayer.release()
        }
    }
    
    // 3. جب باہر سے isPlaying تبدیل ہو، تو پلیر کو اپ ڈیٹ کریں
    LaunchedEffect(isPlaying) {
        exoPlayer.playWhenReady = isPlaying
    }
    
    // 4. اینڈرائیڈ کا نیٹو PlayerView دکھائیں
    AndroidView(
        factory = { ctx ->
            PlayerView(ctx).apply {
                player = exoPlayer
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
                useController = true // کنٹرولز دکھائیں
            }
        },
        update = { playerView ->
            // Recomposition پر پلیر کو دوبارہ اٹیچ کریں
            playerView.player = exoPlayer
        },
        modifier = modifier
    )
}
