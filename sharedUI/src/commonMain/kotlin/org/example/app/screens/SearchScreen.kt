package org.example.app.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.devsrsouza.compose.icons.feather.Feather
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import coil3.compose.AsyncImage
import org.example.app.DummyVideo
import org.example.app.dummyVideos

class SearchScreen : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        var searchQuery by remember { mutableStateOf("") }
        
        // Search Logic: ٹائٹل یا چینل نام سے match کریں
        val filteredVideos = remember(searchQuery) {
            if (searchQuery.isBlank()) {
                emptyList()
            } else {
                dummyVideos.filter { video ->
                    video.title.contains(searchQuery, ignoreCase = true) ||
                    video.channel.contains(searchQuery, ignoreCase = true)
                }
            }
        }

        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        // سرچ بار
                        OutlinedTextField(
                            value = searchQuery,
                            onValueChange = { searchQuery = it },
                            placeholder = { Text("ویڈیوز سرچ کریں...", color = Color.Gray) },
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                            keyboardActions = KeyboardActions(
                                onSearch = { /* سرچ مکمل */ }
                            ),
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = Color(0xFF6200EE),
                                unfocusedBorderColor = Color.White.copy(alpha = 0.5f)
                            )
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = { navigator.pop() }) {
                            Icon(
                                imageVector = Feather.ArrowLeft,
                                contentDescription = "Back",
                                tint = Color(0xFF1F2937)
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color(0xFFE8EAF6).copy(alpha = 0.9f)
                    )
                )
            },
            containerColor = Color(0xFFE8EAF6)
        ) { padding ->
            if (searchQuery.isBlank()) {
                // جب کوئی سرچ نہ ہو
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            imageVector = Feather.Search,
                            contentDescription = null,
                            modifier = Modifier.size(64.dp),
                            tint = Color.Gray.copy(alpha = 0.5f)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "ویڈیوز تلاش کرنے کے لیے ٹائپ کریں",
                            color = Color.Gray,
                            fontSize = 16.sp
                        )
                    }
                }
            } else if (filteredVideos.isEmpty()) {
                // جب کوئی نتیجہ نہ ملے
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            imageVector = Feather.Frown,
                            contentDescription = null,
                            modifier = Modifier.size(64.dp),
                            tint = Color.Gray.copy(alpha = 0.5f)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "کوئی نتیجہ نہیں ملا",
                            color = Color.Gray,
                            fontSize = 16.sp
                        )
                    }
                }
            } else {
                // سرچ نتائج
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                        .padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(filteredVideos) { video ->
                        VideoCard(video = video) {
                            navigator.push(VideoPlayerScreen(video.videoUrl, video.title))
                        }
                    }
                }
            }
        }
    }
}
