package org.example.app.core.platform

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
expect fun VideoThumbnail(videoPath: String?, modifier: Modifier)
