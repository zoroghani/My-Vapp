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
    
    // ExoPlayer کو یاد رکھیں تاکہ بار بار ری کریٹ نہ ہو
    val exoPlayer = remember(videoUrl) {
        ExoPlayer.Builder(context).build().apply {
            setMediaItem(MediaItem.fromUri(videoUrl))
            prepare()
        }
    }
    
    // جب Composable اسکرین سے ہٹے، تو پلیر کو ریلیز کر دیں
    DisposableEffect(Unit) {
        onDispose {
            exoPlayer.release()
        }
    }
    
    // Play/Pause کنٹرول
    LaunchedEffect(isPlaying) {
        if (isPlaying) exoPlayer.play() else exoPlayer.pause()
    }
    
    // اینڈرائیڈ کا نیٹو PlayerView
    AndroidView(
        factory = { ctx ->
            PlayerView(ctx).apply {
                player = exoPlayer
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
                useController = true // Play/Pause بٹن دکھائیں
            }
        },
        modifier = modifier
    )
}
