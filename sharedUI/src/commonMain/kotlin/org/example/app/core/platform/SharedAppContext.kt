package org.example.app.core.platform

// یہ object sharedUI کے اندر ہے، اس لیے androidApp اسے استعمال کر سکتا ہے
object SharedAppContext {
    var androidContext: Any? = null
}
