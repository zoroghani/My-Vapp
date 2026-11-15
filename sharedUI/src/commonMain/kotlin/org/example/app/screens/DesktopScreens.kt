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
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(Icons.Default.Settings, contentDescription = null, modifier = Modifier.size(64.dp), tint = Color(0xFF6200EE))
            Spacer(modifier = Modifier.height(16.dp))
            Text("Settings", fontSize = 20.sp, fontWeight = FontWeight.Bold)
            Text("App preferences and configurations.", color = Color.Gray)
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
