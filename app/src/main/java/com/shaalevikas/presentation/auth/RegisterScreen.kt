package com.shaalevikas.presentation.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.shaalevikas.presentation.components.PrimaryButton
import com.shaalevikas.presentation.components.PrimaryTextField

@Composable
fun RegisterScreen(
    onRegisterSuccess: () -> Unit,
    onNavigateToLogin: () -> Unit
) {

    var email by remember {
        mutableStateOf("")
    }

    var password by remember {
        mutableStateOf("")
    }

    var errorMessage by remember {
        mutableStateOf("")
    }

    val auth = FirebaseAuth.getInstance()

    val backgroundGradient = Brush.verticalGradient(
        colorStops = arrayOf(
            0.0f to Color(0xFF0D1547),
            0.40f to Color(0xFF1A237E),
            0.70f to Color(0xFF303F9F),
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

            Spacer(modifier = Modifier.height(64.dp))

            Text(
                text = "Shaale-Vikas",
                fontSize = 34.sp,
                fontWeight = FontWeight.Black,
                color = Color.White,
                letterSpacing = (-0.5).sp
            )

            Spacer(modifier = Modifier.height(6.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {

                Box(
                    modifier = Modifier
                        .height(1.dp)
                        .weight(1f)
                        .background(Color(0x449FA8DA))
                )

                Text(
                    text = "  Join the community  ",
                    fontSize = 12.sp,
                    color = Color(0xFF9FA8DA),
                    letterSpacing = 1.8.sp,
                    fontWeight = FontWeight.Medium
                )

                Box(
                    modifier = Modifier
                        .height(1.dp)
                        .weight(1f)
                        .background(Color(0x449FA8DA))
                )
            }

            Spacer(modifier = Modifier.height(44.dp))

            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 22.dp)
                    .shadow(
                        elevation = 30.dp,
                        shape = cardShape,
                        spotColor = Color(0x660D1547),
                        ambientColor = Color(0x330D1547)
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
                        text = "Create your account",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1A237E)
                    )

                    Text(
                        text = "Start making a difference today",
                        fontSize = 13.sp,
                        color = Color(0xFF7986CB),

                        modifier = Modifier.padding(
                            top = 4.dp,
                            bottom = 26.dp
                        )
                    )

                    PrimaryTextField(
                        value = email,
                        onValueChange = {
                            email = it
                        },
                        label = "Email Address",
                        keyboardType = KeyboardType.Email
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    PrimaryTextField(
                        value = password,
                        onValueChange = {
                            password = it
                        },
                        label = "Password",
                        isPassword = true,
                        keyboardType = KeyboardType.Password
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "Use at least 6 characters",
                        fontSize = 12.sp,
                        color = Color(0xFFB0BAD8),

                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 4.dp)
                    )

                    Spacer(modifier = Modifier.height(22.dp))

                    PrimaryButton(
                        text = "Create Account",

                        onClick = {

                            auth.createUserWithEmailAndPassword(
                                email,
                                password
                            ).addOnCompleteListener { task ->

                                if (task.isSuccessful) {

                                    val uid = auth.currentUser?.uid

                                    if (uid != null) {

                                        val firestore = FirebaseFirestore.getInstance()

                                        val userData = hashMapOf(
                                            "email" to email,
                                            "role" to "alumni"
                                        )

                                        firestore.collection("users")
                                            .document(uid)
                                            .set(userData)
                                            .addOnSuccessListener {
                                                onRegisterSuccess()
                                            }
                                            .addOnFailureListener {
                                                errorMessage = "Failed to save user role"
                                            }

                                    } else {
                                        errorMessage = "User ID not found"
                                    }

                                } else {

                                    errorMessage =
                                        task.exception?.message
                                            ?: "Registration Failed"
                                }
                            }
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

                    Spacer(modifier = Modifier.height(20.dp))

                    TextButton(
                        onClick = {
                            onNavigateToLogin()
                        },

                        modifier = Modifier.fillMaxWidth()
                    ) {

                        Text(
                            text = "Already have an account? Sign in",
                            color = Color(0xFF3D52D5),
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(48.dp))

            Text(
                text = "Empowering schools · Strengthening communities",
                fontSize = 11.sp,
                color = Color(0x99FFFFFF),
                letterSpacing = 0.3.sp
            )

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}