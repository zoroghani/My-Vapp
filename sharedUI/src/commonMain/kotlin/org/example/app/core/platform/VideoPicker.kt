package org.example.app.core.platform

import androidx.compose.runtime.Composable

@Composable
expect fun rememberVideoPicker(onVideoPicked: (uri: String, fileBytes: ByteArray, fileName: String) -> Unit): () -> Unit
