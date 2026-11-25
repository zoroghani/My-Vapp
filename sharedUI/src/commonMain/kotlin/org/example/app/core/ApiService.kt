package org.example.app.core

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.*
import io.ktor.client.request.forms.*
import io.ktor.client.statement.bodyAsText
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement

// --- ڈیٹا کلاسز ---
@Serializable
data class ApiResponse(val status: String, val message: String, val data: JsonElement? = null)

@Serializable
data class FtpUploadResponse(val status: String, val message: String, val videoUrl: String = "", val thumbnailUrl: String = "")

@Serializable
data class SaveVideoRequest(
    val title: String,
    val description: String,
    val categoryId: Int,
    val uploaderId: Int,
    val videoUrl: String,
    val thumbnailUrl: String,
    val duration: String
)

object ApiService {
    private const val BASE_URL = "https://testooo.cu.ma/admin/api/" 

    val client by lazy {
        HttpClient {
            install(ContentNegotiation) { 
                json(Json { ignoreUnknownKeys = true; isLenient = true }) 
            }
            install(HttpTimeout) { 
                requestTimeoutMillis = 600000
                socketTimeoutMillis = 600000 
            }
            defaultRequest {
                header(HttpHeaders.UserAgent, "Mozilla/5.0 (Linux; Android 13; SM-A515F) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Mobile Safari/537.36")
            }
            expectSuccess = false
        }
    }

    suspend fun uploadVideoViaPhpFtp(fileBytes: ByteArray, fileName: String): FtpUploadResponse {
        val response = client.post("${BASE_URL}upload_video.php") {
            setBody(MultiPartFormDataContent(formData {
                append("video_file", fileBytes, Headers.build {
                    append(HttpHeaders.ContentType, "video/mp4")
                    append(HttpHeaders.ContentDisposition, "filename=\"$fileName\"")
                })
            }))
        }
        
        val rawText = response.bodyAsText()
        println("🔥 PHP FTP RESPONSE: $rawText")
        
        return try {
            Json.decodeFromString<FtpUploadResponse>(rawText)
        } catch (e: Exception) {
            throw Exception("Invalid response from PHP: $rawText")
        }
    }

    suspend fun saveVideoMetadata(
        title: String, description: String, categoryId: Int, uploaderId: Int,
        videoUrl: String, thumbnailUrl: String, duration: String
    ): ApiResponse {
        val response = client.get("${BASE_URL}save_video.php") {
            parameter("title", title)
            parameter("description", description)
            parameter("categoryId", categoryId)     // ✅ درست syntax
            parameter("uploaderId", uploaderId)     // ✅ درست syntax
            parameter("videoUrl", videoUrl)
            parameter("thumbnailUrl", thumbnailUrl)
            parameter("duration", duration)
        }
        val rawText = response.bodyAsText()
        println("🔥 SAVE_DB_RAW: $rawText")
        
        return try {
            Json.decodeFromString<ApiResponse>(rawText) // ✅ stray dot ہٹا دیا گیا
        } catch (e: Exception) {
            ApiResponse(status = "error", message = "Server Error: $rawText")
        }
    }
}
