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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import coil3.compose.AsyncImage
import org.example.app.dummyVideos

private val AccentColor = Color(0xFFFF4757)

class ChannelScreen : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        var selectedTab by remember { mutableStateOf(0) }
        var isFollowing by remember { mutableStateOf(false) }

        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("World of Music", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1A1A1A)) },
                    navigationIcon = {
                        IconButton(onClick = { navigator.pop() }) {
                            Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color(0xFF1A1A1A))
                        }
                    },
                    actions = {
                        IconButton(onClick = { }) {
                            Icon(Icons.Default.MoreVert, contentDescription = "More", tint = Color(0xFF1A1A1A))
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
                )
            },
            containerColor = Color.White
        ) { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .verticalScroll(rememberScrollState())
            ) {
                // 1️⃣ چینل ہیڈر
                Column(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    AsyncImage(
                        model = "https://picsum.photos/seed/channel/200/200",
                        contentDescription = "Channel Avatar",
                        modifier = Modifier.size(96.dp).clip(CircleShape),
                        contentScale = ContentScale.Crop
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("World of Music", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1A1A1A))
                        Spacer(modifier = Modifier.width(6.dp))
                        Icon(Icons.Default.CheckCircle, contentDescription = "Verified", tint = AccentColor, modifier = Modifier.size(20.dp))
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    // Follow بٹن
                    Button(
                        onClick = { isFollowing = !isFollowing },
                        shape = RoundedCornerShape(24.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (isFollowing) Color(0xFFE0E0E0) else AccentColor,
                            contentColor = if (isFollowing) Color(0xFF1A1A1A) else Color.White
                        )
                    ) {
                        Text(if (isFollowing) "Following" else "Follow", fontWeight = FontWeight.Bold)
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "9.5M followers  ·  769 videos",
                        fontSize = 13.sp,
                        color = Color(0xFF666666)
                    )
                }

                // 2️⃣ ٹیبز
                TabRow(
                    selectedTabIndex = selectedTab,
                    containerColor = Color.White,
                    contentColor = AccentColor
                ) {
                    Tab(selected = selectedTab == 0, onClick = { selectedTab = 0 },
                        text = { Text("Videos", fontWeight = if (selectedTab == 0) FontWeight.Bold else FontWeight.Normal) })
                    Tab(selected = selectedTab == 1, onClick = { selectedTab = 1 },
                        text = { Text("About", fontWeight = if (selectedTab == 1) FontWeight.Bold else FontWeight.Normal) })
                }

                // 3️⃣ ٹیب کا مواد
                if (selectedTab == 0) {
                    dummyVideos.forEach { video ->
                        ChannelVideoItem(video) {
                            navigator.push(VideoPlayerScreen(video.videoUrl, video.title))
                        }
                    }
                } else {
                    Column(modifier = Modifier.fillMaxWidth().padding(20.dp)) {
                        Text("About", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1A1A1A))
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "World of Music — International music, dance concerts and live performances from around the globe. New videos every week.",
                            fontSize = 14.sp,
                            color = Color(0xFF444444),
                            textAlign = TextAlign.Start
                        )
                    }
                }
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}

// چینل کی ویڈیو آئٹم (بڑا تھمب + duration + نیچے لوگو/ٹائٹل/میٹا)
@Composable
private fun ChannelVideoItem(video: org.example.app.DummyVideo, onClick: () -> Unit) {
    Column(modifier = Modifier.fillMaxWidth().clickable(onClick = onClick).padding(horizontal = 16.dp, vertical = 10.dp)) {
        // تھمب نیل + duration
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .clip(RoundedCornerShape(14.dp))
                .background(Color(0xFFE0E0E0))
        ) {
            AsyncImage(
                model = video.thumbnailUrl,
                contentDescription = video.title,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
            Box(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(8.dp)
                    .background(Color.Black.copy(alpha = 0.75f), RoundedCornerShape(4.dp))
                    .padding(horizontal = 6.dp, vertical = 2.dp)
            ) {
                Text("56:44", color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Medium)
            }
        }
        Spacer(modifier = Modifier.height(10.dp))
        Row(verticalAlignment = Alignment.Top) {
            AsyncImage(
                model = "https://picsum.photos/seed/channel/80/80",
                contentDescription = null,
                modifier = Modifier.size(36.dp).clip(CircleShape),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(video.title, fontSize = 14.sp, fontWeight = FontWeight.Medium, color = Color(0xFF1A1A1A), maxLines = 2)
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "${video.channel}  ·  6.4M views  ·  2 days ago",
                    fontSize = 12.sp,
                    color = Color(0xFF666666)
                )
            }
            Icon(Icons.Default.MoreVert, contentDescription = "More", tint = Color(0xFF666666), modifier = Modifier.size(20.dp))
        }
    }
}
