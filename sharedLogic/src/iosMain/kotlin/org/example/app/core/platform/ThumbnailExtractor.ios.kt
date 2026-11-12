package org.example.app.core.platform

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.addressOf
import kotlinx.cinterop.usePinned
import platform.AVFoundation.AVAssetImageGenerator
import platform.AVFoundation.AVURLAsset
import platform.CoreMedia.CMTimeMake
import platform.Foundation.NSData
import platform.Foundation.NSURL
import platform.UIKit.UIImage
import platform.UIKit.UIImageJPEGRepresentation

@OptIn(ExperimentalForeignApi::class)
actual fun extractThumbnail(videoPath: String): ByteArray? {
    val url = NSURL.fileURLWithPath(videoPath)
    val asset = AVURLAsset(uRL = url)
    val generator = AVAssetImageGenerator(asset = asset)
    generator.appliesPreferredTrackTransform = true
    
    val time = CMTimeMake(value = 1, timescale = 1) // 1 سیکنڈ
    var actualTime = CMTimeMake(value = 0, timescale = 0)
    
    return try {
        val cgImage = generator.copyCGImageAtTime(time, actualTime = actualTime, error = null)
        val uiImage = UIImage(CGImage = cgImage)
        val imageData = UIImageJPEGRepresentation(uiImage, 0.8)
        imageData?.toByteArray()
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}

// NSData کو ByteArray میں تبدیل کرنے کا ہیلپر فنکشن
@OptIn(ExperimentalForeignApi::class)
fun NSData.toByteArray(): ByteArray {
    val bytes = this.bytes ?: return ByteArray(0)
    val length = this.length
    return ByteArray(length.toInt()).apply {
        usePinned { pinned ->
            pinned.addressOf(0).copyFrom(bytes, length)
        }
    }
}
