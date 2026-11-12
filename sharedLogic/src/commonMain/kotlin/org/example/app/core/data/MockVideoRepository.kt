package org.example.app.core.data

import kotlinx.coroutines.delay

class MockVideoRepository : VideoRepository {
    
    // فی الحال میموری میں ڈیٹا اسٹور کریں گے (بعد میں Database میں کریں گے)
    private val videos = mutableListOf(
        Video(
            id = "1",
            title = "میری پہلی ویڈیو",
            description = "یہ ایک ٹیسٹ ویڈیو ہے",
            filePath = "/sample/video1.mp4",
            thumbnailPath = "https://picsum.photos/seed/v1/500/300",
            duration = 120,
            uploadDate = System.currentTimeMillis()
        )
    )

    override suspend fun getAllVideos(): List<Video> {
        delay(500) // نیٹ ورک یا ڈیٹا بیس کی تاخیر کی نقل
        return videos.sortedByDescending { it.uploadDate }
    }

    override suspend fun uploadVideo(video: Video, fileBytes: ByteArray): Result<Unit> {
        delay(1000) // اپلوڈ ہونے کی نقل
        videos.add(video)
        return Result.success(Unit)
    }

    override suspend fun deleteVideo(id: String): Result<Unit> {
        delay(300)
        val removed = videos.removeAll { it.id == id }
        return if (removed) Result.success(Unit) 
               else Result.failure(Exception("ویڈیو نہیں ملی"))
    }
}
