package org.example.app.core.platform

import androidx.compose.ui.graphics.ImageBitmap

actual suspend fun extractVideoThumbnail(videoPath: String): ImageBitmap? {
    // ⚠️ Desktop پر native video frame extraction ممکن نہیں
    // بعد میں FFmpegFrameGrabber لائبریری سے بھریں گے
    println("⚠️ Desktop پر thumbnail extraction فی الحال دستیاب نہیں")
    return null
}
