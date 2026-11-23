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
import kotlinx.coroutines.delay
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

// ==========================================
// 1. ڈیٹا کلاسز (Data Classes)
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

// --- Streamtape Upload Classes ---
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

@Serializable
data class StreamtapeInfoResponse(
    val status: Int,
    val msg: String,
    val result: Map<String, StreamtapeFileInfo>? = null
)

@Serializable
data class StreamtapeFileInfo(
    val id: String? = null,
    val name: String? = null,
    val size: Long? = null,
    val type: String? = null,
    val converted: Boolean? = null,
    val status: Int? = null,
    val link: String? = null
)

@Serializable
data class StreamtapeThumbResponse(
    val status: Int,
    val msg: String,
    val result: String? = null
)

@Serializable
data class StreamtapeFinalResponse(
    val status: Int,
    val msg: String,
    val result: StreamtapeFinalResult?
)

@Serializable
data class StreamtapeFinalResult(
    val url: String,
    val name: String,
    val thumbnail: String = ""
)

// --- Streamtape Raw MP4 Download Classes ---
@Serializable
data class StreamtapeTicketResponse(
    val status: Int,
    val msg: String,
    val result: TicketResult?
)

@Serializable
data class TicketResult(
    val ticket: String,
    val wait_time: Int,
    val valid_until: String
)

@Serializable
data class StreamtapeDlResponse(
    val status: Int,
    val msg: String,
    val result: DlResult?
)

@Serializable
data class DlResult(
    val name: String,
    val size: Long,
    val url: String // 👈 یہی وہ Raw MP4 لنک ہے جس میں کوئی اشتہار نہیں
)

// --- Save Video Request (Serialization Crash کا مستقل حل) ---
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


// ==========================================
// 2. API سروس (API Service)
// ==========================================

object ApiService {

    // ⚠️ انتہائی اہم: یہاں اپنا اصل PHP API URL لکھیں
    private const val BASE_URL = "https://testooo.cu.ma/admin/api/" 
    
    // ⚠️ انتہائی اہم: یہاں اپنی Streamtape کی اصل Login اور Key لکھیں
    private const val STREAMTAPE_LOGIN = "04d3d9a57b69adbdb681"
    private const val STREAMTAPE_KEY = "o9DarRp488uJykb"

    val client by lazy {
        HttpClient {
            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                    isLenient = true
                })
            }
            install(HttpTimeout) {
                connectTimeoutMillis = 60000
                requestTimeoutMillis = 600000   // 👈 10 منٹ (بڑی فائلز کے لیے)
                socketTimeoutMillis = 600000
            }
            expectSuccess = false
        }
    }

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

    suspend fun uploadVideoToStreamtape(
        fileBytes: ByteArray,
        fileName: String
    ): StreamtapeFinalResponse {

        // مرحلہ 1: اپلوڈ URL حاصل کریں
        val urlResponse = client.get("https://api.streamtape.com/file/ul?login=$STREAMTAPE_LOGIN&key=$STREAMTAPE_KEY")
            .body<StreamtapeUploadUrlResponse>()

        if (urlResponse.status != 200 || urlResponse.result?.url == null) {
            throw Exception("Failed to get upload URL: ${urlResponse.msg}")
        }

        val uploadUrl = urlResponse.result.url
        val fileId = uploadUrl.substringAfterLast("/")
        println("✅ Got Upload URL. File ID = $fileId")

        // مرحلہ 2: فائل اپلوڈ کریں
        val uploadResponse = client.post(uploadUrl) {
            setBody(MultiPartFormDataContent(formData {
                append("file1", fileBytes, Headers.build {
                    append(HttpHeaders.ContentType, "video/mp4")
                    append(HttpHeaders.ContentDisposition, "filename=\"$fileName\"")
                })
            }))
        }
        println("✅ Upload done. Status = ${uploadResponse.status.value}")

        // مرحلہ 3: file/info سے لنک حاصل کریں
        val infoResponse = client.get("https://api.streamtape.com/file/info?file=$fileId&login=$STREAMTAPE_LOGIN&key=$STREAMTAPE_KEY")
            .body<StreamtapeInfoResponse>()

        val videoInfo = infoResponse.result?.get(fileId)
        val watchUrl = videoInfo?.link ?: "https://streamtape.com/v/$fileId"
        println("✅ Watch URL = $watchUrl")

        // مرحلہ 4: تھمب نیل حاصل کریں
        val thumbResponse = client.get("https://api.streamtape.com/file/getsplash?file=$fileId&login=$STREAMTAPE_LOGIN&key=$STREAMTAPE_KEY")
            .body<StreamtapeThumbResponse>()
        val thumbnailUrl = thumbResponse.result ?: ""
        println("✅ Thumbnail = $thumbnailUrl")

        return StreamtapeFinalResponse(
            status = 200,
            msg = "OK",
            result = StreamtapeFinalResult(
                url = watchUrl,
                name = videoInfo?.name ?: fileName,
                thumbnail = thumbnailUrl
            )
        )
    }

    // 👇 یہ فنکشن File ID لے کر اشتہارات سے پاک Raw MP4 لنک واپس کرے گا
            suspend fun getRawVideoUrl(fileId: String): String {
        // 👇 مرحلہ 1: ویڈیو کی conversion مکمل ہونے کا انتظار (ہر 5 سیکنڈ بعد چیک، زیادہ سے زیادہ 60 سیکنڈ)
        var converted = false
        var waited = 0
        while (!converted && waited < 60) {
            val infoResponse = client.get("https://api.streamtape.com/file/info?file=$fileId&login=$STREAMTAPE_LOGIN&key=$STREAMTAPE_KEY")
                .body<StreamtapeInfoResponse>()
            
            val info = infoResponse.result?.get(fileId)
            converted = (info?.converted == true)
            
            if (!converted) {
                println("⏳ Video not converted yet. Waiting 5s... ($waited/60s)")
                kotlinx.coroutines.delay(5000)
                waited += 5
            } else {
                println("✅ Video converted! Proceeding to get raw URL.")
            }
        }

        // 👇 مرحلہ 2: dlticket مانگیں اور wait_time کا احترام کریں
        var rawUrl: String? = null
        var attempts = 0
        val maxAttempts = 5

        while (rawUrl == null && attempts < maxAttempts) {
            attempts++
            println("🔄 Attempt $attempts to get raw URL...")

            val ticketResponse = client.get("https://api.streamtape.com/file/dlticket?file=$fileId&login=$STREAMTAPE_LOGIN&key=$STREAMTAPE_KEY")
                .body<StreamtapeTicketResponse>()

            if (ticketResponse.status == 200 && ticketResponse.result?.ticket != null) {
                val waitTime = ticketResponse.result.wait_time  // 👈 سرور کا بتایا ہوا وقت
                println("⏳ Server says wait $waitTime seconds.")
                kotlinx.coroutines.delay((waitTime + 2) * 1000L)  // +2 سیکنڈ محفوظ فاصلہ

                val dlResponse = client.get("https://api.streamtape.com/file/dl?file=$fileId&ticket=${ticketResponse.result.ticket}")
                    .body<StreamtapeDlResponse>()

                if (dlResponse.status == 200 && dlResponse.result?.url != null) {
                    rawUrl = dlResponse.result.url
                    println("✅ Raw MP4 URL obtained on attempt $attempts")
                } else {
                    println("⚠️ dl failed: ${dlResponse.msg}")
                    kotlinx.coroutines.delay(5000)
                }
            } else {
                println("⚠️ ticket failed: ${ticketResponse.msg}")
                kotlinx.coroutines.delay(5000)
            }
        }

        return rawUrl ?: throw Exception("Failed to get raw video URL after $maxAttempts attempts")
    }
    // 👇 ڈیٹا بیس میں ویڈیو محفوظ کرنے کا فنکشن (Serialization Crash فری)
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
            setBody(SaveVideoRequest(
                title = title,
                description = description,
                categoryId = categoryId,
                uploaderId = uploaderId,
                videoUrl = videoUrl,
                thumbnailUrl = thumbnailUrl,
                duration = duration
            ))
        }
        return response.body()
    }
}
