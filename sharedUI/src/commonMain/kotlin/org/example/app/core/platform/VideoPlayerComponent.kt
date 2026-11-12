package org.example.app.core.platform

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

// یہ فنکشن ہر پلیٹ فارم اپنی مرضی کا ویڈیو پلیر فراہم کرے گا
@Composable
expect fun VideoPlayerView(
    videoUrl: String,
    isPlaying: Boolean,
    modifier: Modifier = Modifier
)
