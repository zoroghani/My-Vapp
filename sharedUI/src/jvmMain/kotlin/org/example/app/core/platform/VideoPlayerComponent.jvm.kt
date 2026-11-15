package org.example.app.core.platform

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@Composable
actual fun NativeVideoPlayer(videoUrl: String, modifier: Modifier) {
    Box(
        modifier = modifier.background(Color.Black),
        contentAlignment = Alignment.Center
    ) {
        Text("🖥️ Desktop Video Player Active\n(Video: $videoUrl)", color = Color.White)
        // نوٹ: ڈیسک ٹاپ پر اصل ویڈیو چلانے کے لیے VLCJ یا GStreamer لائبریری درکار ہوتی ہے۔
        // ورژن 1 کے لیے یہ محفوظ پلیس ہولڈر بہترین ہے تاکہ ایپ کریش نہ ہو۔
    }
}
