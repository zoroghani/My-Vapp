package org.example.app.screens

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import coil3.compose.AsyncImage
import org.example.app.core.platform.rememberVideoPicker

class UploadScreen : Screen {
    
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        
        // State variables
        var videoPath by remember { mutableStateOf<String?>(null) }
        var videoThumbnail by remember { mutableStateOf<String?>(null) }
        var title by remember { mutableStateOf("") }
        var description by remember { mutableStateOf("") }
        var isUploading by remember { mutableStateOf(false) }
        var uploadProgress by remember { mutableStateOf(0f) }
        
        // Native Video Picker
        val launchVideoPicker = rememberVideoPicker { path ->
            videoPath = path
            // Thumbnail کے لیے ایک placeholder (آپ بعد میں MediaMetadataRetriever سے اصل thumbnail لے سکتے ہیں)
            videoThumbnail = "https://picsum.photos/seed/upload/${System.currentTimeMillis()}/640/360"
        }
        
        // Progress animation
        val progressAnim = remember { Animatable(0f) }
        LaunchedEffect(uploadProgress) {
            progressAnim.animateTo(uploadProgress, tween(500))
        }

        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            text = "Upload",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = { navigator.pop() }) {
                            Icon(
                                Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Back",
                                tint = Color.White
                            )
                        }
                    },
                    actions = {
                        IconButton(onClick = { /* More options */ }) {
                            Icon(
                                Icons.Default.MoreVert,
                                contentDescription = "More",
                                tint = Color.White
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color.Transparent
                    )
                )
            },
            // گلاس UI کے لیے پس منظر: purple gradient
            containerColor = Color.Transparent
        ) { padding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color(0xFF6A1B9A),
                                Color(0xFF4A148C),
                                Color(0xFF311B92)
                            )
                        )
                    )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                ) {
                    // 1. ویڈیو پریویو (اگر ویڈیو منتخب ہو)
                    if (videoThumbnail != null) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(280.dp)
                        ) {
                            AsyncImage(
                                model = videoThumbnail,
                                contentDescription = "Video Preview",
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                            
                            // Purple overlay
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(Color(0xFF6A1B9A).copy(alpha = 0.6f))
                            )
                            
                            // Uploading text
                            if (isUploading) {
                                Column(
                                    modifier = Modifier.align(Alignment.Center),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(
                                        text = "UPLOADING...",
                                        color = Color.White,
                                        fontSize = 20.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(
                                        text = "${(progressAnim.value * 100).toInt()}% completed",
                                        color = Color.White.copy(alpha = 0.9f),
                                        fontSize = 14.sp
                                    )
                                }
                            }
                        }
                    } else {
                        // Placeholder جب کوئی ویڈیو منتخب نہ ہو
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(280.dp)
                                .background(Color(0xFF6A1B9A).copy(alpha = 0.4f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Icon(
                                    Icons.Default.VideoLibrary,
                                    contentDescription = null,
                                    modifier = Modifier.size(64.dp),
                                    tint = Color.White.copy(alpha = 0.7f)
                                )
                                Spacer(modifier = Modifier.height(12.dp))
                                Text(
                                    text = "ویڈیو منتخب کریں",
                                    color = Color.White,
                                    fontSize = 16.sp
                                )
                            }
                        }
                    }
                    
                    // 2. گلاس فارم کارڈ
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color.White.copy(alpha = 0.95f)
                        ),
                        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(20.dp),
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            // Title field
                            OutlinedTextField(
                                value = title,
                                onValueChange = { title = it },
                                label = { Text("Title") },
                                placeholder = { Text("My first video") },
                                modifier = Modifier.fillMaxWidth(),
                                singleLine = true,
                                trailingIcon = {
                                    if (title.isNotEmpty()) {
                                        IconButton(onClick = { title = "" }) {
                                            Icon(
                                                Icons.Default.Delete,
                                                contentDescription = "Clear",
                                                tint = Color(0xFF6A1B9A)
                                            )
                                        }
                                    }
                                },
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = Color(0xFF6A1B9A),
                                    focusedLabelColor = Color(0xFF6A1B9A)
                                )
                            )
                            
                            // Description field
                            OutlinedTextField(
                                value = description,
                                onValueChange = { description = it },
                                label = { Text("Description") },
                                placeholder = { Text("Tell viewers about your video...") },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(100.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = Color(0xFF6A1B9A),
                                    focusedLabelColor = Color(0xFF6A1B9A)
                                )
                            )
                            
                            // Progress bar (اگر اپ لوڈ ہو رہا ہو)
                            if (isUploading) {
                                Column {
                                    LinearProgressIndicator(
                                        progress = { progressAnim.value },
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(8.dp)
                                            .clip(RoundedCornerShape(4.dp)),
                                        color = Color(0xFF6A1B9A),
                                        trackColor = Color(0xFF6A1B9A).copy(alpha = 0.2f)
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = "${(progressAnim.value * 100).toInt()}% uploaded",
                                        fontSize = 12.sp,
                                        color = Color(0xFF6A1B9A)
                                    )
                                }
                            }
                            
                            // Upload button
                            Button(
                                onClick = {
                                    if (videoPath != null && title.isNotBlank()) {
                                        isUploading = true
                                        // Simulate upload progress
                                        launchUploadSimulation(
                                            onProgress = { progress ->
                                                uploadProgress = progress
                                            },
                                            onComplete = {
                                                isUploading = false
                                                // کامیابی کے بعد پلیر پر جائیں
                                                navigator.push(VideoPlayerScreen(videoPath!!, title))
                                            }
                                        )
                                    }
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(56.dp),
                                enabled = videoPath != null && title.isNotBlank() && !isUploading,
                                shape = RoundedCornerShape(16.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(0xFF6A1B9A),
                                    disabledContainerColor = Color.Gray.copy(alpha = 0.5f)
                                )
                            ) {
                                Row(
                                    horizontalArrangement = Arrangement.Center,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = if (isUploading) "UPLOADING..." else "UPLOAD",
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Icon(
                                        Icons.Default.Upload,
                                        contentDescription = null,
                                        tint = Color.White
                                    )
                                }
                            }
                        }
                    }
                }
                
                // 3. Floating "Select Video" button (اگر ویڈیو منتخب نہ ہو)
                if (videoPath == null && !isUploading) {
                    FloatingActionButton(
                        onClick = { launchVideoPicker() },
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .padding(24.dp),
                        containerColor = Color(0xFF6A1B9A),
                        shape = CircleShape
                    ) {
                        Icon(
                            Icons.Default.Add,
                            contentDescription = "Select Video",
                            tint = Color.White,
                            modifier = Modifier.size(28.dp)
                        )
                    }
                }
            }
        }
    }
    
    // Upload simulation (بعد میں اصل API call سے بدلیں)
    private fun launchUploadSimulation(
        onProgress: (Float) -> Unit,
        onComplete: () -> Unit
    ) {
        // یہ صرف simulation ہے - اصل میں یہاں API call ہوگی
        kotlinx.coroutines.GlobalScope.launch(kotlinx.coroutines.Dispatchers.Main) {
            for (i in 1..100) {
                kotlinx.coroutines.delay(50) // 50ms * 100 = 5 seconds total
                onProgress(i / 100f)
            }
            onComplete()
        }
    }
}
