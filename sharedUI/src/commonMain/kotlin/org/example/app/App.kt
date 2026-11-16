package org.example.app

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.navigator.Navigator
import org.example.app.core.ThemeState
import org.example.app.screens.VideoListScreen

@Composable
fun App(modifier: Modifier = Modifier) {
    
    val isDarkMode = ThemeState.isDarkMode

    MaterialTheme(
        colorScheme = if (isDarkMode) darkColorScheme() else lightColorScheme()
    ) {
        Surface(
            modifier = modifier,
            color = MaterialTheme.colorScheme.background // 👈 یہ پوری ایپ کا پس منظر خود بخود بدل دے گا
        ) {
            Navigator(VideoListScreen())
        }
    }
}
