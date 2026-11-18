package org.example.app.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import org.example.app.core.platform.NativeVideoPlayer
import org.example.app.dummyVideos

private val AccentColor = Color(0xFFFF4757)

// کمنٹ کا سادہ ماڈل
data class Comment(val author: String, val text: String)

class VideoPlayerScreen(
    private val videoUrl: String,
    private val videoTitle: String
) : Screen {

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        var isLiked by remember { mutableStateOf(false) }
        var isFollowed by remember { mutableStateOf(false) }
        var isDescriptionExpanded by remember { mutableStateOf(false) }
        var commentText by remember { mutableStateOf("") }
        var isCommentsExpanded by remember { mutableStateOf(false) }
var newComment by remember { mutableStateOf("") }
        
        val comments = remember { mutableStateListOf(Comment("Tech Fan", "Great video! 🙌")) }
        val snackbarHostState = remember { SnackbarHostState() }
        var snackbarMessage by remember { mutableStateOf<String?>(null) }
        
        LaunchedEffect(snackbarMessage) {
            snackbarMessage?.let { msg ->
                snackbarHostState.showSnackbar(msg)
                snackbarMessage = null
            }
        }

        Scaffold(
            // ✅ تھیم کے مطابق پس منظر
            containerColor = MaterialTheme.colorScheme.background,
            snackbarHost = { SnackbarHost(snackbarHostState) }
        ) { padding ->
            Column(
                modifier = Modifier.fillMaxSize().padding(padding).verticalScroll(rememberScrollState())
            ) {
                NativeVideoPlayer(
                    videoUrl = videoUrl,
                    modifier = Modifier.fillMaxWidth().height(240.dp).background(Color.Black)
                )

                Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 12.dp)) {
                    Text(
                        text = videoTitle,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface // ✅
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "37 views  •  2y ago  •  ",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant // ✅
                        )
                        Text(
                            text = if (isDescriptionExpanded) "less" else "more",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary, // ✅ Accent رنگ تھیم سے
                            modifier = Modifier.clickable { isDescriptionExpanded = !isDescriptionExpanded }
                        )
                    }
                    if (isDescriptionExpanded) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "This is the official video description. It contains details about the video, tags, and links.",
                            fontSize = 13.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Box(
                            modifier = Modifier.size(40.dp).clip(CircleShape).background(Color(0xFF008060)).clickable { navigator.push(ChannelScreen()) },
                            contentAlignment = Alignment.Center
                        ) {
                            Text("Z", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                        }
                        Button(
                            onClick = { isFollowed = !isFollowed },
                            shape = RoundedCornerShape(20.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (isFollowed) MaterialTheme.colorScheme.surfaceVariant else MaterialTheme.colorScheme.primary,
                                contentColor = if (isFollowed) MaterialTheme.colorScheme.onSurfaceVariant else MaterialTheme.colorScheme.onPrimary
                            ),
                            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
                        ) {
                            Text(if (isFollowed) "Following" else "Follow", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                        }
                    }

                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
                        val iconBg = MaterialTheme.colorScheme.surfaceVariant // ✅ آئیکن کا پس منظر
                        val iconColor = MaterialTheme.colorScheme.onSurfaceVariant
                        
                        IconButton(onClick = { isLiked = !isLiked }, modifier = Modifier.size(40.dp).clip(CircleShape).background(iconBg)) {
                            Icon(
                                imageVector = if (isLiked) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                                contentDescription = "Like",
                                tint = if (isLiked) Color(0xFFE53935) else iconColor,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        IconButton(onClick = { snackbarMessage = "Link copied" }, modifier = Modifier.size(40.dp).clip(CircleShape).background(iconBg)) {
                            Icon(Icons.Share, contentDescription = "Share", tint = iconColor, modifier = Modifier.size(20.dp))
                        }
                        IconButton(onClick = { snackbarMessage = "Download started..." }, modifier = Modifier.size(40.dp).clip(CircleShape).background(iconBg)) {
                            Icon(Icons.Default.Download, contentDescription = "Download", tint = iconColor, modifier = Modifier.size(20.dp))
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Card(
    modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 16.dp)
        .clickable { isCommentsExpanded = true },
    shape = RoundedCornerShape(16.dp),
    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
    elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
) {
    Column(modifier = Modifier.padding(16.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                "Comments",
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                "${comments.size}",
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        // صرف پہلا کمنٹ preview کے طور پر
        if (comments.isNotEmpty()) {
            Spacer(modifier = Modifier.height(8.dp))
            Row(verticalAlignment = Alignment.Top) {
                Box(
                    modifier = Modifier.size(24.dp).clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primary),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        comments.first().author.take(1),
                        color = Color.White,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text(
                        comments.first().text,
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.onSurface,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }
}

// 👇 پھیلی حالت: پوری سکرین پر overlay (تصویر 2 جیسی)
if (isCommentsExpanded) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.5f))
            .clickable { isCommentsExpanded = false }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .heightIn(max = 600.dp)
                .background(MaterialTheme.colorScheme.background)
                .clip(RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp))
        ) {
            // Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "Comments",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.weight(1f)
                )
                Text(
                    "${comments.size}",
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(end = 16.dp)
                )
                IconButton(onClick = { isCommentsExpanded = false }) {
                    Icon(Icons.Default.Close, contentDescription = "Close")
                }
            }
            HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))

            // کمنٹس کی لسٹ
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(comments) { comment ->
                    Row(verticalAlignment = Alignment.Top) {
                        Box(
                            modifier = Modifier.size(36.dp).clip(CircleShape)
                                .background(MaterialTheme.colorScheme.primary),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                comment.author.take(1),
                                color = Color.White,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    "@${comment.author.lowercase().replace(" ", "")}",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    "• just now",
                                    fontSize = 12.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                comment.text,
                                fontSize = 14.sp,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            // Like / Dislike / Reply آئیکنز
                            Row(
                                modifier = Modifier.padding(top = 6.dp),
                                horizontalArrangement = Arrangement.spacedBy(16.dp)
                            ) {
                                Icon(Icons.Default.ThumbUp, contentDescription = "Like",
                                    modifier = Modifier.size(18.dp),
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant)
                                Icon(Icons.Default.ThumbDown, contentDescription = "Dislike",
                                    modifier = Modifier.size(18.dp),
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant)
                                Text("Reply", fontSize = 12.sp, fontWeight = FontWeight.Medium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                        }
                    }
                }
            }

            HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))

            // نیچے input field
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier.size(36.dp).clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primary),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Z", color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                }
                Spacer(modifier = Modifier.width(12.dp))
                OutlinedTextField(
                    value = newComment,
                    onValueChange = { newComment = it },
                    placeholder = { Text("Comment...") },
                    modifier = Modifier.weight(1f),
                    singleLine = true,
                    shape = RoundedCornerShape(20.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.outline
                    )
                )
                if (newComment.isNotBlank()) {
                    IconButton(onClick = {
                        comments.add(0, Comment("Andrew Ainsley", newComment))
                        newComment = ""
                    }) {
                        Icon(Icons.Default.Send, contentDescription = "Send",
                            tint = MaterialTheme.colorScheme.primary)
                    }
                }
            }
        }
    }
}

                Spacer(modifier = Modifier.height(20.dp))

                VideoCard(
                    video = dummyVideos[0],
                    onVideoClick = { navigator.push(VideoPlayerScreen(dummyVideos[0].videoUrl, dummyVideos[0].title)) },
                    onChannelClick = { navigator.push(ChannelScreen()) }
                )
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}
