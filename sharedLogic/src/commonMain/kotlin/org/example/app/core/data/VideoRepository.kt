package org.example.app.core.data

interface VideoRepository {
    // تمام ویڈیوز کی لسٹ حاصل کریں
    suspend fun getAllVideos(): List<Video>
    
    // نئی ویڈیو اپلوڈ/سیو کریں (فی الحال لوکل، بعد میں API سے جوڑیں گے)
    suspend fun uploadVideo(video: Video, fileBytes: ByteArray): Result<Unit>
    
    // ویڈیو ڈیلیٹ کریں
    suspend fun deleteVideo(id: String): Result<Unit>
}
