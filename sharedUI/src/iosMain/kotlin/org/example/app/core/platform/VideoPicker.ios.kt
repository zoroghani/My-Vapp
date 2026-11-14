package org.example.app.core.platform

import androidx.compose.runtime.Composable

@Composable
actual fun rememberVideoPicker(onVideoSelected: (String) -> Unit): () -> Unit {
    return { } // بعد میں PHPickerViewController سے بدلیں گے
}
