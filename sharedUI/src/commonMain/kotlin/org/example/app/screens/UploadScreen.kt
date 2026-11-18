package org.example.app.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.launch
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import kotlinx.coroutines.delay
import org.example.app.core.platform.rememberVideoPicker

// 📋 تمام کیٹیگریز (تصویر 2 کے مطابق)
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
        var isUploading by remember { mutableStateOf(false) }
        var progress by remember { mutableStateOf(0f) }
        var isUploaded by remember { mutableStateOf(false) }

        var title by remember { mutableStateOf("") }
        var description by remember { mutableStateOf("") }
        var category by remember { mutableStateOf<String?>(null) }
        var categoryExpanded by remember { mutableStateOf(false) }

        val snackbarHostState = remember { SnackbarHostState() }
        val scope = rememberCoroutineScope()

        // 🎬 ویڈیو پکر — فائل منتخب ہوتے ہی اپلوڈنگ شروع
        val pickVideo = rememberVideoPicker { path ->
            videoPath = path
            isUploading = true
            progress = 0f
            isUploaded = false
        }

        // 📊 simulated اپلوڈنگ پروگریس (0 → 100)
        LaunchedEffect(isUploading) {
            if (isUploading) {
                while (progress < 1f) {
                    delay(40)
                    progress = (progress + 0.012f).coerceAtMost(1f)
                }
                isUploading = false
                isUploaded = true
            }
        }

        val canUpload = title.isNotBlank() && category != null && !isUploading

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
                // 1️⃣ اوپر والا حصہ: Select file / Uploading
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
                        // 🔹 ابتدائی حالت: پلی آئیکن + Select file
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                Icons.Default.PlayCircle,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.size(56.dp)
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            Button(
                                onClick = { pickVideo() },
                                shape = RoundedCornerShape(20.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = MaterialTheme.colorScheme.onSurface,
                                    contentColor = MaterialTheme.colorScheme.surface
                                )
                            ) {
                                Text("Select file", fontWeight = FontWeight.Bold)
                            }
                        }
                    } else {
                        // 🔹 اپلوڈنگ / مکمل حالت: "Uploading... X%" + بار
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(20.dp),
                            verticalArrangement = Arrangement.Bottom
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = if (isUploaded) "Uploaded!" else "Uploading...",
                                    color = Color.White,
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(modifier = Modifier.width(12.dp))
                                LinearProgressIndicator(
                                    progress = { progress },
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(8.dp)
                                        .clip(RoundedCornerShape(4.dp)),
                                    color = Color(0xFF3066E8),
                                    trackColor = Color.White.copy(alpha = 0.4f)
                                )
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "${(progress * 100).toInt()}%",
                                color = Color.White,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Medium,
                                modifier = Modifier.align(Alignment.CenterHorizontally)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // 2️⃣ Title فیلڈ + 0/100 کاؤنٹر
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
                Text(
                    text = "${title.length}/100",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 4.dp),
                    textAlign = androidx.compose.ui.text.style.TextAlign.End
                )

                Spacer(modifier = Modifier.height(12.dp))

                // 3️⃣ Description فیلڈ
                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    placeholder = { Text("Add description") },
                    label = { Text("Description") },
                    modifier = Modifier.fillMaxWidth().height(130.dp),
                    shape = RoundedCornerShape(12.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                // 4️⃣ Category ڈراپ ڈاؤن
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
    // 👇 شفاف پرت: کلکس یہ پکڑے گی، TextField نہیں
    Box(
        modifier = Modifier
            .matchParentSize()
            .clickable { categoryExpanded = true }
    )
    DropdownMenu(
        expanded = categoryExpanded,
        onDismissRequest = { categoryExpanded = false },
        modifier = Modifier.heightIn(max = 320.dp),
        containerColor = MaterialTheme.colorScheme.surface
    ) {
        videoCategories.forEach { cat ->
            DropdownMenuItem(
                text = { Text(cat, fontWeight = if (cat == category) FontWeight.Bold else FontWeight.Normal) },
                onClick = {
                    category = cat
                    categoryExpanded = false
                },
                leadingIcon = {
                    if (cat == category) {
                        Icon(Icons.Default.Check, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                    }
                }
            )
        }
    }
}

                Spacer(modifier = Modifier.height(24.dp))

                // 5️⃣ Upload بٹن
                Button(
                    onClick = {
                        scope.launch {
                            snackbarHostState.showSnackbar("Video uploaded successfully! 🎉")
                        }
                        navigator.pop()
                },
                enabled = canUpload,
                modifier = Modifier.fillMaxWidth().height(52.dp),
                shape = RoundedCornerShape(26.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF3066E8),
                    contentColor = Color.White
                )
           ) {
                Text("Upload", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                }
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}
