package org.example.app.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import coil3.compose.AsyncImage

private val AccentColor = Color(0xFFFF4757)

class ProfileScreen : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        var isDarkMode by remember { mutableStateOf(false) }

        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Profile", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1A1A1A)) },
                    navigationIcon = {
                        IconButton(onClick = { navigator.pop() }) {
                            Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color(0xFF1A1A1A))
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
                // آواتار + نام
                Column(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box {
                        AsyncImage(
                            model = "https://picsum.photos/seed/profile/200/200",
                            contentDescription = "Profile Picture",
                            modifier = Modifier.size(100.dp).clip(CircleShape),
                            contentScale = ContentScale.Crop
                        )
                        Box(
                            modifier = Modifier
                                .align(Alignment.BottomEnd)
                                .size(28.dp)
                                .clip(CircleShape)
                                .background(AccentColor),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.Edit, contentDescription = "Edit", tint = Color.White, modifier = Modifier.size(14.dp))
                        }
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    Text("Andrew Ainsley", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1A1A1A))
                }

                // فہرست (سب ایک جیسی)
                Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)) {
                    ProfileRow(Icons.Default.PlayCircle, "My Profile") { navigator.push(ChannelScreen()) }
                    ProfileRow(Icons.Default.Download, "Downloads") { /* Downloads screen */ }
                    DarkModeRow(isDarkMode) { isDarkMode = !isDarkMode }
                    ProfileRow(Icons.Default.LocationOn, "Location", value = "United States") { /* Location screen */ }
                    ProfileRow(Icons.Default.Settings, "Settings") { }
                    ProfileRow(Icons.Outlined.Info, "Help Center") { }
                    LogoutRow { }
                }
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}

// عام قطار — اب optional value کے ساتھ (Location بھی اسی کو استعمال کرتا ہے)
@Composable
private fun ProfileRow(icon: ImageVector, label: String, value: String? = null, onClick: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth().clickable(onClick = onClick).padding(vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, contentDescription = null, tint = Color(0xFF1A1A1A), modifier = Modifier.size(24.dp))
        Spacer(modifier = Modifier.width(16.dp))
        Text(label, fontSize = 16.sp, color = Color(0xFF1A1A1A), modifier = Modifier.weight(1f))
        if (value != null) {
            Text(value, fontSize = 14.sp, color = Color(0xFF999999), modifier = Modifier.padding(end = 8.dp))
        }
        Icon(Icons.Default.ChevronRight, contentDescription = null, tint = Color(0xFF999999), modifier = Modifier.size(20.dp))
    }
}

@Composable
private fun DarkModeRow(isDarkMode: Boolean, onToggle: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth().clickable(onClick = onToggle).padding(vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(Icons.Default.Visibility, contentDescription = null, tint = Color(0xFF1A1A1A), modifier = Modifier.size(24.dp))
        Spacer(modifier = Modifier.width(16.dp))
        Text("Dark Mode", fontSize = 16.sp, color = Color(0xFF1A1A1A), modifier = Modifier.weight(1f))
        Switch(
            checked = isDarkMode,
            onCheckedChange = { onToggle() },
            colors = SwitchDefaults.colors(checkedThumbColor = AccentColor, checkedTrackColor = AccentColor.copy(alpha = 0.3f))
        )
    }
}

@Composable
private fun LogoutRow(onClick: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth().clickable(onClick = onClick).padding(vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(Icons.Default.Logout, contentDescription = null, tint = AccentColor, modifier = Modifier.size(24.dp))
        Spacer(modifier = Modifier.width(16.dp))
        Text("Logout", fontSize = 16.sp, fontWeight = FontWeight.Medium, color = AccentColor)
    }
}
