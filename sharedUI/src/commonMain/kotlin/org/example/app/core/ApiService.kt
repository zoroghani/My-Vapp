package org.example.app.core

import io.ktor.client.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

// 👇 ڈیٹا کلاسز (JSON کو Kotlin میں تبدیل کرنے کے لیے)
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

// 👇 API کال کرنے والا کلائنٹ
object ApiService {
    
    // 👇 یہاں اپنا اصل IP ایڈریس یا ڈومین لکھیں (جیسا کہ test_api.html میں تھا)
    // اگر آپ موبائل پر ٹیسٹ کر رہے ہیں تو localhost کی جگہ کمپیوٹر کا IP لکھیں
    private const val BASE_URL = "http://192.168.1.10/your_project/api/" 

    val client = HttpClient {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                isLenient = true
            })
        }
    }

    // لاگ ان فنکشن
    suspend fun login(email: String, password: String): ApiResponse {
        val response = client.post("${BASE_URL}login.php") {
            contentType(ContentType.Application.Json)
            setBody(LoginRequest(email, password))
        }
        return response.body()
    }

    // رجسٹریشن فنکشن
    suspend fun register(fullName: String, email: String, password: String): ApiResponse {
        val response = client.post("${BASE_URL}register.php") {
            contentType(ContentType.Application.Json)
            setBody(RegisterRequest(fullName, email, password))
        }
        return response.body()
    }
}
