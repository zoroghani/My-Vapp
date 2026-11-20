package org.example.app.core.platform

import android.net.Uri
import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

actual suspend fun readFileAsBytes(filePath: String): ByteArray = withContext(Dispatchers.IO) {
    val actualPath = filePath.substringBefore("|") // سائز والے حصے کو ہٹا دیں
    val uri = Uri.parse(actualPath)
    
    // Android کے ContentResolver سے فائل پڑھیں
    val context = androidx.compose.ui.platform.LocalContext.current
    context.contentResolver.openInputStream(uri)?.use { inputStream ->
        inputStream.readBytes()
    } ?: byteArrayOf() // اگر فائل نہ ملے تو خالی بائٹس
}
