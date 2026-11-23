package org.example.app.core

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.request.*
import io.ktor.client.request.forms.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

// --- ڈیٹا کلاسز (صرف اتنی ہی درکار ہیں) ---
@Serializable data class ApiResponse(val status: String, val message: String, val data: Any? = null)
@Serializable data class StreamtapeUploadUrlResponse(val status: Int, val msg: String, val result: StreamtapeUploadResult?)
@Serializable data class StreamtapeUploadResult(val url: String, val valid_until: String)
@Serializable data class StreamtapeInfoResponse(val status: Int, val msg: String, val result: Map<String, StreamtapeFileInfo>?)
@Serializable data class StreamtapeFileInfo(val id: String? = null, val name: String? = null, val link: String? = null)
@Serializable data class StreamtapeThumbResponse(val status: Int, val msg: String, val result: String? = null)
@Serializable data class StreamtapeFinalResponse(val status: Int, val msg: String, val result: StreamtapeFinalResult?)
@Serializable data class StreamtapeFinalResult(val url: String, val name: String, val thumbnail: String = "")
@Serializable data class SaveVideoRequest(val title: String, val description: String, val categoryId: Int, val uploaderId: Int, val videoUrl: String, val thumbnailUrl: String, val duration: String)

object ApiService {
    private const val BASE_URL = "http://testooo.cu.ma/php-admin-panel-master/source-code/admin/api/" 
    private const val STREAMTAPE_LOGIN = "YOUR_API_LOGIN_HERE" // اپنی Login یہاں ڈالیں
    private const val STREAMTAPE_KEY = "YOUR_API_KEY_HERE"     // اپنی Key یہاں ڈالیں

    val client by lazy {
        HttpClient {
            install(ContentNegotiation) { json(Json { ignoreUnknownKeys = true; isLenient = true }) }
            install(HttpTimeout) { requestTimeoutMillis = 600000; socketTimeoutMillis = 600000 }
            expectSuccess = false
        }
    }

    // 1. ویڈیو اپلوڈ کریں اور /v/ والا لنک + تھمب نیل حاصل کریں
    suspend fun uploadVideoToStreamtape(fileBytes: ByteArray, fileName: String): StreamtapeFinalResponse {
        val urlResponse = client.get("https://api.streamtape.com/file/ul?login=$STREAMTAPE_LOGIN&key=$STREAMTAPE_KEY").body<StreamtapeUploadUrlResponse>()
        if (urlResponse.status != 200 || urlResponse.result?.url == null) throw Exception("Failed to get upload URL")

        val uploadUrl = urlResponse.result.url
        val fileId = uploadUrl.substringAfterLast("/")

        client.post(uploadUrl) {
            setBody(MultiPartFormDataContent(formData {
                append("file1", fileBytes, Headers.build {
                    append(HttpHeaders.ContentType, "video/mp4")
                    append(HttpHeaders.ContentDisposition, "filename=\"$fileName\"")
                })
            }))
        }

        val infoResponse = client.get("https://api.streamtape.com/file/info?file=$fileId&login=$STREAMTAPE_LOGIN&key=$STREAMTAPE_KEY").body<StreamtapeInfoResponse>()
        val videoInfo = infoResponse.result?.get(fileId)
        val watchUrl = videoInfo?.link ?: "https://streamtape.com/v/$fileId"

        val thumbResponse = client.get("https://api.streamtape.com/file/getsplash?file=$fileId&login=$STREAMTAPE_LOGIN&key=$STREAMTAPE_KEY").body<StreamtapeThumbResponse>()
        
        return StreamtapeFinalResponse(200, "OK", StreamtapeFinalResult(watchUrl, videoInfo?.name ?: fileName, thumbResponse.result ?: ""))
    }

    // 2. ڈیٹا بیس میں سیو کریں (یہی وہ فنکشن ہے جو پہلے خراب ہو رہا تھا، اب یہ صاف ہے)
    suspend fun saveVideoMetadata(title: String, description: String, categoryId: Int, uploaderId: Int, videoUrl: String, thumbnailUrl: String, duration: String): ApiResponse {
        return client.post("${BASE_URL}save_video.php") {
            contentType(ContentType.Application.Json)
            setBody(SaveVideoRequest(title, description, categoryId, uploaderId, videoUrl, thumbnailUrl, duration))
        }.body()
    }
}
