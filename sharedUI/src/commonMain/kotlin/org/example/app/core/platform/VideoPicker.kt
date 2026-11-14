package org.example.app.core.platform

import androidx.compose.runtime.Composable

@Composable
expect fun rememberVideoPicker(onVideoSelected: (String) -> Unit): () -> Unit
