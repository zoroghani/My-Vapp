package org.example.app.core.platform

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import java.awt.FileDialog
import java.awt.Frame
import java.io.File

@Composable
actual fun rememberVideoPicker(onVideoPicked: (String) -> Unit): () -> Unit {
    return remember {
        {
            val dialog = FileDialog(null as Frame?, "Select Video", FileDialog.LOAD)
            dialog.filenameFilter = java.io.FilenameFilter { _, name ->
                name.lowercase().let {
                    it.endsWith(".mp4") || it.endsWith(".mov") || it.endsWith(".avi") ||
                    it.endsWith(".mkv") || it.endsWith(".webm")
                }
            }
            dialog.isVisible = true
            val fileName = dialog.file
            val directory = dialog.directory
            if (fileName != null && directory != null) {
                val f = File(directory, fileName)
                onVideoPicked("${f.absolutePath}|${f.length()}")
            }
        }
    }
}
