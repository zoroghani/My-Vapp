package org.example.app.core

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

// پوری ایپ کا مشترکہ ڈارک موڈ اسٹیٹ
object ThemeState {
    var isDarkMode by mutableStateOf(false)
}
