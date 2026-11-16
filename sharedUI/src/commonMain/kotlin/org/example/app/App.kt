package org.example.app

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.navigator.Navigator
import org.example.app.screens.VideoListScreen

// مین ایپ فنکشن
@Composable
fun App(modifier: Modifier = Modifier) {
    MaterialTheme {
        Surface(modifier = modifier) {
            Navigator(VideoListScreen())
        }
    }
}
