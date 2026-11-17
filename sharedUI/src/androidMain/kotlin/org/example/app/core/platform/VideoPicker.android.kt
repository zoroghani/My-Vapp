package org.example.app.core.platform

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext

@Composable
actual fun rememberVideoPicker(onVideoPicked: (String) -> Unit): () -> Unit {
    val context = LocalContext.current
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        if (uri != null) {
            // فائل کا سائز چیک کر کے path بھیجیں
            val size = context.contentResolver.openFileDescriptor(uri, "r")?.use { it.statSize } ?: 0
            onVideoPicked("${uri.path}|$size")
        }
    }
    return remember { { launcher.launch("video/*") } }
}
