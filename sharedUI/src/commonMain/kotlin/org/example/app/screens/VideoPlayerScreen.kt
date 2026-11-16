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

        // کمنٹ فیچر — فنکشنل
        var commentText by remember { mutableStateOf("") }
        val comments = remember {
            mutableStateListOf(
                Comment("Tech Fan", "Great video! 🙌"),
                Comment("Dev Lover", "Very helpful, thanks.")
            )
        }

        // Snackbar
        val snackbarHostState = remember { SnackbarHostState() }
        var snackbarMessage by remember { mutableStateOf<String?>(null) }
        LaunchedEffect(snackbarMessage) {
            snackbarMessage?.let { msg ->
                snackbarHostState.showSnackbar(msg)
                snackbarMessage = null
            }
        }

        Scaffold(
            containerColor = Color(0xFFF5F5F7),
            snackbarHost = { SnackbarHost(snackbarHostState) }
        ) { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .verticalScroll(rememberScrollState())
            ) {
                // 1️⃣ ویڈیو پلئر
                NativeVideoPlayer(
                    videoUrl = videoUrl,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(240.dp)
                        .background(Color.Black)
                )

                // 2️⃣ ٹائٹل + میٹا + "more" (کلکیبل → ڈسکرپشن)
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp)
                ) {
                    Text(
                        text = videoTitle,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1A1A1A)
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "37 views  •  2y ago  •  ",
                            fontSize = 12.sp,
                            color = Color(0xFF666666)
                        )
                        // 👇 more بٹن — کلک پر ڈسکرپشن کھلے گی
                        Text(
                            text = if (isDescriptionExpanded) "less" else "more",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = AccentColor,
                            modifier = Modifier.clickable { isDescriptionExpanded = !isDescriptionExpanded }
                        )
                    }
                    // 👇 ڈسکرپشن (صرف more دبانے پر)
                    if (isDescriptionExpanded) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "This is the official video description. It contains details about the video, tags, and links. " +
                                    "Watch till the end for more amazing content! #video #trending",
                            fontSize = 13.sp,
                            color = Color(0xFF444444)
                        )
                    }
                }

                // 3️⃣ ایک ہی لائن: [آواتار] [FOLLOW] ... [❤️] [↪️] [⬇️]
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    // بائیں طرف: آواتار + FOLLOW (علیحدہ علیحدہ)
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        // آواتار (کلکیبل → ChannelScreen)
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(Color(0xFF008060))
                                .clickable { navigator.push(ChannelScreen()) },
                            contentAlignment = Alignment.Center
                        ) {
                            Text("Z", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                        }
                        // FOLLOW بٹن (علیحدہ)
                        Button(
                            onClick = { isFollowed = !isFollowed },
                            shape = RoundedCornerShape(20.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (isFollowed) Color(0xFFE0E0E0) else AccentColor,
                                contentColor = if (isFollowed) Color(0xFF1A1A1A) else Color.White
                            ),
                            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
                        ) {
                            Text(
                                text = if (isFollowed) "Following" else "Follow",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    // دائیں طرف: Like + Forward + Download
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // ❤️ Like
                        IconButton(
                            onClick = { isLiked = !isLiked },
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(Color(0xFFF0F0F0))
                        ) {
                            Icon(
                                imageVector = if (isLiked) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                                contentDescription = "Like",
                                tint = if (isLiked) Color(0xFFE53935) else Color(0xFF1A1A1A),
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        // ↪️ Forward (شیئر کی جگہ)
                        IconButton(
                            onClick = { snackbarMessage = "Forwarded!" },
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(Color(0xFFF0F0F0))
                        ) {
                            Icon(
                                imageVector = Icons.Default.Forward,
                                contentDescription = "Forward",
                                tint = Color(0xFF1A1A1A),
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        // ⬇️ Download
                        IconButton(
                            onClick = { snackbarMessage = "Download started..." },
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(Color(0xFFF0F0F0))
                        ) {
                            Icon(
                                imageVector = Icons.Default.Download,
                                contentDescription = "Download",
                                tint = Color(0xFF1A1A1A),
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // 4️⃣ Comments — فنکشنل (گلاس کارڈ)
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White.copy(alpha = 0.7f)
                    ),
                    border = BorderStroke(1.dp, Color.White.copy(alpha = 0.5f)),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Comments (${comments.size})",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color(0xFF666666)
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        // 👇 کمنٹس کی لسٹ
                        comments.forEach { comment ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 6.dp),
                                verticalAlignment = Alignment.Top
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(28.dp)
                                        .clip(CircleShape)
                                        .background(Color(0xFF008060)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = comment.author.take(1),
                                        color = Color.White,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                                Spacer(modifier = Modifier.width(10.dp))
                                Column {
                                    Text(
                                        comment.author,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF1A1A1A)
                                    )
                                    Text(
                                        comment.text,
                                        fontSize = 13.sp,
                                        color = Color(0xFF333333)
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // 👇 نیا کمنٹ لکھنے کا ان پٹ + Send بٹن
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(28.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFF008060)),
                                contentAlignment = Alignment.Center
                            ) {
                                Text("Z", color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            OutlinedTextField(
                                value = commentText,
                                onValueChange = { commentText = it },
                                placeholder = { Text("Comment...", color = Color.Gray, fontSize = 13.sp) },
                                modifier = Modifier.weight(1f),
                                singleLine = true,
                                shape = RoundedCornerShape(20.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = Color(0xFF008060),
                                    unfocusedBorderColor = Color(0xFFE0E0E0)
                                )
                            )
                            // 👇 Send بٹن
                            IconButton(
                                onClick = {
                                    if (commentText.isNotBlank()) {
                                        comments.add(0, Comment("Andrew Ainsley", commentText))
                                        commentText = ""
                                        snackbarMessage = "Comment posted!"
                                    }
                                }
                            ) {
                                Icon(
                                    Icons.Default.Send,
                                    contentDescription = "Send",
                                    tint = if (commentText.isNotBlank()) AccentColor else Color.Gray
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // 5️⃣ Related ویڈیو (ہوم والا VideoCard)
                VideoCard(
                    video = dummyVideos[0],
                    onVideoClick = {
                        navigator.push(VideoPlayerScreen(dummyVideos[0].videoUrl, dummyVideos[0].title))
                    },
                    onChannelClick = { navigator.push(ChannelScreen()) }
                )

                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}
