package org.example.app.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import org.example.app.core.platform.KeepScreenOn
import org.example.app.core.platform.NativeVideoPlayer
import org.example.app.dummyVideos

data class Comment(val author: String, val text: String)

@OptIn(ExperimentalMaterial3Api::class)
class VideoPlayerScreen(
    private val videoUrl: String,
    private val videoTitle: String
) : Screen {

    @Composable
    override fun Content() {
        // ✅ سکرین آن رکھنے کا فنکشن بالکل درست جگہ پر
        KeepScreenOn(true)
        
        val navigator = LocalNavigator.currentOrThrow
        var isLiked by remember { mutableStateOf(false) }
        var isFollowed by remember { mutableStateOf(false) }
        var isDescriptionExpanded by remember { mutableStateOf(false) }
        var commentText by remember { mutableStateOf("") }
        var isCommentsExpanded by remember { mutableStateOf(false) }
        var isFullScreen by remember { mutableStateOf(false) }
        var selectedFilter by remember { mutableStateOf("Top") }
        
        val comments = remember { mutableStateListOf(Comment("Tech Fan", "Great video! 🙌")) }
        val snackbarHostState = remember { SnackbarHostState() }
        var snackbarMessage by remember { mutableStateOf<String?>(null) }
        
        LaunchedEffect(snackbarMessage) {
            snackbarMessage?.let { msg ->
                snackbarHostState.showSnackbar(msg)
                snackbarMessage = null
            }
        }

        val windowSize = LocalWindowInfo.current.containerSize
        val isLandscape = windowSize.width > windowSize.height
        val screenHeightDp = with(LocalDensity.current) { windowSize.height.toDp() }

        Scaffold(
            containerColor = MaterialTheme.colorScheme.background,
            snackbarHost = { SnackbarHost(snackbarHostState) }
        ) { padding ->
            
            // 👇 فل سکرین موڈ
            if (isFullScreen) {
                Box(modifier = Modifier.fillMaxSize().background(Color.Black)) {
                    NativeVideoPlayer(videoUrl = videoUrl, modifier = Modifier.fillMaxSize())
                    IconButton(
                        onClick = { isFullScreen = false },
                        modifier = Modifier.align(Alignment.TopStart).padding(16.dp).background(Color.Black.copy(alpha = 0.5f), CircleShape)
                    ) {
                        Icon(Icons.Default.FullscreenExit, contentDescription = "Exit", tint = Color.White)
                    }
                }
            } 
            // 👇 نارمل موڈ
            else {
                Column(modifier = Modifier.fillMaxSize().padding(padding).verticalScroll(rememberScrollState())) {
                    
                    // 1️⃣ ویڈیو پلئر
                    Box(modifier = Modifier.fillMaxWidth().height(if (isLandscape) screenHeightDp else 240.dp).background(Color.Black)) {
                        NativeVideoPlayer(videoUrl = videoUrl, modifier = Modifier.fillMaxSize())
                        IconButton(
                            onClick = { isFullScreen = true },
                            modifier = Modifier.align(Alignment.BottomEnd).padding(8.dp).background(Color.Black.copy(alpha = 0.5f), CircleShape)
                        ) {
                            Icon(Icons.Default.Fullscreen, contentDescription = "Full Screen", tint = Color.White)
                        }
                    }

                    // 2️⃣ ٹائٹل اور ڈسکرپشن
                    Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 12.dp)) {
                        Text(text = videoTitle, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
                        Spacer(modifier = Modifier.height(4.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(text = "37 views  •  2y ago  •  ", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            Text(text = if (isDescriptionExpanded) "less" else "more", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary, modifier = Modifier.clickable { isDescriptionExpanded = !isDescriptionExpanded })
                        }
                        if (isDescriptionExpanded) {
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(text = "This is the official video description. It contains details about the video, tags, and links.", fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                    }

                    // 3️⃣ ایکشن بٹنز
                    Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Box(modifier = Modifier.size(40.dp).clip(CircleShape).background(Color(0xFF008060)).clickable { navigator.push(ChannelScreen()) }, contentAlignment = Alignment.Center) {
                                Text("Z", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                            }
                            Button(
                                onClick = { isFollowed = !isFollowed },
                                shape = RoundedCornerShape(20.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = if (isFollowed) MaterialTheme.colorScheme.surfaceVariant else MaterialTheme.colorScheme.primary, contentColor = if (isFollowed) MaterialTheme.colorScheme.onSurfaceVariant else MaterialTheme.colorScheme.onPrimary),
                                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
                            ) {
                                Text(if (isFollowed) "Following" else "Follow", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
                            val iconBg = MaterialTheme.colorScheme.surfaceVariant
                            val iconColor = MaterialTheme.colorScheme.onSurfaceVariant
                            IconButton(onClick = { isLiked = !isLiked }, modifier = Modifier.size(40.dp).clip(CircleShape).background(iconBg)) {
                                Icon(imageVector = if (isLiked) Icons.Default.Favorite else Icons.Default.FavoriteBorder, contentDescription = "Like", tint = if (isLiked) Color(0xFFE53935) else iconColor, modifier = Modifier.size(20.dp))
                            }
                            IconButton(onClick = { snackbarMessage = "Link copied" }, modifier = Modifier.size(40.dp).clip(CircleShape).background(iconBg)) {
                                Icon(Icons.Default.Reply, contentDescription = "Share", tint = iconColor, modifier = Modifier.size(20.dp))
                            }
                            IconButton(onClick = { snackbarMessage = "Download started..." }, modifier = Modifier.size(40.dp).clip(CircleShape).background(iconBg)) {
                                Icon(Icons.Default.Download, contentDescription = "Download", tint = iconColor, modifier = Modifier.size(20.dp))
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // 4️⃣ کمنٹس پریویو کارڈ
                    Card(
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp).clickable { isCommentsExpanded = true },
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text("Comments", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("${comments.size}", fontSize = 14.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                            if (comments.isNotEmpty()) {
                                Spacer(modifier = Modifier.height(8.dp))
                                Row(verticalAlignment = Alignment.Top) {
                                    Box(modifier = Modifier.size(24.dp).clip(CircleShape).background(MaterialTheme.colorScheme.primary), contentAlignment = Alignment.Center) {
                                        Text(comments.first().author.take(1), color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                    }
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(comments.first().text, fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurface, maxLines = 1, overflow = TextOverflow.Ellipsis)
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    // 5️⃣ ریلیٹڈ ویڈیو
                    VideoCard(
                        video = dummyVideos[0],
                        onVideoClick = { navigator.push(VideoPlayerScreen(dummyVideos[0].videoUrl, dummyVideos[0].title)) },
                        onChannelClick = { navigator.push(ChannelScreen()) }
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                }
            }
        }

        // 6️⃣ کمنٹس موڈل بوٹم شیٹ (YouTube جیسا)
        if (isCommentsExpanded) {
            ModalBottomSheet(
                onDismissRequest = { isCommentsExpanded = false },
                containerColor = MaterialTheme.colorScheme.background,
                contentColor = MaterialTheme.colorScheme.onSurface
            ) {
                Column(modifier = Modifier.fillMaxWidth().fillMaxHeight(0.78f)) {
                    Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp), verticalAlignment = Alignment.CenterVertically) {
                        Text("Comments", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface, modifier = Modifier.weight(1f))
                        Text("${comments.size}", fontSize = 14.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Spacer(Modifier.width(16.dp))
                        IconButton(onClick = { isCommentsExpanded = false }) {
                            Icon(Icons.Default.Close, contentDescription = "Close")
                        }
                    }
                    Spacer(Modifier.height(8.dp))
                    
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.padding(horizontal = 16.dp)) {
                        listOf("Top", "Newest").forEach { filter ->
                            FilterChip(
                                selected = selectedFilter == filter,
                                onClick = { selectedFilter = filter },
                                label = { Text(filter) },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = MaterialTheme.colorScheme.onSurface,
                                    selectedLabelColor = MaterialTheme.colorScheme.surface
                                )
                            )
                        }
                    }
                    Spacer(Modifier.height(8.dp))
                    HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))

                    LazyColumn(modifier = Modifier.weight(1f).fillMaxWidth(), contentPadding = PaddingValues(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                        items(comments) { comment ->
                            Row(verticalAlignment = Alignment.Top) {
                                Box(Modifier.size(36.dp).clip(CircleShape).background(MaterialTheme.colorScheme.primary), contentAlignment = Alignment.Center) {
                                    Text(comment.author.take(1), color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                                }
                                Spacer(Modifier.width(12.dp))
                                Column(Modifier.weight(1f)) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text("@${comment.author.lowercase().replace(" ", "")}", fontSize = 13.sp, fontWeight = FontWeight.Medium, color = MaterialTheme.colorScheme.onSurface)
                                        Spacer(Modifier.width(6.dp))
                                        Text("• just now", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                    }
                                    Spacer(Modifier.height(4.dp))
                                    Text(comment.text, fontSize = 14.sp, color = MaterialTheme.colorScheme.onSurface)
                                    Row(Modifier.padding(top = 6.dp), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                                        Icon(Icons.Default.ThumbUp, null, Modifier.size(18.dp), tint = MaterialTheme.colorScheme.onSurfaceVariant)
                                        Icon(Icons.Default.ThumbDown, null, Modifier.size(18.dp), tint = MaterialTheme.colorScheme.onSurfaceVariant)
                                        Text("Reply", fontSize = 12.sp, fontWeight = FontWeight.Medium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                    }
                                }
                            }
                        }
                    }

                    HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))

                    Row(Modifier.fillMaxWidth().padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                        Box(Modifier.size(36.dp).clip(CircleShape).background(MaterialTheme.colorScheme.primary), contentAlignment = Alignment.Center) {
                            Text("Z", color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                        }
                        Spacer(Modifier.width(12.dp))
                        OutlinedTextField(
                            value = commentText,
                            onValueChange = { commentText = it },
                            placeholder = { Text("Comment...") },
                            modifier = Modifier.weight(1f),
                            singleLine = true,
                            shape = RoundedCornerShape(20.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = MaterialTheme.colorScheme.primary,
                                unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
                            )
                        )
                        if (commentText.isNotBlank()) {
                            IconButton(onClick = {
                                comments.add(0, Comment("Andrew Ainsley", commentText))
                                commentText = ""
                            }) {
                                Icon(Icons.Default.Send, contentDescription = "Send", tint = MaterialTheme.colorScheme.primary)
                            }
                        }
                    }
                }
            }
        }
    }
}
