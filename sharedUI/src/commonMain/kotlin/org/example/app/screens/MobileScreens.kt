package org.example.app.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.runtime.mutableStateListOf
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import coil3.compose.AsyncImage
import org.example.app.dummyVideos
import androidx.compose.ui.graphics.Color
import androidx.compose.material3.Button

private val AccentColor = Color(0xFFFF4757)

// ---------- مشترکہ ٹاپ بار ----------
@Composable
private fun PageTopBar(title: String, onBack: () -> Unit) {
    TopAppBar(
        title = { Text(title, fontSize = 20.sp, fontWeight = FontWeight.Bold) },
        navigationIcon = {
            IconButton(onClick = onBack) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back")
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.surface,
            titleContentColor = MaterialTheme.colorScheme.onSurface
        )
    )
}

// ================= 1) DOWNLOADS =================
class DownloadsPage : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        // 👇 mutableStateListOf تاکہ delete کام کرے
        val downloaded = remember {
            mutableStateListOf(*dummyVideos.take(2).toTypedArray())
        }

        Scaffold(
            topBar = { PageTopBar("Downloads") { navigator.pop() } },
            containerColor = MaterialTheme.colorScheme.background
        ) { padding ->
            if (downloaded.isEmpty()) {
                Box(Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                    Text("No downloads yet", color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            } else {
                Column(
                    modifier = Modifier.fillMaxSize().padding(padding).padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    downloaded.forEach { video ->
                        var isMenuExpanded by remember { mutableStateOf(false) }

                        Card(
                            shape = RoundedCornerShape(14.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                        ) {
                            Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                                AsyncImage(
                                    model = video.thumbnailUrl,
                                    contentDescription = video.title,
                                    modifier = Modifier.size(80.dp).clip(RoundedCornerShape(10.dp)),
                                    contentScale = ContentScale.Crop
                                )
                                Spacer(modifier = Modifier.width(12.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(video.title, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = MaterialTheme.colorScheme.onSurface)
                                    Text(video.channel, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(Icons.Default.CheckCircle, contentDescription = null, tint = Color(0xFF008060), modifier = Modifier.size(14.dp))
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text("Downloaded • ${video.duration}", fontSize = 11.sp, color = Color(0xFF008060))
                                    }
                                }

                                // 👇 play بٹن ہٹا کر 3 ڈاٹ مینیو
                                Box {
                                    IconButton(onClick = { isMenuExpanded = true }) {
                                        Icon(Icons.Default.MoreVert, contentDescription = "More", tint = MaterialTheme.colorScheme.onSurface)
                                    }
                                    DropdownMenu(
                                        expanded = isMenuExpanded,
                                        onDismissRequest = { isMenuExpanded = false },
                                        containerColor = MaterialTheme.colorScheme.surface
                                    ) {
                                        DropdownMenuItem(
                                            text = { Text("Delete", color = MaterialTheme.colorScheme.error) },
                                            onClick = {
                                                downloaded.remove(video)
                                                isMenuExpanded = false
                                            },
                                            leadingIcon = { Icon(Icons.Default.Delete, contentDescription = null, tint = MaterialTheme.colorScheme.error) }
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

// ================= 2) LOCATION =================
class LocationPage : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val countries = listOf("United States", "Pakistan", "United Kingdom", "India", "UAE", "Saudi Arabia", "Canada", "Australia")
        var selected by remember { mutableStateOf("United States") }

        Scaffold(
            topBar = { PageTopBar("Location") { navigator.pop() } },
            containerColor = MaterialTheme.colorScheme.background
        ) { padding ->
            Column(modifier = Modifier.fillMaxSize().padding(padding).padding(16.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text("Select your country", fontSize = 14.sp, color = Color.Gray)
                Spacer(modifier = Modifier.height(8.dp))
                countries.forEach { country ->
                    Row(
                        modifier = Modifier.fillMaxWidth().clickable { selected = country }.padding(vertical = 14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = if (selected == country) Icons.Default.CheckCircle else Icons.Default.RadioButtonUnchecked,
                            contentDescription = null,
                            tint = if (selected == country) AccentColor else Color.Gray
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(country, fontSize = 15.sp, color = MaterialTheme.colorScheme.onSurface)
                    }
                }
            }
        }
    }
}

// ================= 3) SETTINGS =================
class SettingsPage : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        var name by remember { mutableStateOf("Andrew Ainsley") }
        var bio by remember { mutableStateOf("Video creator and tech enthusiast.") }
        var password by remember { mutableStateOf("") }

        val snackbarHostState = remember { SnackbarHostState() }

        Scaffold(
            topBar = { PageTopBar("Settings") { navigator.pop() } },
            snackbarHost = { SnackbarHost(snackbarHostState) },
            containerColor = MaterialTheme.colorScheme.background
        ) { padding ->
            Column(
                modifier = Modifier.fillMaxSize().padding(padding).padding(20.dp).verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Full Name") }, modifier = Modifier.fillMaxWidth(), singleLine = true)
                OutlinedTextField(value = bio, onValueChange = { bio = it }, label = { Text("About / Bio") }, modifier = Modifier.fillMaxWidth().height(120.dp))
                OutlinedTextField(
                    value = password, onValueChange = { password = it },
                    label = { Text("New Password") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    visualTransformation = androidx.compose.ui.text.input.PasswordVisualTransformation()
                )
                Button(
                    onClick = { },
                    modifier = Modifier.fillMaxWidth().height(52.dp),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = AccentColor)
                ) {
                    Text("Save Changes", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

// ================= 4) HELP CENTER =================
class HelpPage : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val faqs = listOf(
            "How do I upload a video?" to "Tap the ➕ button in the bottom navigation, select a video, add a title and description, then tap Upload.",
            "How do I download videos?" to "Open any video, then tap the ⬇️ Download icon below the player.",
            "How do I change my profile photo?" to "Go to Profile and tap the ✏️ edit badge on your photo.",
            "How do I turn on Dark Mode?" to "Open Profile and toggle the Dark Mode switch."
        )
        var expanded by remember { mutableStateOf(-1) }

        Scaffold(
            topBar = { PageTopBar("Help Center") { navigator.pop() } },
            containerColor = MaterialTheme.colorScheme.background
        ) { padding ->
            Column(modifier = Modifier.fillMaxSize().padding(padding).padding(16.dp).verticalScroll(rememberScrollState()), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text("Frequently Asked Questions", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = MaterialTheme.colorScheme.onSurface)
                faqs.forEachIndexed { index, (q, a) ->
                    Card(
                        modifier = Modifier.fillMaxWidth().clickable { expanded = if (expanded == index) -1 else index },
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(q, fontWeight = FontWeight.Medium, fontSize = 14.sp, modifier = Modifier.weight(1f), color = MaterialTheme.colorScheme.onSurface)
                                Icon(if (expanded == index) Icons.Default.ExpandLess else Icons.Default.ExpandMore, contentDescription = null)
                            }
                            if (expanded == index) {
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(a, fontSize = 13.sp, color = Color.Gray)
                            }
                        }
                    }
                }
                OutlinedButton(
                    onClick = { },
                    modifier = Modifier.fillMaxWidth().height(52.dp),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = AccentColor)
                ) {
                    Icon(Icons.Default.Mail, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Contact Support", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
