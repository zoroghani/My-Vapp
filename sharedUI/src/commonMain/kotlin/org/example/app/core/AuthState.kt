package org.example.app.core

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

// پوری ایپ میں لاگ ان اسٹیٹ
object AuthState {
    var isLoggedIn by mutableStateOf(false)
    var userName by mutableStateOf("")
    var userEmail by mutableStateOf("")
}
