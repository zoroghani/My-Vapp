package org.example.app.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.* // 👈 تمام آئیکنز کی امپورٹ
import androidx.compose.material3.*
import androidx.compose.runtime.* // 👈 remember اور mutableStateOf کی امپورٹ
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import org.example.app.DummyVideo

@Composable
fun VideoCard(
    video: DummyVideo,
    onVideoClick: () -> Unit,
    onChannelClick: () -> Unit,
    isOwner: Boolean = false
) {
    Card(
        onClick = onVideoClick,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Box(modifier = Modifier.fillMaxWidth().height(180.dp)) {
                AsyncImage(
                    model = video.thumbnailUrl,
                    contentDescription = video.title,
                    modifier = Modifier.fillMaxSize().clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)),
                    contentScale = ContentScale.Crop
                )
                if (video.duration.isNotEmpty()) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .padding(8.dp)
                            .background(Color.Black.copy(alpha = 0.75f), RoundedCornerShape(4.dp))
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(video.duration, color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Medium)
                    }
                }
                
                // 👇 3 ڈاٹ مینیو (درست امپورٹس اور بریسز کے ساتھ)
                var isMenuExpanded by remember { mutableStateOf(false) }
                Box(modifier = Modifier.align(Alignment.TopEnd).padding(4.dp)) {
                    IconButton(onClick = { isMenuExpanded = true }) {
                        Icon(Icons.Default.MoreVert, contentDescription = "More", tint = Color.White)
                    }
                    DropdownMenu(
                        expanded = isMenuExpanded,
                        onDismissRequest = { isMenuExpanded = false },
                        containerColor = MaterialTheme.colorScheme.surface
                    ) {
                        if (isOwner) {
                            DropdownMenuItem(
                                text = { Text("Edit") },
                                onClick = { isMenuExpanded = false },
                                leadingIcon = { Icon(Icons.Default.Edit, contentDescription = null) }
                            )
                            DropdownMenuItem(
                                text = { Text("Delete", color = MaterialTheme.colorScheme.error) },
                                onClick = { isMenuExpanded = false },
                                leadingIcon = { Icon(Icons.Default.Delete, contentDescription = null, tint = MaterialTheme.colorScheme.error) }
                            )
                        } else {
                            DropdownMenuItem(
                                text = { Text("Report") },
                                onClick = { isMenuExpanded = false },
                                leadingIcon = { Icon(Icons.Default.Flag, contentDescription = null) }
                            )
                        }
                        DropdownMenuItem(
                            text = { Text("Share") },
                            onClick = { isMenuExpanded = false },
                            leadingIcon = { Icon(Icons.Default.Share, contentDescription = null) }
                        )
                        DropdownMenuItem(
                            text = { Text("Download") },
                            onClick = { isMenuExpanded = false },
                            leadingIcon = { Icon(Icons.Default.Download, contentDescription = null) }
                        )
                    }
                }
            }
            
            Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.Top) {
                AsyncImage(
                    model = "https://picsum.photos/seed/channel/80/80",
                    contentDescription = "Channel",
                    modifier = Modifier.size(36.dp).clip(CircleShape).clickable { onChannelClick() },
                    contentScale = ContentScale.Crop
                )
                Spacer(modifier = Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        video.title,
                        style = MaterialTheme.typography.titleMedium,
                        maxLines = 2,
                        color = MaterialTheme.colorScheme.onSurface,
                        fontWeight = FontWeight.Medium
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(video.channel, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    val meta = listOfNotNull(
                        video.views.takeIf { it.isNotEmpty() },
                        video.publishDate.takeIf { it.isNotEmpty() }
                    ).joinToString(" • ")
                    if (meta.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(meta, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
            }
        }
    }
}
