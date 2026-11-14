package org.example.app.core.platform

import android.content.Context
import android.media.MediaMetadataRetriever
import android.net.Uri
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

// Android-specific: Context کے ساتھ
suspend fun extractVideoThumbnailAndroid(
    context: Context,
    videoPath: String
): ImageBitmap? = withContext(Dispatchers.IO) {
    try {
        val retriever = MediaMetadataRetriever()
        
        if (videoPath.startsWith("content://")) {
            retriever.setDataSource(context, Uri.parse(videoPath))
        } else {
            retriever.setDataSource(videoPath)
        }
        
        // پہلا فریم (1,000,000 microseconds = 1 second)
        val frame = retriever.getFrameAtTime(
            1_000_000,
            MediaMetadataRetriever.OPTION_CLOSEST_SYNC
        )
        
        retriever.release()
        frame?.asImageBitmap()
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}

// Common expect function کے لیے (Context کے بغیر - Desktop/iOS کے لیے)
actual suspend fun extractVideoThumbnail(videoPath: String): ImageBitmap? {
    return null // Android پر Android-specific version استعمال کریں
}
