package com.shaalevikas.presentation.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.KeyboardType

@Composable
fun PrimaryTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    isPassword: Boolean = false,
    keyboardType: KeyboardType = KeyboardType.Text
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,

        label = {
            Text(
                text = label,
                fontSize = 14.sp
            )
        },

        modifier = Modifier.fillMaxWidth(),

        shape = RoundedCornerShape(14.dp),

        singleLine = true,

        keyboardOptions = KeyboardOptions(
            keyboardType = keyboardType
        ),

        visualTransformation = if (isPassword)
            PasswordVisualTransformation()
        else
            VisualTransformation.None,

        colors = OutlinedTextFieldDefaults.colors(
            // Border
            focusedBorderColor  = Color(0xFF3D52D5),
            unfocusedBorderColor = Color(0xFFCDD5EF),

            // Label
            focusedLabelColor   = Color(0xFF3D52D5),
            unfocusedLabelColor = Color(0xFF7986CB),

            // Text
            focusedTextColor    = Color(0xFF1A237E),
            unfocusedTextColor  = Color(0xFF283593),

            // Cursor
            cursorColor = Color(0xFF3D52D5),

            // Container fill (gives the field a slightly tinted surface)
            focusedContainerColor   = Color(0xFFF5F6FF),
            unfocusedContainerColor = Color(0xFFF5F6FF)
        )
    )
}