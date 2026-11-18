package org.example.app.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import org.example.app.core.platform.rememberImagePicker
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
import org.example.app.core.AuthState
import org.example.app.core.ThemeState

private val AccentColor = Color(0xFF3066E8)

class ProfileScreen : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val pickImage = rememberImagePicker { bytes -> AuthState.profileImage = bytes }

        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Profile", fontSize = 20.sp, fontWeight = FontWeight.Bold) },
                    navigationIcon = {
                        IconButton(onClick = { navigator.pop() }) {
                            Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.surface,
                        titleContentColor = MaterialTheme.colorScheme.onSurface
                    )
                )
            },
            containerColor = MaterialTheme.colorScheme.background
        ) { padding ->
            Column(
                modifier = Modifier.fillMaxSize().padding(padding).verticalScroll(rememberScrollState())
            ) {
                // آواتار + نام
                Column(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box {
                        AsyncImage(
                            model = AuthState.profileImage ?: "https://picsum.photos/seed/profile/200/200",
                            contentDescription = "Profile Picture",
                            modifier = Modifier.size(100.dp).clip(CircleShape),
                            contentScale = ContentScale.Crop
                        )
                        Box(
                            modifier = Modifier.align(Alignment.BottomEnd).size(28.dp).clip(CircleShape)
                                .background(AccentColor)
                                .clickable { pickImage() },   // 👈 یہاں pickImage() کال ہو رہا ہے
                            contentAlignment = Alignment.Center
                         ) {
                            Icon(Icons.Default.Edit, contentDescription = "Edit", tint = Color.White, modifier = Modifier.size(14.dp))
                        }
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = if (AuthState.userName.isNotBlank()) AuthState.userName else "Andrew Ainsley",
                        fontSize = 18.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface
                    )
                }

                // فہرست
                Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)) {
                    ProfileRow(Icons.Default.Person, "My Profile") { navigator.push(ChannelScreen()) }
                    ProfileRow(Icons.Default.Download, "Downloads") { navigator.push(DownloadsPage()) }
                    ProfileRow(Icons.Default.MonetizationOn, "Monetization") { navigator.push(MonetizationScreen()) }
                    DarkModeRow()
                    ProfileRow(Icons.Default.LocationOn, "Location", value = "United States") { navigator.push(LocationPage()) }
                    ProfileRow(Icons.Default.Settings, "Settings") { navigator.push(SettingsPage()) }
                    ProfileRow(Icons.Outlined.Info, "Help Center") { navigator.push(HelpPage()) }
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    // ✅ یہاں Login / Logout کی منطق موجود ہے
                    if (AuthState.isLoggedIn) {
                        LogoutRow { 
                            AuthState.isLoggedIn = false 
                            AuthState.userName = ""
                            AuthState.userEmail = ""
                        }
                    } else {
                        ProfileRow(Icons.Default.Login, "Log in") { 
                            navigator.push(AuthScreen()) 
                        }
                    }
                }
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}

// 👇 یہ تمام فنکشنز کلاس کے باہر ہیں، اس لیے انہیں کوئی ایرر نہیں آئے گا

@Composable
private fun ProfileRow(icon: ImageVector, label: String, value: String? = null, onClick: () -> Unit) {
    Row(modifier = Modifier.fillMaxWidth().clickable(onClick = onClick).padding(vertical = 16.dp), verticalAlignment = Alignment.CenterVertically) {
        Icon(icon, contentDescription = null, tint = MaterialTheme.colorScheme.onSurface, modifier = Modifier.size(24.dp))
        Spacer(modifier = Modifier.width(16.dp))
        Text(label, fontSize = 16.sp, color = MaterialTheme.colorScheme.onSurface, modifier = Modifier.weight(1f))
        if (value != null) Text(value, fontSize = 14.sp, color = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.padding(end = 8.dp))
        Icon(Icons.Default.ChevronRight, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.size(20.dp))
    }
}

@Composable
private fun DarkModeRow() {
    Row(modifier = Modifier.fillMaxWidth().clickable { ThemeState.isDarkMode = !ThemeState.isDarkMode }.padding(vertical = 16.dp), verticalAlignment = Alignment.CenterVertically) {
        Icon(Icons.Default.Visibility, contentDescription = null, tint = MaterialTheme.colorScheme.onSurface, modifier = Modifier.size(24.dp))
        Spacer(modifier = Modifier.width(16.dp))
        Text("Dark Mode", fontSize = 16.sp, color = MaterialTheme.colorScheme.onSurface, modifier = Modifier.weight(1f))
        Switch(
            checked = ThemeState.isDarkMode,
            onCheckedChange = { ThemeState.isDarkMode = it },
            colors = SwitchDefaults.colors(checkedThumbColor = AccentColor, checkedTrackColor = AccentColor.copy(alpha = 0.3f))
        )
    }
}

@Composable
private fun LogoutRow(onClick: () -> Unit) {
    Row(modifier = Modifier.fillMaxWidth().clickable(onClick = onClick).padding(vertical = 16.dp), verticalAlignment = Alignment.CenterVertically) {
        Icon(Icons.Default.Logout, contentDescription = null, tint = AccentColor, modifier = Modifier.size(24.dp))
        Spacer(modifier = Modifier.width(16.dp))
        Text("Logout", fontSize = 16.sp, fontWeight = FontWeight.Medium, color = AccentColor)
    }
}
