package org.example.app.core.platform

import android.annotation.SuppressLint
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView

@SuppressLint("SetJavaScriptEnabled")
@Composable
actual fun NativeVideoPlayer(videoUrl: String, modifier: Modifier = Modifier) {
    val embedUrl = videoUrl.replace("/v/", "/e/") // /v/ کو /e/ میں بدلیں

    AndroidView(modifier = modifier, factory = { context ->
        WebView(context).apply {
            settings.apply {
                javaScriptEnabled = true
                mediaPlaybackRequiresUserGesture = false
                useWideViewPort = true
                loadWithOverviewMode = true
            }
            webViewClient = object : WebViewClient() {
                override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean = false
            }
            loadUrl(embedUrl)
        }
    })
}
