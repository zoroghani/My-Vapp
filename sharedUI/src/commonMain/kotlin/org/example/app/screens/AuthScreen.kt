package org.example.app.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import org.example.app.core.AuthState

class AuthScreen : Screen {

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow

        var isLoginMode by remember { mutableStateOf(true) }
        var fullName by remember { mutableStateOf("") }
        var email by remember { mutableStateOf("") }
        var password by remember { mutableStateOf("") }
        var passwordVisible by remember { mutableStateOf(false) }
        var agreeTerms by remember { mutableStateOf(false) }

        // ✅ ای میل درست ہے یا نہیں چیک کرنا
        val isEmailValid = remember(email) {
            email.matches(Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"))
        }
        
        // ✅ بٹن تبھی چلے گا جب ڈیٹا درست ہو
        val canSubmit = if (isLoginMode) {
            isEmailValid && password.length >= 6
        } else {
            fullName.isNotBlank() && isEmailValid && password.length >= 6 && agreeTerms
        }

        // 📱 ڈیسک ٹاپ اور موبائل کا سائز خودکار چیک کرنا
        BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
            val isDesktop = maxWidth >= 840.dp

            Scaffold(
                containerColor = MaterialTheme.colorScheme.background
            ) { padding ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        modifier = Modifier
                            .then(
                                if (isDesktop) {
                                    // ڈیسک ٹاپ کے لیے: درمیان میں کارڈ
                                    Modifier.width(440.dp)
                                        .clip(RoundedCornerShape(24.dp))
                                        .background(MaterialTheme.colorScheme.surface)
                                } else {
                                    // موبائل کے لیے: پوری سکرین
                                    Modifier.fillMaxSize()
                                }
                            )
                            .verticalScroll(rememberScrollState())
                            .padding(24.dp)
                    ) {
                        // 🖥️ ڈیسک ٹاپ پر اوپر ایپ کا لوگو
                        if (isDesktop) {
                            Box(
                                modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Box(
                                    modifier = Modifier.size(64.dp).clip(RoundedCornerShape(16.dp))
                                        .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.12f)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(Icons.Default.PlayCircle, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(32.dp))
                                }
                            }
                        }

                        // ہیڈر ٹیکسٹ
                        Text(
                            text = if (isLoginMode) "Welcome Back" else "Create Account",
                            fontSize = 26.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = if (isLoginMode) "Please enter your details to sign in." else "Please fill in the details to create your account.",
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.height(20.dp))

                        // 🔀 Log In / Sign Up سوئچر
                        AuthTabSwitcher(isLoginMode) { isLoginMode = it }
                        Spacer(modifier = Modifier.height(20.dp))

                        // 👤 نام (صرف Sign Up میں)
                        if (!isLoginMode) {
                            FieldLabel("Full Name")
                            OutlinedTextField(
                                value = fullName,
                                onValueChange = { fullName = it },
                                placeholder = { Text("e.g. John Doe") },
                                leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
                                modifier = Modifier.fillMaxWidth(),
                                singleLine = true,
                                shape = RoundedCornerShape(14.dp)
                            )
                            Spacer(modifier = Modifier.height(14.dp))
                        }

                        // 📧 ای میل
                        FieldLabel("Email Address")
                        OutlinedTextField(
                            value = email,
                            onValueChange = { email = it },
                            placeholder = { Text("name@company.com") },
                            leadingIcon = { Icon(Icons.Default.Mail, contentDescription = null) },
                            trailingIcon = {
                                if (isEmailValid) {
                                    Icon(Icons.Default.CheckCircle, contentDescription = "Valid", tint = Color(0xFF43A047))
                                }
                            },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true,
                            shape = RoundedCornerShape(14.dp)
                        )
                        Spacer(modifier = Modifier.height(14.dp))

                        // 🔒 پاسورڈ
                        FieldLabel(if (isLoginMode) "Password" else "Create Password")
                        OutlinedTextField(
                            value = password,
                            onValueChange = { password = it },
                            placeholder = { Text("••••••••") },
                            leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
                            trailingIcon = {
                                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                    Icon(
                                        if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                        contentDescription = "Toggle password visibility"
                                    )
                                }
                            },
                            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true,
                            shape = RoundedCornerShape(14.dp)
                        )

                        // 💪 پاسورڈ کی طاقت (صرف Sign Up میں)
                        if (!isLoginMode && password.isNotEmpty()) {
                            Spacer(modifier = Modifier.height(8.dp))
                            StrengthBars(passwordStrength(password))
                        }

                        // Forgot Password / Terms
                        if (isLoginMode) {
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                                TextButton(onClick = { }) {
                                    Text("Forgot Password?", fontSize = 13.sp, color = MaterialTheme.colorScheme.primary)
                                }
                            }
                        } else {
                            Spacer(modifier = Modifier.height(8.dp))
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Checkbox(
                                    checked = agreeTerms,
                                    onCheckedChange = { agreeTerms = it },
                                    colors = CheckboxDefaults.colors(checkedColor = MaterialTheme.colorScheme.primary)
                                )
                                Text(
                                    "I agree to the Terms & Privacy",
                                    fontSize = 13.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // ✅ مرکزی بٹن (لاگ ان / سائن اپ)
                        Button(
                            onClick = {
                                // 👇 یہاں لاگ ان اسٹیٹ سیٹ ہو رہی ہے
                                AuthState.isLoggedIn = true
                                AuthState.userEmail = email
                                if (!isLoginMode) AuthState.userName = fullName
                                
                                // 👇 سکرین بند کر کے واپس جائیں
                                navigator.pop()
                            },
                            enabled = canSubmit,
                            modifier = Modifier.fillMaxWidth().height(52.dp),
                            shape = RoundedCornerShape(16.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.primary,
                                contentColor = MaterialTheme.colorScheme.onPrimary
                            )
                        ) {
                            Text(
                                if (isLoginMode) "Sign In" else "Create Account",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // 🔁 موڈ تبدیل کرنے کا لنک
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = if (isLoginMode) "Don't have an account? " else "Already have an account? ",
                                fontSize = 14.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                text = if (isLoginMode) "Sign Up" else "Log In",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.clickable { isLoginMode = !isLoginMode }
                            )
                        }
                    }
                }
            }
        }
    }
}

// ---------- چھوٹے مددگار حصے ----------

@Composable
private fun FieldLabel(text: String) {
    Text(
        text = text,
        fontSize = 13.sp,
        fontWeight = FontWeight.Medium,
        color = MaterialTheme.colorScheme.onSurface,
        modifier = Modifier.padding(bottom = 6.dp)
    )
}

@Composable
private fun AuthTabSwitcher(isLogin: Boolean, onSelect: (Boolean) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
            .padding(4.dp)
    ) {
        TabPill("Log In", selected = isLogin, onClick = { onSelect(true) })
        TabPill("Sign Up", selected = !isLogin, onClick = { onSelect(false) })
    }
}

@Composable
private fun RowScope.TabPill(text: String, selected: Boolean, onClick: () -> Unit) {
    Surface(
        modifier = Modifier.weight(1f),
        shape = RoundedCornerShape(20.dp),
        color = if (selected) MaterialTheme.colorScheme.surface else Color.Transparent,
        shadowElevation = if (selected) 2.dp else 0.dp,
        onClick = onClick
    ) {
        Box(contentAlignment = Alignment.Center, modifier = Modifier.padding(vertical = 10.dp)) {
            Text(
                text = text,
                fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal,
                color = if (selected) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun StrengthBars(strength: Int) {
    Row(horizontalArrangement = Arrangement.spacedBy(6.dp), modifier = Modifier.fillMaxWidth()) {
        repeat(4) { index ->
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(4.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .background(
                        if (index < strength) strengthColor(strength)
                        else MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)
                    )
            )
        }
    }
}

private fun strengthColor(strength: Int): Color = when (strength) {
    1 -> Color(0xFFE53935)
    2 -> Color(0xFFFFA726)
    3 -> Color(0xFFD4E157)
    else -> Color(0xFF43A047)
}

private fun passwordStrength(pw: String): Int {
    var score = 0
    if (pw.length >= 6) score++
    if (pw.length >= 10) score++
    if (pw.any { it.isUpperCase() } && pw.any { it.isLowerCase() }) score++
    if (pw.any { it.isDigit() } || pw.any { !it.isLetterOrDigit() }) score++
    return score
}
