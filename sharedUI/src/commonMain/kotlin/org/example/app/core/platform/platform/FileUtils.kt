package org.example.app.core.platform

// فائل کا راستہ لے کر اسے ByteArray میں تبدیل کرے گا
expect suspend fun readFileAsBytes(filePath: String): ByteArray
