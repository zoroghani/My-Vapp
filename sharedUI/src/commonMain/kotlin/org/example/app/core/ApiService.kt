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

// 👇 تھرڈ پارٹی ویڈیو اپلوڈ کا ریسپانس
@Serializable
data class VideoUploadResponse(
    val success: Boolean,
    val video_id: String? = null,
    val title: String? = null,
    val thumbnail: String? = null,
    val watch_url: String? = null,
    val embed_url: String? = null,
    val delete_url: String? = null,
    val upload_date: String? = null,
    val message: String? = null,
    val error: UploadError? = null
)

@Serializable
data class UploadError(
    val code: String? = null,
    val message: String? = null
)

// ==========================================
// 2. API سروس (HTTP Client)
// ==========================================

object ApiService {
    
    // 👇 یہاں اپنا PHP API کا URL لکھیں
    private const val BASE_URL = "http://192.168.1.10/your_project/api/" 

    val client = HttpClient {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                isLenient = true
            })
        }
        // ٹائم آؤٹ کو تھوڑا بڑھا دیا ہے تاکہ بڑی فائلیں اپلوڈ ہو سکیں
        expectSuccess = false 
    }

    // --- Auth APIs ---
    suspend fun login(email: String, password: String): ApiResponse {
        val response = client.post("${BASE_URL}login.php") {
            contentType(ContentType.Application.Json)
            setBody(LoginRequest(email, password))
        }
        return response.body()
    }

    suspend fun register(fullName: String, email: String, password: String): ApiResponse {
        val response = client.post("${BASE_URL}register.php") {
            contentType(ContentType.Application.Json)
            setBody(RegisterRequest(fullName, email, password))
        }
        return response.body()
    }

    // --- Video Upload APIs ---
    
    /**
     * 1. ویڈیو کو تھرڈ پارٹی API پر اپلوڈ کرتا ہے
     */
    suspend fun uploadVideoToExternalApi(fileBytes: ByteArray, fileName: String): VideoUploadResponse {
        val response = client.post("https://videostreamingapi.com/upload") {
            setBody(MultiPartFormDataContent(formData {
                append("file", fileBytes, Headers.build {
                    append(HttpHeaders.ContentType, "video/mp4")
                    append(HttpHeaders.ContentDisposition, "filename=\"$fileName\"")
                })
            }))
        }
        return response.body()
    }

    /**
     * 2. اپلوڈ کے بعد ویڈیو کا ڈیٹا ہمارے اپنے PHP ڈیٹا بیس میں محفوظ کرتا ہے
     */
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
        return response.body()
    }
}
