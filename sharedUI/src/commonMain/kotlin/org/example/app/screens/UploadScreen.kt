package org.example.app.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import org.example.app.screens.VideoPlayerScreen
import androidx.compose.foundation.rememberScrollState
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.example.app.core.ApiService
import org.example.app.core.platform.rememberVideoPicker

private val videoCategories = listOf(
    "None", "Autos & Vehicles", "Comedy", "Education", "Entertainment",
    "Film & Animation", "Fashion & Beauty", "Gaming", "Music",
    "News & Politics", "Nonprofits & Activism", "People & Blogs",
    "Pets & Animals", "Science & Technology", "Sports", "Travel & Events"
)

class UploadScreen : Screen {

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow

        var videoPath by remember { mutableStateOf<String?>(null) }
        var videoBytes by remember { mutableStateOf<ByteArray?>(null) }
        var videoFileName by remember { mutableStateOf<String?>(null) }
        var isUploading by remember { mutableStateOf(false) }
        var progress by remember { mutableStateOf(0f) }
        var isUploaded by remember { mutableStateOf(false) }

        var title by remember { mutableStateOf("") }
        var description by remember { mutableStateOf("") }
        var category by remember { mutableStateOf<String?>(null) }
        var categoryExpanded by remember { mutableStateOf(false) }

        val snackbarHostState = remember { SnackbarHostState() }
        val scope = rememberCoroutineScope()

        val pickVideo = rememberVideoPicker { uri: String, bytes: ByteArray, name: String ->
            videoPath = uri
            videoBytes = bytes
            videoFileName = name
        }

        val canUpload = title.isNotBlank() && category != null && !isUploading && videoBytes != null

        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Upload Video", fontSize = 20.sp, fontWeight = FontWeight.Bold) },
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
            snackbarHost = { SnackbarHost(snackbarHostState) },
            containerColor = MaterialTheme.colorScheme.background
        ) { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp)
            ) {
                // 1️⃣ اوپر والا حصہ
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(
                            if (videoPath != null)
                                Brush.verticalGradient(listOf(Color(0xFF7FD8C4), Color(0xFF2E6E7E)))
                            else
                                Brush.verticalGradient(listOf(MaterialTheme.colorScheme.surfaceVariant, MaterialTheme.colorScheme.surfaceVariant))
                        )
                ) {
                    if (videoPath == null) {
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Icon(Icons.Default.PlayCircle, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.size(56.dp))
                            Spacer(modifier = Modifier.height(12.dp))
                            Button(
                                onClick = { pickVideo() },
                                shape = RoundedCornerShape(20.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.onSurface, contentColor = MaterialTheme.colorScheme.surface)
                            ) {
                                Text("Select file", fontWeight = FontWeight.Bold)
                            }
                        }
                    } else {
                        Column(
                            modifier = Modifier.fillMaxSize().padding(20.dp),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            if (isUploading || isUploaded) {
                                Text(text = if (isUploaded) "Uploaded!" else "Uploading...", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                                Spacer(modifier = Modifier.height(12.dp))
                                LinearProgressIndicator(
                                    progress = { progress },
                                    modifier = Modifier.fillMaxWidth().height(8.dp).clip(RoundedCornerShape(4.dp)),
                                    color = Color(0xFF3066E8),
                                    trackColor = Color.White.copy(alpha = 0.4f)
                                )
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(text = "${(progress * 100).toInt()}%", color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Medium)
                            } else {
                                Text(text = videoFileName ?: "Video Selected", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(text = "Ready to upload", color = Color.White.copy(alpha = 0.8f), fontSize = 14.sp)
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // 2️⃣ Title
                Text("Title", fontSize = 13.sp, fontWeight = FontWeight.Medium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Spacer(modifier = Modifier.height(6.dp))
                OutlinedTextField(
                    value = title,
                    onValueChange = { if (it.length <= 100) title = it },
                    placeholder = { Text("Create a title") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp)
                )
                Text(text = "${title.length}/100", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.fillMaxWidth().padding(top = 4.dp), textAlign = TextAlign.End)

                Spacer(modifier = Modifier.height(12.dp))

                // 3️⃣ Description
                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    placeholder = { Text("Add description") },
                    label = { Text("Description") },
                    modifier = Modifier.fillMaxWidth().height(130.dp),
                    shape = RoundedCornerShape(12.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                // 4️⃣ Category
                Box {
                    OutlinedTextField(
                        value = category ?: "",
                        onValueChange = { },
                        label = { Text("Category") },
                        placeholder = { Text("Select category") },
                        readOnly = true,
                        trailingIcon = { Icon(Icons.Default.ExpandMore, contentDescription = null) },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    )
                    Box(modifier = Modifier.matchParentSize().clickable { categoryExpanded = true })
                    DropdownMenu(
                        expanded = categoryExpanded,
                        onDismissRequest = { categoryExpanded = false },
                        modifier = Modifier.heightIn(max = 320.dp),
                        containerColor = MaterialTheme.colorScheme.surface
                    ) {
                        videoCategories.forEach { cat ->
                            DropdownMenuItem(
                                text = { Text(cat, fontWeight = if (cat == category) FontWeight.Bold else FontWeight.Normal) },
                                onClick = { category = cat; categoryExpanded = false },
                                leadingIcon = {
                                    if (cat == category) Icon(Icons.Default.Check, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // 5️⃣ Upload Button
                Button(
                    onClick = {
                        scope.launch {
                            isUploading = true
                            progress = 0f
                            
                            try {
                                withContext(Dispatchers.IO) {
                                    val bytes = videoBytes
                                    val name = videoFileName ?: "video.mp4"
                                    
                                    if (bytes == null || bytes.isEmpty()) {
                                        throw Exception("File bytes are empty")
                                    }

                                    val uploadResponse = ApiService.uploadVideoToStreamtape(bytes, name)
                                    
                                    if (uploadResponse.status == 200 && uploadResponse.result?.url != null) {
                                        val streamtapeUrl = uploadResponse.result.url!!
                                        
                                        val saveResponse = ApiService.saveVideoMetadata(
                                            title = title,
                                            description = description,
                                            categoryId = 1,
                                            uploaderId = 1,
                                            videoUrl = streamtapeUrl,
                                            thumbnailUrl = uploadResponse.result.thumbnail ?: "",
                                            duration = "00:00"
                                        )

                                        if (saveResponse.status == "success") {
                                            withContext(Dispatchers.Main) {
                                                isUploaded = true
                                                progress = 1f
                                                navigator.push(VideoPlayerScreen(streamtapeUrl))
                                            }
                                        } else {
                                            withContext(Dispatchers.Main) {
                                                snackbarHostState.showSnackbar("DB Save Failed: ${saveResponse.message}")
                                            }
                                        }
                                    }
                                }
                            } catch (e: Throwable) {
                                println("❌ CRASH PREVENTED! Error: ${e.message}")
                                e.printStackTrace()
                                withContext(Dispatchers.Main) {
                                    snackbarHostState.showSnackbar("Upload Error: ${e.message ?: e.toString()}")
                                }
                            } finally {
                                withContext(Dispatchers.Main) {
                                    isUploading = false
                                }
                            }
                        }
                    },
                    enabled = canUpload,
                    modifier = Modifier.fillMaxWidth().height(52.dp),
                    shape = RoundedCornerShape(26.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF3066E8), contentColor = Color.White)
                ) {
                    if (isUploading) return@onClick
                        CircularProgressIndicator(modifier = Modifier.size(24.dp), color = Color.White, strokeWidth = 3.dp)
                        Spacer(modifier = Modifier.width(8.dp))
                    }
                    Text(if (isUploading) "Uploading..." else "Upload", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                }
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}
