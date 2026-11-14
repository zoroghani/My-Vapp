package org.example.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import org.example.app.core.platform.SharedAppContext // 👈 یہ امپورٹ شامل کریں

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        
        // 👇 یہاں Context کو sharedUI میں محفوظ کر لیں
        SharedAppContext.androidContext = applicationContext
        
        setContent {
            App()
        }
    }
}
