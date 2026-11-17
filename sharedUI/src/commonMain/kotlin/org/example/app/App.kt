package org.example.app

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import cafe.adriel.voyager.navigator.Navigator
import org.example.app.core.ThemeState
import org.example.app.screens.VideoListScreen

private val LightColors = lightColorScheme(
    primary = Color(0xFF3066E8),          // آپ کا برانڈ نیلا رنگ
    onPrimary = Color.White,              // بٹن/آئیکن کا ٹیکسٹ (سفید)
    primaryContainer = Color(0xFFD9E2FF), // ہلکا نیلا (ہائلائٹس کے لیے)
    onPrimaryContainer = Color(0xFF001A41),
    background = Color(0xFFF5F5F5),       // آپ کا لائٹ بیک گراؤنڈ
    surface = Color.White,                // کارڈز اور ٹاپ بار (سفید تاکہ بیک گراؤنڈ سے الگ نظر آئیں)
    onSurface = Color(0xFF1A1A1A),        // اصلی ٹیکسٹ (گہرا سیاہ)
    onSurfaceVariant = Color(0xFF666666), // سیکنڈری ٹیکسٹ (سرمئی)
    outline = Color(0xFFE0E0E0)           // کارڈز کی ہلکی سرحد
)

private val DarkColors = darkColorScheme(
    primary = Color(0xFF3066E8),          // برانڈ نیلا رنگ (ڈارک موڈ میں بھی وہی)
    onPrimary = Color.White,
    primaryContainer = Color(0xFF002D6E), // گہرا نیلا
    onPrimaryContainer = Color(0xFFD9E2FF),
    background = Color(0xFF121212),       // آپ کا ڈارک بیک گراؤنڈ
    surface = Color(0xFF1E1E1E),          // کارڈز (بیک گراؤنڈ سے تھوڑا ہلکا تاکہ ابھر کر نظر آئیں)
    onSurface = Color(0xFFE5E5E5),        // اصلی ٹیکسٹ (ہلکا سفید)
    onSurfaceVariant = Color(0xFFA0A0A0), // سیکنڈری ٹیکسٹ (ہلکا سرمئی)
    outline = Color(0xFF333333)           // کارڈز کی ہلکی سرحد
)

@Composable
fun App(modifier: Modifier = Modifier) {
    val isDarkMode = ThemeState.isDarkMode

    MaterialTheme(
        colorScheme = if (isDarkMode) DarkColors else LightColors
    ) {
        Surface(
            modifier = modifier,
            color = MaterialTheme.colorScheme.background // پوری ایپ کا پس منظر خود بخود سیٹ ہو جائے گا
        ) {
            Navigator(VideoListScreen())
        }
    }
}
