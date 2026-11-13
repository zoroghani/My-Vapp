package org.example.app.core.platform

import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.toComposeImageBitmap
import java.io.ByteArrayInputStream
import javax.imageio.ImageIO

actual fun bytesToImageBitmap(bytes: ByteArray): ImageBitmap? {
    return try {
        val bufferedImage = ImageIO.read(ByteArrayInputStream(bytes))
        bufferedImage.toComposeImageBitmap()
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}
