package org.example.app.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import org.example.app.dummyVideos
import org.example.app.core.ThemeState

class VideoListScreen : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        var selectedTab by remember { mutableStateOf(0) }

        BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
            val isDesktop = maxWidth >= 840.dp

            if (isDesktop) {
                DesktopLayout(
                    selectedTab = selectedTab,
                    onTabChange = { selectedTab = it },
                    navigator = navigator
                )
            } else {
                MobileLayout(
                    selectedTab = selectedTab,
                    onTabChange = { selectedTab = it },
                    navigator = navigator
                )
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
                title = {
                    Text("LOG", fontSize = 24.sp, fontWeight = FontWeight.Bold, letterSpacing = 2.sp, color = MaterialTheme.colorScheme.primary)
                },
                actions = {
                    IconButton(onClick = { navigator.push(SearchScreen()) }) {
                        Icon(Icons.Default.Search, contentDescription = "Search", tint = MaterialTheme.colorScheme.onSurface)
                    }
                    IconButton(onClick = { navigator.push(NotificationsScreen()) }) {
                        Icon(Icons.Default.Notifications, contentDescription = "Notifications", tint = MaterialTheme.colorScheme.onSurface)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surface)
            )
        },
        bottomBar = {
            NavigationBar(
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = MaterialTheme.colorScheme.onSurfaceVariant,
                tonalElevation = 0.dp
            ) {
                NavItem(Icons.Default.Home, "Home", selectedTab == 0) { onTabChange(0) }
                NavItem(Icons.Default.PlayCircle, "Reels", selectedTab == 1) {
                    onTabChange(1)
                    navigator.push(ReelsScreen())
                }

                // ➕ Add بٹن (درمیان، ہمیشہ بڑا)
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Add, contentDescription = "Add", modifier = Modifier.size(28.dp)) },
                    label = { },
                    selected = false,
                    onClick = { onTabChange(2); navigator.push(UploadScreen()) },
                    colors = NavigationBarItemDefaults.colors(
                        unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                        indicatorColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.12f)
                    )
                )

                NavItem(Icons.Default.Mail, "Inbox", selectedTab == 3) {
                    onTabChange(3)
                    navigator.push(InboxScreen())
                }
                NavItem(Icons.Default.Person, "Profile", selectedTab == 4) {
                    onTabChange(4)
                    if (org.example.app.core.AuthState.isLoggedIn) {
                    navigator.push(ProfileScreen())
                } else {
                    navigator.push(AuthScreen())
                }
            }
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->
        VideoGrid(padding = padding, navigator = navigator, twoColumns = false)
    }
}

// 👇 سلیکٹڈ ٹیب کا نیا انداز
@Composable
private fun RowScope.NavItem(   // <-- یہاں RowScope. شامل کیا ہے
    icon: ImageVector,
    label: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    NavigationBarItem(
        icon = {
            Icon(
                icon,
                contentDescription = label,
                modifier = Modifier.size(if (selected) 28.dp else 24.dp)
            )
        },
        label = {
            if (selected) {
                Text(label, fontSize = 12.sp, fontWeight = FontWeight.Bold)
            }
        },
        selected = selected,
        onClick = onClick,
        colors = NavigationBarItemDefaults.colors(
            selectedIconColor = MaterialTheme.colorScheme.primary,
            selectedTextColor = MaterialTheme.colorScheme.primary,
            unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
            indicatorColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.12f)
        )
    )
}

// ---------------- ڈیسک ٹاپ لے آؤٹ ----------------
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
                        Text("My Video App", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
                        Spacer(modifier = Modifier.width(24.dp))
                        OutlinedTextField(
                            value = "", onValueChange = { },
                            placeholder = { Text("Search accounts", color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 14.sp) },
                            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant) },
                            modifier = Modifier.width(320.dp).height(44.dp),
                            singleLine = true,
                            shape = RoundedCornerShape(24.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = MaterialTheme.colorScheme.primary,
                                unfocusedBorderColor = MaterialTheme.colorScheme.outline
                            )
                        )
                    }
                },
                actions = {
                    TextButton(onClick = { navigator.push(UploadScreen()) }) {
                        Text("Upload", color = MaterialTheme.colorScheme.onSurface)
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = { navigator.push(AuthScreen()) },
                        shape = RoundedCornerShape(24.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                    ) {
                        Text("Log in", color = MaterialTheme.colorScheme.onPrimary)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surface)
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->
        Row(modifier = Modifier.fillMaxSize().padding(padding)) {
            NavigationRail(
                containerColor = MaterialTheme.colorScheme.surface,
                header = { Icon(Icons.Default.Home, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.padding(bottom = 16.dp)) }
            ) {
                RailItem(Icons.Default.Home, "Home", selectedTab == 0) { onTabChange(0) }
                RailItem(Icons.Default.PlayCircle, "Clips", selectedTab == 1) { onTabChange(1) }
                RailItem(Icons.Default.Mail, "Inbox", selectedTab == 2) { onTabChange(2) }
                RailItem(Icons.Default.Person, "My Profile", selectedTab == 3) { onTabChange(3) }
                RailItem(Icons.Default.Download, "Downloads", selectedTab == 4) { onTabChange(4) }
                RailItem(Icons.Default.MonetizationOn, "Monetization", selectedTab == 5) { onTabChange(5) }
                RailItem(Icons.Default.DarkMode, "Dark Mode", selectedTab == 6) { ThemeState.isDarkMode = !ThemeState.isDarkMode }
                RailItem(Icons.Default.Settings, "Settings", selectedTab == 7) { onTabChange(7) }
                RailItem(Icons.Default.Help, "Help Center", selectedTab == 8) { onTabChange(8) }
            }

            Box(modifier = Modifier.weight(1f).fillMaxHeight().padding(16.dp)) {
                when (selectedTab) {
                    0 -> VideoGrid(padding = PaddingValues(0.dp), navigator = navigator, twoColumns = true)
                    1 -> ReelsScreen().Content()          // 👈 .Content() لگانا ضروری ہے
                    2 -> InboxScreen().Content()          // 👈 .Content() لگانا ضروری ہے
                    3 -> ChannelScreen().Content()        // 👈 .Content() لگانا ضروری ہے
                    4 -> DownloadsScreen()                // 👈 یہ @Composable فنکشن ہے، () کافی ہے
                    5 -> MonetizationScreen().Content()   // 👈 .Content() لگانا ضروری ہے
                    6 -> VideoGrid(padding = PaddingValues(0.dp), navigator = navigator, twoColumns = true)
                    7 -> SettingsScreen()                 // 👈 یہ @Composable فنکشن ہے
                    8 -> HelpCenterScreen()               // 👈 یہ @Composable فنکشن ہے
                }
            }
        }
    }
}

@Composable
private fun RailItem(icon: ImageVector, label: String, selected: Boolean, onClick: () -> Unit) {
    NavigationRailItem(
        icon = { Icon(icon, contentDescription = null) },
        label = { Text(label, fontSize = 11.sp) },
        selected = selected,
        onClick = onClick,
        colors = NavigationRailItemDefaults.colors(
            selectedIconColor = MaterialTheme.colorScheme.primary,
            selectedTextColor = MaterialTheme.colorScheme.primary,
            unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
            unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
        )
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
                            VideoCard(
                                video = video,
                                onVideoClick = { navigator.push(VideoPlayerScreen(video.videoUrl, video.title)) },
                                onChannelClick = { navigator.push(ChannelScreen()) }
                            )
                        }
                    }
                    if (row.size == 1) Spacer(modifier = Modifier.weight(1f))
                }
            }
        } else {
            items(dummyVideos) { video ->
                VideoCard(
                    video = video,
                    onVideoClick = { navigator.push(VideoPlayerScreen(video.videoUrl, video.title)) },
                    onChannelClick = { navigator.push(ChannelScreen()) }
                )
            }
        }
    }
}
