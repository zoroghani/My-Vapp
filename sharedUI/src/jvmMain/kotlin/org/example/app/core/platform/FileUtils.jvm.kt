package org.example.app.core.platform

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

actual suspend fun readFileAsBytes(filePath: String): ByteArray = withContext(Dispatchers.IO) {
    val actualPath = filePath.substringBefore("|")
    try {
        File(actualPath).readBytes()
    } catch (e: Exception) {
        byteArrayOf()
    }
}
