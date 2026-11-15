package org.example.app.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ClipsScreen() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(Icons.Default.PlayCircle, contentDescription = null, modifier = Modifier.size(64.dp), tint = Color(0xFF6200EE))
            Spacer(modifier = Modifier.height(16.dp))
            Text("Clips / Reels Feed", fontSize = 20.sp, fontWeight = FontWeight.Bold)
            Text("Vertical scrolling short videos will appear here.", color = Color.Gray)
        }
    }
}

@Composable
fun InboxScreen() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(Icons.Default.Mail, contentDescription = null, modifier = Modifier.size(64.dp), tint = Color(0xFF6200EE))
            Spacer(modifier = Modifier.height(16.dp))
            Text("Inbox", fontSize = 20.sp, fontWeight = FontWeight.Bold)
            Text("No new messages.", color = Color.Gray)
        }
    }
}

@Composable
fun DownloadsScreen() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(Icons.Default.Download, contentDescription = null, modifier = Modifier.size(64.dp), tint = Color(0xFF6200EE))
            Spacer(modifier = Modifier.height(16.dp))
            Text("Downloads", fontSize = 20.sp, fontWeight = FontWeight.Bold)
            Text("Your offline videos will be saved here.", color = Color.Gray)
        }
    }
}

@Composable
fun SettingsScreen() {
    var name by remember { mutableStateOf("Andrew Ainsley") }
    var bio by remember { mutableStateOf("Video creator and tech enthusiast.") }
    var password by remember { mutableStateOf("") }

    Column(modifier = Modifier.fillMaxSize().padding(32.dp).verticalScroll(rememberScrollState())) {
        Text("Account Settings", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1A1A1A))
        Spacer(modifier = Modifier.height(24.dp))

        OutlinedTextField(
            value = name, onValueChange = { name = it },
            label = { Text("Full Name") },
            modifier = Modifier.fillMaxWidth(0.6f),
            singleLine = true
        )
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = bio, onValueChange = { bio = it },
            label = { Text("About / Bio") },
            modifier = Modifier.fillMaxWidth(0.6f).height(100.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = password, onValueChange = { password = it },
            label = { Text("New Password") },
            modifier = Modifier.fillMaxWidth(0.6f),
            singleLine = true,
            visualTransformation = PasswordVisualTransformation()
        )
        Spacer(modifier = Modifier.height(24.dp))

        Button(onClick = { /* Save logic */ }, shape = RoundedCornerShape(8.dp)) {
            Text("Save Changes")
        }
    }
}

@Composable
fun HelpCenterScreen() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(Icons.Default.Help, contentDescription = null, modifier = Modifier.size(64.dp), tint = Color(0xFF6200EE))
            Spacer(modifier = Modifier.height(16.dp))
            Text("Help Center", fontSize = 20.sp, fontWeight = FontWeight.Bold)
            Text("FAQs and support documentation.", color = Color.Gray)
        }
    }
}
