package org.example.app.core.platform

import org.jcodec.api.FrameGrab
import org.jcodec.common.io.NIOUtils
import org.jcodec.scale.AWTUtil
import java.awt.image.BufferedImage
import java.io.ByteArrayOutputStream
import java.io.File
import javax.imageio.ImageIO

actual fun extractThumbnail(videoPath: String): ByteArray? {
    return try {
        val file = File(videoPath)
        // JCodec ویڈیو فائل کو پڑھتا ہے
        val grabber = FrameGrab.createFrameGrab(NIOUtils.readableChannel(file))
        val picture = grabber.nativeFrame // پہلا فریم
        
        picture?.let { pic ->
            // تصویر کو BufferedImage میں بدلیں
            val bufferedImage: BufferedImage = AWTUtil.toBufferedImage(pic)
            val baos = ByteArrayOutputStream()
            ImageIO.write(bufferedImage, "jpg", baos) // JPEG میں سیو کریں
            baos.toByteArray()
        }
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}
