package org.example.app.core.platform

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import java.awt.FileDialog
import java.awt.Frame
import java.io.File

@Composable
actual fun rememberVideoPicker(onVideoSelected: (String) -> Unit): () -> Unit {
    return remember {
        {
            // ڈیسک ٹاپ کے لیے ایک عارضی Frame بنائیں تاکہ ڈائیلاگ اس سے جڑ سکے
            val frame = Frame()
            val dialog = FileDialog(frame, "ویڈیو منتخب کریں", FileDialog.LOAD)
            
            // صرف ویڈیو فائلز دکھانے کی کوشش کریں (macOS پر یہ کام کرتا ہے)
            dialog.file = "*.mp4" 
            
            dialog.isVisible = true
            
            // اگر صارف نے فائل منتخب کی
            dialog.file?.let { fileName ->
                val file = File(dialog.directory, fileName)
                onVideoSelected(file.absolutePath)
            }
            
            frame.dispose() // صفائی
        }
    }
}
