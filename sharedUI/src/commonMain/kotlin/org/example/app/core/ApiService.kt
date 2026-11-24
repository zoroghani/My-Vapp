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
data class StreamtapeUploadUrlResponse(val status: Int, val msg: String, val result: StreamtapeUploadResult?)

@Serializable
data class StreamtapeUploadResult(val url: String, val valid_until: String)

@Serializable
data class StreamtapeInfoResponse(val status: Int, val msg: String, val result: Map<String, StreamtapeFileInfo>?)

@Serializable
data class StreamtapeFileInfo(val id: String? = null, val name: String? = null, val link: String? = null)

@Serializable
data class StreamtapeThumbResponse(val status: Int, val msg: String, val result: String? = null)

@Serializable
data class StreamtapeFinalResponse(val status: Int, val msg: String, val result: StreamtapeFinalResult?)

@Serializable
data class StreamtapeFinalResult(val url: String, val name: String, val thumbnail: String = "")

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
            // 👇 Imunify360 کو bypass کرنے کے لیے Browser جیسا User-Agent
            defaultRequest {
                header(HttpHeaders.UserAgent, "Mozilla/5.0 (Linux; Android 13; SM-A515F) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Mobile Safari/537.36")
                header(HttpHeaders.Accept, "application/json")
            }
            expectSuccess = false
        }
    }

    // نیا فنکشن: ہمارے PHP سرور سے Upload URL مانگیں
    suspend fun getStreamtapeUploadUrl(): StreamtapeUploadUrlResponse {
        return client.post("${BASE_URL}get_upload_url.php").body()
    }

    suspend fun uploadVideoToStreamtape(fileBytes: ByteArray, fileName: String): StreamtapeFinalResponse {
        // 1. ہمارے سرور سے Upload URL حاصل کریں
        val urlResponse = getStreamtapeUploadUrl()
            
        if (urlResponse.status != 200 || urlResponse.result?.url == null) {
            throw Exception("Failed to get upload URL: ${urlResponse.msg}")
        }

        val uploadUrl = urlResponse.result.url
        val fileId = uploadUrl.substringAfterLast("/")

        // 2. ویڈیو براہِ راست Streamtape پر اپلوڈ کریں
        val uploadResp = client.post(uploadUrl) {
            setBody(MultiPartFormDataContent(formData {
                append("file1", fileBytes, Headers.build {
                    append(HttpHeaders.ContentType, "video/mp4")
                    append(HttpHeaders.ContentDisposition, "filename=\"$fileName\"")
                })
            }))
        }
        
        println("🔥 UPLOAD RESPONSE BODY: ${uploadResp.bodyAsText()}")

        // 3. ویڈیو کی معلومات اور تھمب نیل حاصل کریں (یہ Streamtape API سے براہِ راست آئیں گی)
        val infoResponse = client.get("https://api.streamtape.com/file/info?file=$fileId&login=y7bhafa3bxfxudzk&key=dq6hzjewe27bmwdn")
            .body<StreamtapeInfoResponse>()
            
        val videoInfo = infoResponse.result?.get(fileId)
        val watchUrl = videoInfo?.link ?: "https://streamtape.com/v/$fileId"

        val thumbResponse = client.get("https://api.streamtape.com/file/getsplash?file=$fileId&login=y7bhafa3bxfxudzk&key=dq6hzjewe27bmwdn")
            .body<StreamtapeThumbResponse>()

        return StreamtapeFinalResponse(
            status = 200,
            msg = "OK",
            result = StreamtapeFinalResult(
                url = watchUrl,
                name = videoInfo?.name ?: fileName,
                thumbnail = thumbResponse.result ?: ""
            )
        )
    }

    suspend fun saveVideoMetadata(
        title: String, description: String, categoryId: Int, uploaderId: Int, 
        videoUrl: String, thumbnailUrl: String, duration: String
    ): ApiResponse {
        val response = client.post("${BASE_URL}save_video.php") {
            contentType(ContentType.Application.Json)
            setBody(SaveVideoRequest(title, description, categoryId, uploaderId, videoUrl, thumbnailUrl, duration))
        }
        
        val rawText = response.bodyAsText()
        println("🔥 RAW PHP RESPONSE: $rawText") 
        
        return try {
            Json.decodeFromString<ApiResponse>(rawText)
        } catch (e: Exception) {
            ApiResponse(status = "error", message = "Server returned invalid JSON: $rawText")
        }
    }
}
