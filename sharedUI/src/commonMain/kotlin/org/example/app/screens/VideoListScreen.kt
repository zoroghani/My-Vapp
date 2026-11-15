package org.example.app.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import org.example.app.dummyVideos

private val AccentColor = Color(0xFF6200EE)

class VideoListScreen : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        var selectedTab by remember { mutableStateOf(0) }

        // ✅ Adaptive breakpoint: 840dp سے زیادہ = ڈیسکٹاپ
        BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
            val isDesktop = maxWidth >= 840.dp

            if (isDesktop) {
                DesktopLayout(selectedTab, onTabChange = { selectedTab = it }, navigator)
            } else {
                MobileLayout(selectedTab, onTabChange = { selectedTab = it }, navigator)
            }
        }
    }
}

// ---------------- موبائل لے آؤٹ ----------------
@Composable
private fun MobileLayout(
    selectedTab: Int,
    onTabChange: (Int) -> Unit,
    navigator: cafe.adriel.voyager.navigator.Navigator
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("LOG", fontSize = 24.sp, fontWeight = FontWeight.Bold, letterSpacing = 2.sp, color = AccentColor) },
                actions = {
                    IconButton(onClick = { navigator.push(SearchScreen()) }) {
                        Icon(Icons.Default.Search, contentDescription = "Search", tint = Color(0xFF1F2937))
                    }
                    IconButton(onClick = { }) {
                        Icon(Icons.Default.Notifications, contentDescription = "Notifications", tint = Color(0xFF1F2937))
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFFE8EAF6).copy(alpha = 0.9f))
            )
        },
        bottomBar = {
            NavigationBar(containerColor = Color.White.copy(alpha = 0.8f), tonalElevation = 0.dp) {
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
                    label = { Text("Home", fontSize = 10.sp) },
                    selected = selectedTab == 0, onClick = { onTabChange(0) }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.PlayCircle, contentDescription = "Reels") },
                    label = { Text("Reels", fontSize = 10.sp) },
                    selected = selectedTab == 1, onClick = { onTabChange(1) }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Add, contentDescription = "Add", modifier = Modifier.size(28.dp)) },
                    label = { }, selected = false,
                    onClick = { onTabChange(2); navigator.push(UploadScreen()) }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Mail, contentDescription = "Inbox") },
                    label = { Text("Inbox", fontSize = 10.sp) },
                    selected = selectedTab == 3, onClick = { onTabChange(3) }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Person, contentDescription = "Profile") },
                    label = { Text("Profile", fontSize = 10.sp) },
                    selected = selectedTab == 4, onClick = { onTabChange(4); navigator.push(ProfileScreen()) }
                )
            }
        },
        containerColor = Color(0xFFE8EAF6)
    ) { padding ->
        VideoGrid(padding = padding, navigator = navigator)
    }
}

// ---------------- ڈیسکٹاپ لے آؤٹ ----------------
@Composable
private fun DesktopLayout(
    selectedTab: Int,
    onTabChange: (Int) -> Unit,
    navigator: cafe.adriel.voyager.navigator.Navigator
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("My Video App", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1A1A1A))
                        Spacer(modifier = Modifier.width(24.dp))
                        OutlinedTextField(
                            value = "", onValueChange = { },
                            placeholder = { Text("Search accounts", color = Color.Gray, fontSize = 14.sp) },
                            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = Color.Gray) },
                            modifier = Modifier.width(320.dp).height(44.dp),
                            singleLine = true,
                            shape = RoundedCornerShape(24.dp)
                        )
                    }
                },
                actions = {
                    TextButton(onClick = { navigator.push(UploadScreen()) }) { Text("Upload", color = Color(0xFF1A1A1A)) }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = { },
                        shape = RoundedCornerShape(24.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF4757))
                    ) { Text("Log in", color = Color.White) }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        },
        containerColor = Color.White
    ) { padding ->
        Row(modifier = Modifier.fillMaxSize().padding(padding)) {
            // سائیڈ مینو
            NavigationRail(
                containerColor = Color.White,
                header = { Icon(Icons.Default.Home, contentDescription = null, tint = AccentColor) }
            ) {
                RailItem(Icons.Default.Home, "Home", selectedTab == 0) { onTabChange(0) }
                RailItem(Icons.Default.PlayCircle, "Clips", selectedTab == 1) { onTabChange(1) }
                RailItem(Icons.Default.Mail, "Inbox", selectedTab == 2) { onTabChange(2) }
                RailItem(Icons.Default.Person, "My Profile", selectedTab == 3) { onTabChange(3); navigator.push(ChannelScreen()) }
                RailItem(Icons.Default.Download, "Download", selectedTab == 4) { onTabChange(4) }
                RailItem(Icons.Default.DarkMode, "Dark Mode", selectedTab == 5) { onTabChange(5) }
                RailItem(Icons.Default.Settings, "Settings", selectedTab == 6) { onTabChange(6) }
                RailItem(Icons.Default.Help, "Help Center", selectedTab == 7) { onTabChange(7) }
            }
            // مرکزی مواد
            VideoGrid(padding = PaddingValues(0.dp), navigator = navigator, twoColumns = true)
        }
    }
}

@Composable
private fun RailItem(icon: androidx.compose.ui.graphics.vector.ImageVector, label: String, selected: Boolean, onClick: () -> Unit) {
    NavigationRailItem(
        icon = { Icon(icon, contentDescription = null) },
        label = { Text(label, fontSize = 11.sp) },
        selected = selected,
        onClick = onClick,
        colors = NavigationRailItemDefaults.colors(selectedIconColor = AccentColor, selectedTextColor = AccentColor)
    )
}

// ---------------- مشترکہ ویڈیو گرڈ ----------------
@Composable
private fun VideoGrid(padding: PaddingValues, navigator: cafe.adriel.voyager.navigator.Navigator, twoColumns: Boolean = false) {
    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(padding).padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        if (twoColumns) {
            items(dummyVideos.chunked(2)) { row ->
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    row.forEach { video ->
                        Box(modifier = Modifier.weight(1f)) {
                            VideoCard(video = video) {
                                navigator.push(VideoPlayerScreen(video.videoUrl, video.title))
                            }
                        }
                    }
                    // اگر طاق تعداد ہو تو خالی جگہ
                    if (row.size == 1) Spacer(modifier = Modifier.weight(1f))
                }
            }
        } else {
            items(dummyVideos) { video ->
                VideoCard(video = video) {
                    navigator.push(VideoPlayerScreen(video.videoUrl, video.title))
                }
            }
        }
    }
}
