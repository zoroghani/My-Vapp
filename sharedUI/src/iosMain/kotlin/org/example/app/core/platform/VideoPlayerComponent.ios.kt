package org.example.app.core.platform

import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.UIKitView
import platform.AVFoundation.AVPlayer
import platform.AVFoundation.AVPlayerItem
import platform.AVKit.AVPlayerViewController
import platform.Foundation.NSURL

@Composable
actual fun VideoPlayerView(
    videoUrl: String,
    isPlaying: Boolean,
    modifier: Modifier
) {
    // آئی فون کے لیے URL سے پلیئر بنائیں
    val url = NSURL.URLWithString(videoUrl) ?: return
    val playerItem = AVPlayerItem(uRL = url)
    val player = AVPlayer(playerItem = playerItem)
    val playerViewController = AVPlayerViewController()
    
    playerViewController.player = player
    
    // Play/Pause کنٹرول
    LaunchedEffect(isPlaying) {
        if (isPlaying) player.play() else player.pause()
    }
    
    // آئی فون کا نیٹو UIKitView
    UIKitView(
        factory = {
            playerViewController.view
        },
        modifier = modifier
    )
}
