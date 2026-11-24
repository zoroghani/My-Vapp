package org.example.app.core

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.*
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
data class ApiResponse(
    val status: String = "error",
    val message: String = "",
    val data: JsonElement? = null
)

@Serializable
data class StreamtapeUploadUrlResponse(
    val status: Int = 0,
    val msg: String = "",
    val result: StreamtapeUploadResult? = null
)

@Serializable
data class StreamtapeUploadResult(
    val url: String? = null,
    val valid_until: String? = null
)

@Serializable
data class StreamtapeInfoResponse(
    val status: Int = 0,
    val msg: String = "",
    val result: Map<String, StreamtapeFileInfo>? = null
)

@Serializable
data class StreamtapeFileInfo(
    val id: String? = null,
    val name: String? = null,
    val link: String? = null
)

@Serializable
data class StreamtapeThumbResponse(
    val status: Int = 0,
    val msg: String = "",
    val result: String? = null
)

@Serializable
data class StreamtapeFinalResponse(
    val status: Int = 200,
    val msg: String = "OK",
    val result: StreamtapeFinalResult? = null
)

@Serializable
data class StreamtapeFinalResult(
    val url: String = "",
    val name: String = "",
    val thumbnail: String = ""
)

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

    private val jsonConfig = Json {
        ignoreUnknownKeys = true
        isLenient = true
        coerceInputValues = true
    }

    val client by lazy {
        HttpClient {
            install(ContentNegotiation) {
                json(jsonConfig)
            }
            install(HttpTimeout) {
                requestTimeoutMillis = 600000
                socketTimeoutMillis = 600000
            }
            defaultRequest {
                header(
                    HttpHeaders.UserAgent,
                    "Mozilla/5.0 (Linux; Android 13; SM-A515F) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Mobile Safari/537.36"
                )
                header(HttpHeaders.Accept, "application/json, text/plain, */*")
            }
            expectSuccess = false
        }
    }

    // سرور سے Upload URL حاصل کریں + HTTP اسٹیٹس اور Raw رسپانس چیک کریں
    suspend fun getStreamtapeUploadUrl(): StreamtapeUploadUrlResponse {
        val endpoint = "${BASE_URL}get_upload_url.php"
        val response = client.post(endpoint)
        val statusCode = response.status.value
        val rawText = response.bodyAsText()

        println("🔥 [getStreamtapeUploadUrl] URL: $endpoint")
        println("🔥 [getStreamtapeUploadUrl] HTTP Status: $statusCode")
        println("🔥 [getStreamtapeUploadUrl] Raw Body: $rawText")

        if (rawText.isBlank()) {
            return StreamtapeUploadUrlResponse(
                status = statusCode,
                msg = "Empty response body (HTTP $statusCode)",
                result = null
            )
        }

        return try {
            val parsed = jsonConfig.decodeFromString<StreamtapeUploadUrlResponse>(rawText)
            if (statusCode !in 200..299 && parsed.status == 0) {
                parsed.copy(status = statusCode)
            } else {
                parsed
            }
        } catch (e: Exception) {
            println("❌ [getStreamtapeUploadUrl] JSON Parse Error: ${e.message}")
            StreamtapeUploadUrlResponse(
                status = statusCode,
                msg = "HTTP $statusCode | Parse error: ${e.message} | Raw: ${rawText.take(300)}",
                result = null
            )
        }
    }

    suspend fun uploadVideoToStreamtape(fileBytes: ByteArray, fileName: String): StreamtapeFinalResponse {
        // 1. ہمارے سرور سے Upload URL حاصل کریں
        val urlResponse = getStreamtapeUploadUrl()

        if (urlResponse.status != 200 || urlResponse.result?.url.isNullOrBlank()) {
            throw Exception("Failed to get upload URL (status=${urlResponse.status}): ${urlResponse.msg}")
        }

        val uploadUrl = urlResponse.result!!.url!!
        val fileId = uploadUrl.substringAfterLast("/")

        println("🔥 [uploadVideo] Upload URL: $uploadUrl | fileId: $fileId")

        // 2. ویڈیو براہِ راست Streamtape پر اپلوڈ کریں
        val uploadResp = client.post(uploadUrl) {
            setBody(MultiPartFormDataContent(formData {
                append("file1", fileBytes, Headers.build {
                    append(HttpHeaders.ContentType, "video/mp4")
                    append(HttpHeaders.ContentDisposition, "filename=\"$fileName\"")
                })
            }))
        }

        val uploadRaw = uploadResp.bodyAsText()
        println("🔥 [uploadVideo] HTTP Status: ${uploadResp.status.value}")
        println("🔥 [uploadVideo] Raw Body: $uploadRaw")

        if (uploadResp.status.value !in 200..299) {
            throw Exception("Streamtape upload failed (HTTP ${uploadResp.status.value}): ${uploadRaw.take(250)}")
        }

        // 3. ویڈیو کی معلومات اور تھمب نیل حاصل کریں
        val infoRaw = client
            .get("https://api.streamtape.com/file/info?file=$fileId&login=y7bhafa3bxfxudzk&key=dq6hzjewe27bmwdn")
            .bodyAsText()
        println("🔥 [fileInfo] Raw Body: $infoRaw")

        val infoResponse = try {
            jsonConfig.decodeFromString<StreamtapeInfoResponse>(infoRaw)
        } catch (_: Exception) {
            StreamtapeInfoResponse(status = 0, msg = infoRaw, result = null)
        }

        val videoInfo = infoResponse.result?.get(fileId)
        val watchUrl = videoInfo?.link?.takeIf { it.isNotBlank() } ?: "https://streamtape.com/v/$fileId"

        val thumbRaw = client
            .get("https://api.streamtape.com/file/getsplash?file=$fileId&login=y7bhafa3bxfxudzk&key=dq6hzjewe27bmwdn")
            .bodyAsText()
        println("🔥 [getSplash] Raw Body: $thumbRaw")

        val thumbResponse = try {
            jsonConfig.decodeFromString<StreamtapeThumbResponse>(thumbRaw)
        } catch (_: Exception) {
            StreamtapeThumbResponse(status = 0, msg = thumbRaw, result = null)
        }

        return StreamtapeFinalResponse(
            status = 200,
            msg = "OK",
            result = StreamtapeFinalResult(
                url = watchUrl,
                name = videoInfo?.name?.takeIf { it.isNotBlank() } ?: fileName,
                thumbnail = thumbResponse.result ?: ""
            )
        )
    }

    suspend fun saveVideoMetadata(
        title: String,
        description: String,
        categoryId: Int,
        uploaderId: Int,
        videoUrl: String,
        thumbnailUrl: String,
        duration: String
    ): ApiResponse {
        val response = client.get("${BASE_URL}save_video.php") {
            parameter("title", title)
            parameter("description", description)
            parameter("categoryId", categoryId)
            parameter("uploaderId", uploaderId)
            parameter("videoUrl", videoUrl)
            parameter("thumbnailUrl", thumbnailUrl)
            parameter("duration", duration)
        }
        val statusCode = response.status.value
        val rawText = response.bodyAsText()
        println("🔥 [saveVideoMetadata] HTTP Status: $statusCode")
        println("🔥 [saveVideoMetadata] Raw Body: $rawText")

        return try {
            jsonConfig.decodeFromString<ApiResponse>(rawText)
        } catch (e: Exception) {
            ApiResponse(
                status = "error",
                message = "HTTP $statusCode | Server Error: ${rawText.take(300)}"
            )
        }
    }
}
