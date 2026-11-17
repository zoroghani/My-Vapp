package org.example.app.core.platform

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import java.awt.FileDialog
import java.awt.Frame
import java.io.File

@Composable
actual fun rememberImagePicker(onImagePicked: (ByteArray) -> Unit): () -> Unit {
    return remember {
        {
            val dialog = FileDialog(null as Frame?, "Select Profile Picture", FileDialog.LOAD)
            dialog.filenameFilter = java.io.FilenameFilter { _, name ->
                name.lowercase().let {
                    it.endsWith(".png") || it.endsWith(".jpg") || it.endsWith(".jpeg") || it.endsWith(".webp")
                }
            }
            dialog.isVisible = true
            val fileName = dialog.file
            val directory = dialog.directory
            if (fileName != null && directory != null) {
                onImagePicked(File(directory, fileName).readBytes())
            }
        }
    }
}
