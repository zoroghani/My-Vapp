package org.example.app.core.platform

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.painterResource
// نوٹ: اگر آپ کے پاس کوئی آئیکن نہیں ہے تو یہ لائن ہٹا کر صرف Text رکھ سکتے ہیں

@Composable
actual fun VideoPlayerView(
    videoUrl: String,
    isPlaying: Boolean,
    modifier: Modifier
) {
    // ڈیسک ٹاپ کے لیے عارضی UI (تاکہ بلڈ 100% کامیاب رہے)
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(250.dp)
            .background(MaterialTheme.colorScheme.surfaceVariant),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "🎬 ویڈیو پلیر",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = "(ڈیسک ٹاپ ویڈیو سپورٹ جلد آرہی ہے)",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
