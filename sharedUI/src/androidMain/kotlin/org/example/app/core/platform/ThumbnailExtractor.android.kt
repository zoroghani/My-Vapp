package org.example.app.core.platform

import android.graphics.BitmapFactory
import android.media.MediaMetadataRetriever
import android.net.Uri
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

actual suspend fun extractVideoThumbnail(videoPath: String): ImageBitmap? = withContext(Dispatchers.IO) {
    try {
        val retriever = MediaMetadataRetriever()
        
        // content:// URI یا file:// path دونوں handle کریں
        if (videoPath.startsWith("content://")) {
            // Android context کی ضرورت نہیں - URI سے براہ راست
            retriever.setDataSource(
                androidx.compose.ui.platform.LocalContext.current,
                Uri.parse(videoPath)
            )
        } else {
            retriever.setDataSource(videoPath)
        }
        
        // پہلا فریم نکالیں
        val frame = retriever.getFrameAtTime(
            0, // 0 microseconds = پہلا فریم
            MediaMetadataRetriever.OPTION_CLOSEST_SYNC
        )
        
        retriever.release()
        
        frame?.asImageBitmap()
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}
