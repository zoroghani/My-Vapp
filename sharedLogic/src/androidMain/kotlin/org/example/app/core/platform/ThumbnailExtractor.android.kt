package org.example.app.core.platform

import android.graphics.Bitmap
import android.media.MediaMetadataRetriever
import java.io.ByteArrayOutputStream

actual fun extractThumbnail(videoPath: String): ByteArray? {
    val retriever = MediaMetadataRetriever()
    return try {
        retriever.setDataSource(videoPath)
        // پہلے 1 سیکنڈ (1,000,000 مائیکرو سیکنڈ) کا فریم لو
        val frame = retriever.getFrameAtTime(1_000_000, MediaMetadataRetriever.OPTION_CLOSEST_SYNC)
        
        frame?.let { bitmap ->
            val stream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 80, stream) // 80% کوالٹی
            stream.toByteArray()
        }
    } catch (e: Exception) {
        e.printStackTrace()
        null
    } finally {
        retriever.release()
    }
}
