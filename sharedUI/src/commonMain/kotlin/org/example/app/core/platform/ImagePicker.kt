package org.example.app.core.platform

import androidx.compose.runtime.Composable

// تصویر منتخب کرنے کا فنکشن — ہر پلیٹ فارم اسے اپنی طرح implement کرے گا
@Composable
expect fun rememberImagePicker(onImagePicked: (ByteArray) -> Unit): () -> Unit
