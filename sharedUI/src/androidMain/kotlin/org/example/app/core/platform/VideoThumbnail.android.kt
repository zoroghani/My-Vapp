package org.example.app.core.platform

import android.media.MediaMetadataRetriever
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Composable
actual fun VideoThumbnail(videoPath: String?, modifier: Modifier) {
    val context = LocalContext.current
    val bitmap by produceState<ImageBitmap?>(null, videoPath) {
        value = withContext(Dispatchers.IO) {
            if (videoPath == null) return@withContext null
            val src = videoPath.substringBefore("|")
            val retriever = MediaMetadataRetriever()
            try {
                if (src.startsWith("content://")) {
                    retriever.setDataSource(context, Uri.parse(src))
                } else {
                    retriever.setDataSource(src)
                }
                retriever.frameAtTime?.asImageBitmap()
            } catch (e: Exception) {
                null
            } finally {
                retriever.release()
            }
        }
    }
    if (bitmap != null) {
        Image(
            bitmap = bitmap!!,
            contentDescription = "Video thumbnail",
            modifier = modifier,
            contentScale = ContentScale.Crop
        )
    }
}
