package org.example.app.core.platform

import android.media.MediaMetadataRetriever
import android.net.Uri
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.example.app.AppContext

actual suspend fun extractVideoThumbnail(videoPath: String): ImageBitmap? = withContext(Dispatchers.IO) {
    try {
        val retriever = MediaMetadataRetriever()
        val context = AppContext.context
        
        if (videoPath.startsWith("content://") && context != null) {
            retriever.setDataSource(context, Uri.parse(videoPath))
        } else {
            retriever.setDataSource(videoPath)
        }
        
        // پہلا فریم (1 سیکنڈ)
        val frame = retriever.getFrameAtTime(1_000_000, MediaMetadataRetriever.OPTION_CLOSEST_SYNC)
        retriever.release()
        
        frame?.asImageBitmap()
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}
