package com.shaalevikas.presentation.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.shaalevikas.data.repository.AuthRepository
import com.shaalevikas.presentation.components.PrimaryButton
import com.shaalevikas.presentation.components.PrimaryTextField
import androidx.compose.ui.text.input.KeyboardType

@Composable
fun LoginScreen(
    onLoginClick: (String) -> Unit,
    onNavigateToRegister: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }
    val authRepository = remember { AuthRepository() }

    // ── Deep indigo → medium blue-indigo gradient background ──────────────
    val backgroundGradient = Brush.verticalGradient(
        colorStops = arrayOf(
            0.0f to Color(0xFF12185A),
            0.45f to Color(0xFF283593),
            0.75f to Color(0xFF3949AB),
            1.0f to Color(0xFF5C6BC0)
        )
    )

    val cardShape = RoundedCornerShape(28.dp)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundGradient)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // ── App name hero ──────────────────────────────────────────────
            Spacer(modifier = Modifier.height(72.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Shaale",
                    fontSize = 46.sp,
                    fontWeight = FontWeight.Black,
                    color = Color.White,
                    letterSpacing = (-1).sp
                )
                Text(
                    text = "-",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Light,
                    color = Color(0xFF9FA8DA),
                    modifier = Modifier.padding(horizontal = 2.dp)
                )
                Text(
                    text = "Vikas",
                    fontSize = 46.sp,
                    fontWeight = FontWeight.Light,
                    color = Color(0xFFBBCEFF),
                    letterSpacing = 3.sp
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "COMMUNITY WELFARE PLATFORM",
                fontSize = 10.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF9FA8DA),
                letterSpacing = 2.5.sp
            )

            Spacer(modifier = Modifier.height(52.dp))

            // ── Floating form card ─────────────────────────────────────────
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 22.dp)
                    .shadow(
                        elevation = 30.dp,
                        shape = cardShape,
                        spotColor = Color(0x6612185A),
                        ambientColor = Color(0x3312185A)
                    ),
                shape = cardShape,
                color = Color(0xFFF8F9FF),
                tonalElevation = 0.dp
            ) {
                Column(
                    modifier = Modifier.padding(
                        horizontal = 28.dp,
                        vertical = 32.dp
                    ),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Text(
                        text = "Welcome back",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1A237E)
                    )

                    Text(
                        text = "Sign in to your account",
                        fontSize = 13.sp,
                        color = Color(0xFF7986CB),
                        modifier = Modifier.padding(top = 4.dp, bottom = 26.dp)
                    )

                    PrimaryTextField(
                        value = email,
                        onValueChange = { email = it },
                        label = "Email Address",
                        keyboardType = KeyboardType.Email
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    PrimaryTextField(
                        value = password,
                        onValueChange = { password = it },
                        label = "Password",
                        isPassword = true,
                        keyboardType = KeyboardType.Password
                    )

                    Spacer(modifier = Modifier.height(26.dp))

                    PrimaryButton(
                        text = "Sign In",
                        onClick = {
                            authRepository.login(
                                email = email,
                                password = password,
                                onSuccess = { role ->
                                    onLoginClick(role)
                                },
                                onFailure = { errorMessage = it }
                            )
                        }
                    )

                    if (errorMessage.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(14.dp))
                        Text(
                            text = errorMessage,
                            color = Color(0xFFD32F2F),
                            fontSize = 13.sp,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                    Spacer(modifier = Modifier.height(22.dp))

                    // ── Divider ────────────────────────────────────────────
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        HorizontalDivider(
                            modifier = Modifier.weight(1f),
                            color = Color(0xFFE3E8F8),
                            thickness = 1.dp
                        )
                        Text(
                            text = "  or  ",
                            color = Color(0xFFB0BAD8),
                            fontSize = 12.sp
                        )
                        HorizontalDivider(
                            modifier = Modifier.weight(1f),
                            color = Color(0xFFE3E8F8),
                            thickness = 1.dp
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    TextButton(
                        onClick = { onNavigateToRegister() },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "New here? Create an account",
                            color = Color(0xFF3D52D5),
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(48.dp))

            Text(
                text = "Empowering institutions.\nStrengthening future generations.",
                fontSize = 11.sp,
                color = Color(0x99FFFFFF),
                letterSpacing = 0.3.sp
            )

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}