package org.example.app.core.platform

import androidx.compose.ui.graphics.ImageBitmap

expect fun bytesToImageBitmap(bytes: ByteArray): ImageBitmap?
