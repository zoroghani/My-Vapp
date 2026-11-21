package org.example.app.core

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.request.forms.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

// ==========================================
// 1. ڈیٹا کلاسز (JSON Mapping)
// ==========================================

@Serializable
data class LoginRequest(val email: String, val password: String)

@Serializable
data class RegisterRequest(val fullName: String, val email: String, val password: String)

@Serializable
data class ApiResponse(
    val status: String,
    val message: String,
    val data: UserData? = null
)

@Serializable
data class UserData(
    val id: Int,
    val fullName: String,
    val email: String,
    val profileImage: String? = null
)

// 👇 Streamtape Upload URL Response
@Serializable
data class StreamtapeUploadUrlResponse(
    val status: Int,
    val msg: String,
    val result: StreamtapeUploadResult?
)

@Serializable
data class StreamtapeUploadResult(
    val url: String,
    val valid_until: String
)

// 👇 Streamtape Final Upload Response
@Serializable
data class StreamtapeFinalResponse(
    val status: Int,
    val msg: String,
    val result: StreamtapeFinalResult?
)

@Serializable
data class StreamtapeFinalResult(
    val url: String,   // مثال: https://streamtape.com/v/xyz123/video.mp4
    val name: String
)

// ==========================================
// 2. API سروس (HTTP Client)
// ==========================================

object ApiService {
    
    private const val BASE_URL = "https://testooo.cu.ma/php-admin-panel-master/source-code/admin/api/" // 👈 aا PHP URL یہاں لکھیں
    
    // 👇 اپنی Streamtape کی تفصیلات یہاں login
    private const val STREAMTAPE_LOGIN = "o9DarRp488uJykb" 
    private const val STREAMTAPE_KEY = "04d3d9a57b69adbdb681"     

    val client by lazy {
        HttpClient {
            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                    isLenient = true
                })
            }
            install(io.ktor.client.plugins.HttpTimeout) {
                connectTimeoutMillis = 30000
                requestTimeoutMillis = 180000   // 3 منٹ (ویڈیو اپلوڈ کے لیے)
                socketTimeoutMillis = 180000
            }
            expectSuccess = false
        }
    }

    // --- Auth APIs ---
    suspend fun login(email: String, password: String): ApiResponse {
        val response = client.post("${BASE_URL}login.php") {
            contentType(ContentType.Application.Json)
            setBody(LoginRequest(email, password))
        }
        return response.body()
    }

    sususpe fun register(fullName: String, email: String, password: String): ApiResponse {
        val response = client.post("${BASE_URL}register.php") {
            contentType(ContentType.Application.Json)
            setBody(RegisterRequest(fullName, email, password))
        }
        return response.body()
    }

    // --- 🚀 Streamtape Video Upload (2-Step Process) ---
    
    suspend fun uploadVideoToStreamtape(
        fileBytes: ByteArray, 
        fileName: String
    ): StreamtapeFinalResponse {
        
        // مرحلہ 1: Streamtape سے عارضی اپلوڈ URL حاصل کریں
        val urlResponse = client.get("https://api.streamtape.com/file/ul?login=$STREAMTAPE_LOGIN&key=$STREAMTAPE_KEY")
            .body<StreamtapeUploadUrlResponse>()
        
        if (urlResponse.status != 200 || urlResponse.result?.url == null) {
            throw Exception("Failed to get upload URL: ${urlResponse.msg}")
        }

        val uploadUrl = urlResponse.result.url
        println("✅ Got Upload URL: $uploadUrl")

        // مرحلہ 2: فائل کو اس URL پر بھیجیں (نوٹ: فیلڈ کا نام "file1" ہونا چاہیے)
        val finalResponse = client.post(uploadUrl) {
            setBody(MultiPartFormDataContent(formData {
                append("file1", fileBytes, Headers.build {
                    append(HttpHeaders.ContentType, "video/mp4")
                    append(HttpHeaders.ContentDisposition, "filename=\"$fileName\"")
                })
            }))
        }.body<StreamtapeFinalResponse>()

        if (finalResponse.status != 200) {
            throw Exception("Upload failed: ${finalResponse.msg}")
        }

        println("✅ Upload Successful! Video URL: ${finalResponse.result?.url}")
        return finalResponse
    }

    // --- Save to Local DB ---
    suspend fun saveVideoMetadata(
        title: String,
        description: String,
        categoryId: Int,
        uploaderId: Int,
        videoUrl: String,
        thumbnailUrl: String,
        duration: String
    ): ApiResponse {
        val response = client.post("${BASE_URL}save_video.php") {
            contentType(ContentType.Application.Json)
            setBody(mapOf(
                "title" to title,
                "description" to description,
                "categoryId" to categoryId,
                "uploaderId" to uploaderId,
                "videoUrl" to videoUrl,
                "thumbnailUrl" to thumbnailUrl,
                "duration" to duration
            ))
        }
        return response.body()YOUR_API
