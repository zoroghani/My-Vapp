package org.example.app.core.data

data class Video(
    val id: String,
    val title: String,
    val description: String,
    val filePath: String,        // ویڈیو فائل کا لوکل پاتھ
    val thumbnailPath: String?,  // تھمب نیل کا پاتھ (یا URL)
    val duration: Long,          // دورانیہ سیکنڈز میں
    val uploadDate: Long         // اپلوڈ کی تاریخ (Timestamp)
)
