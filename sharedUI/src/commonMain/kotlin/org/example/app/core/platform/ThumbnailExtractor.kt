package org.example.app.core.platform

import androidx.compose.ui.graphics.ImageBitmap

expect suspend fun extractVideoThumbnail(videoPath: String): ImageBitmap?
