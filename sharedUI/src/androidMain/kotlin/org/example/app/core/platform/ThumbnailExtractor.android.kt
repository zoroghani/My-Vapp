package org.example.app.core.platform

import android.content.Context
import android.media.MediaMetadataRetriever
import android.net.Uri
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

// یہ فنکشن Android کے لیے خاص ہے اور Context لیتا ہے
suspend fun extractVideoThumbnailAndroid(context: Context, videoPath: String): ImageBitmap? = withContext(Dispatchers.IO) {
    try {
        val retriever = MediaMetadataRetriever()
        if (videoPath.startsWith("content://")) {
            retriever.setDataSource(context, Uri.parse(videoPath))
        } else {
            retriever.setDataSource(videoPath)
        }
        
        // 1 سیکنڈ (1,000,000 مائیکرو سیکنڈ) پر فریم لیں
        val frame = retriever.getFrameAtTime(1_000_000, MediaMetadataRetriever.OPTION_CLOSEST_SYNC)
        retriever.release()
        
        frame?.asImageBitmap()
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}

// Common expect function کے لیے (فی الحال Android پر یہ استعمال نہیں ہوگا)
actual suspend fun extractVideoThumbnail(videoPath: String): ImageBitmap? {
    return null
}
