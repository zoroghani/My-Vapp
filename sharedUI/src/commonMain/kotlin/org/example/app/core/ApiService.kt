package org.example.app.core

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.HttpTimeout
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
    private const val STREAMTAPE_LOGIN = "04d3d9a57b69adbdb681" 
    private const val STREAMTAPE_KEY = "o9DarRp488uJykb"     

    val client by lazy {
        HttpClient {
            install(ContentNegotiation) { 
                json(Json { ignoreUnknownKeys = true; isLenient = true }) 
            }
            install(HttpTimeout) { 
                requestTimeoutMillis = 600000
                socketTimeoutMillis = 600000 
            }
            expectSuccess = false
        }
    }

    suspend fun uploadVideoToStreamtape(fileBytes: ByteArray, fileName: String): StreamtapeFinalResponse {
        // 1. اپلوڈ URL حاصل کریں
        val urlResponse = client.get("https://api.streamtape.com/file/ul?login=$STREAMTAPE_LOGIN&key=$STREAMTAPE_KEY")
            .body<StreamtapeUploadUrlResponse>()
            
        if (urlResponse.status != 200 || urlResponse.result?.url == null) {
            throw Exception("Upload URL Failed: ${urlResponse.msg}")
        }

        val uploadUrl = urlResponse.result.url
        val fileId = uploadUrl.substringAfterLast("/")

        // 2. فائل اپلوڈ کریں
        val uploadResp = client.post(uploadUrl) {
            setBody(MultiPartFormDataContent(formData {
                append("file1", fileBytes, Headers.build {
                    append(HttpHeaders.ContentType, "video/mp4")
                    append(HttpHeaders.ContentDisposition, "filename=\"$fileName\"")
                })
            }))
        }
        
        // ڈیبگنگ: اپلوڈ کا اصل جواب دیکھنے کے لیے
        println("🔥 UPLOAD RESPONSE BODY: ${uploadResp.bodyAsText()}")

        // 3. ویڈیو لنک حاصل کریں
        val infoResponse = client.get("https://api.streamtape.com/file/info?file=$fileId&login=$STREAMTAPE_LOGIN&key=$STREAMTAPE_KEY")
            .body<StreamtapeInfoResponse>()
            
        val videoInfo = infoResponse.result?.get(fileId)
        val watchUrl = videoInfo?.link ?: "https://streamtape.com/v/$fileId"

        // 4. تھمب نیل حاصل کریں
        val thumbResponse = client.get("https://api.streamtape.com/file/getsplash?file=$fileId&login=$STREAMTAPE_LOGIN&key=$STREAMTAPE_KEY")
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
        
        // سرور کا بالکل خام (Raw) جواب حاصل کریں
        val rawText = response.bodyAsText()
        println("🔥 RAW PHP RESPONSE: $rawText") 
        
        return try {
            // اگر جواب درست JSON ہے تو پارس کریں
            Json.decodeFromString<ApiResponse>(rawText)
        } catch (e: Exception) {
            // اگر JSON نہیں ہے (مثلاً HTML ایرر)، تو وہی خام ٹیکسٹ میسج میں واپس کر دیں تاکہ UI پر نظر آئے
            ApiResponse(status = "error", message = "Server returned invalid JSON: $rawText")
        }
    }
}
