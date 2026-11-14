package org.example.app.core.platform

import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.toComposeImageBitmap
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import platform.AVFoundation.AVAsset
import platform.AVFoundation.AVAssetImageGenerator
import platform.AVFoundation.AVURLAsset
import platform.CoreMedia.CMTime
import platform.CoreMedia.CMTimeMake
import platform.Foundation.NSURL
import platform.UIKit.UIImage

@OptIn(ExperimentalForeignApi::class)
actual suspend fun extractVideoThumbnail(videoPath: String): ImageBitmap? = withContext(Dispatchers.Main) {
    try {
        val url = NSURL.fileURLWithPath(videoPath)
        val asset = AVURLAsset(uRL = url)
        val generator = AVAssetImageGenerator(asset = asset)
        
        // پہلا فریم
        val time = CMTimeMake(value = 1, timescale = 1) // 1 second
        val cgImage = generator.copyCGImageAtTime(time, actualTime = null, error = null)
        
        val uiImage = UIImage(cGImage = cgImage)
        uiImage.toComposeImageBitmap()
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}
