package org.example.app

data class DummyVideo(
    val id: String,
    val title: String,
    val channel: String,
    val thumbnailUrl: String,
    val videoUrl: String,
    val duration: String = "",
    val views: String = "",
    val publishDate: String = ""
)

val dummyVideos = listOf(
    DummyVideo("1", "Kotlin Multiplatform Tutorial", "Tech Channel",
        "https://picsum.photos/seed/video1/500/300",
        "https://storage.googleapis.com/exoplayer-test-media-0/BigBuckBunny_320x180.mp4",
        duration = "56:44", views = "6.4M views", publishDate = "2 days ago"),
    DummyVideo("2", "Build Your First App in 10 Mins", "Coding Life",
        "https://picsum.photos/seed/video2/500/300",
        "https://storage.googleapis.com/exoplayer-test-media-0/ForBiggerBlazes.mp4",
        duration = "10:02", views = "1.2M views", publishDate = "1 week ago"),
    DummyVideo("3", "Compose UI Basics for Beginners", "Android Dev",
        "https://picsum.photos/seed/video3/500/300",
        "https://storage.googleapis.com/exoplayer-test-media-0/ForBiggerEscapes.mp4",
        duration = "08:30", views = "890K views", publishDate = "3 days ago")
)
