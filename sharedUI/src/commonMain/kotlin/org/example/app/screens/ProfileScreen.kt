package org.example.app.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.AutoMirrored.Filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import coil3.compose.AsyncImage

private val AccentColor = Color(0xFFFF4757)
private val HighlightRowColor = Color(0xFFEEF0F5)

class ProfileScreen : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        
        var isDarkMode by remember { mutableStateOf(false) }
        var location by remember { mutableStateOf("United States") }
        
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            text = "Profile",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF1A1A1A)
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = { navigator.pop() }) {
                            Icon(
                                imageVector = ArrowBack, // ✅ درست آئیکن
                                contentDescription = "Back",
                                tint = Color(0xFF1A1A1A)
                            )
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
                // 1️⃣ آواتار + نام
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
                    Text(text = "Andrew Ainsley", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1A1A1A))
                }
                
                // 2️⃣ فہرست
                Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)) {
                    ProfileRow(
                        icon = Icons.Default.PlayCircle,
                        label = "My Profile",
                        onClick = { navigator.push(ChannelScreen()) } // ✅ اب یہ کام کرے گا
                    )
                    
                    DarkModeRow(
                        isDarkMode = isDarkMode,
                        onToggle = { isDarkMode = !isDarkMode }
                    )
                    
                    LocationRow(
                        location = location,
                        onClick = { }
                    )
                    
                    ProfileRow(
                        icon = Icons.Default.Settings,
                        label = "Settings",
                        onClick = { }
                    )
                    
                    ProfileRow(
                        icon = Icons.Outlined.Info, // ✅ درست آئیکن
                        label = "Help Center",
                        onClick = { }
                    )
                    
                    LogoutRow(onClick = { })
                }
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}

// --- Helper Composables ---

@Composable
private fun ProfileRow(icon: androidx.compose.ui.graphics.vector.ImageVector, label: String, onClick: () -> Unit) {
    Row(modifier = Modifier.fillMaxWidth().clickable(onClick = onClick).padding(vertical = 16.dp), verticalAlignment = Alignment.CenterVertically) {
        Icon(imageVector = icon, contentDescription = null, tint = Color(0xFF1A1A1A), modifier = Modifier.size(24.dp))
        Spacer(modifier = Modifier.width(16.dp))
        Text(text = label, fontSize = 16.sp, color = Color(0xFF1A1A1A), modifier = Modifier.weight(1f))
        Icon(Icons.Default.ChevronRight, contentDescription = null, tint = Color(0xFF999999), modifier = Modifier.size(20.dp))
    }
}

@Composable
private fun DarkModeRow(isDarkMode: Boolean, onToggle: () -> Unit) {
    Row(modifier = Modifier.fillMaxWidth().clickable(onClick = onToggle).padding(vertical = 16.dp), verticalAlignment = Alignment.CenterVertically) {
        Icon(Icons.Default.Visibility, contentDescription = null, tint = Color(0xFF1A1A1A), modifier = Modifier.size(24.dp))
        Spacer(modifier = Modifier.width(16.dp))
        Text(text = "Dark Mode", fontSize = 16.sp, color = Color(0xFF1A1A1A), modifier = Modifier.weight(1f))
        Switch(
            checked = isDarkMode,
            onCheckedChange = { onToggle() },
            colors = SwitchDefaults.colors(checkedThumbColor = AccentColor, checkedTrackColor = AccentColor.copy(alpha = 0.3f))
        )
    }
}

@Composable
private fun LocationRow(location: String, onClick: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth().clickable(onClick = onClick).background(color = HighlightRowColor, shape = RoundedCornerShape(8.dp)).padding(horizontal = 16.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(Icons.Default.LocationOn, contentDescription = null, tint = Color(0xFF6688CC), modifier = Modifier.size(24.dp))
        Spacer(modifier = Modifier.width(16.dp))
        Text(text = "Location", fontSize = 16.sp, fontWeight = FontWeight.Medium, color = Color(0xFF1A1A1A), modifier = Modifier.weight(1f))
        Text(text = location, fontSize = 14.sp, color = Color(0xFF6688CC), modifier = Modifier.padding(end = 8.dp))
        Icon(Icons.Default.ChevronRight, contentDescription = null, tint = Color(0xFF6688CC), modifier = Modifier.size(20.dp))
    }
}

@Composable
private fun LogoutRow(onClick: () -> Unit) {
    Row(modifier = Modifier.fillMaxWidth().clickable(onClick = onClick).padding(vertical = 16.dp), verticalAlignment = Alignment.CenterVertically) {
        Icon(Icons.Default.Logout, contentDescription = null, tint = AccentColor, modifier = Modifier.size(24.dp))
        Spacer(modifier = Modifier.width(16.dp))
        Text(text = "Logout", fontSize = 16.sp, fontWeight = FontWeight.Medium, color = AccentColor)
    }
}
