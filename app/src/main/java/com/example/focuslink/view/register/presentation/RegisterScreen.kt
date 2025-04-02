package com.example.focuslink.view.register.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.focuslink.R
import com.example.focuslink.ui.theme.PinkPrimary

@Composable
fun RegisterScreen(
    registerViewModel: RegisterViewModel,
    navigateToLogin: () -> Unit
) {
    val uiState by registerViewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .size(100.dp)
                .clip(CircleShape)
                .background(PinkPrimary),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "FL",
                color = MaterialTheme.colorScheme.onPrimary,
                fontWeight = FontWeight.Bold,
                fontSize = 36.sp
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Registro",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = PinkPrimary
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Nombre de usuario
        OutlinedTextField(
            value = uiState.username,
            onValueChange = { registerViewModel.updateUsername(it) },
            label = { Text("Nombre de usuario") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Email
        OutlinedTextField(
            value = uiState.email,
            onValueChange = { registerViewModel.updateEmail(it) },
            label = { Text("Email") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Contraseña
        OutlinedTextField(
            value = uiState.password,
            onValueChange = { registerViewModel.updatePassword(it) },
            label = { Text("Contraseña") },
            singleLine = true,
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Confirmar contraseña
        OutlinedTextField(
            value = uiState.confirmPassword,
            onValueChange = { registerViewModel.updateConfirmPassword(it) },
            label = { Text("Confirmar contraseña") },
            singleLine = true,
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Botón de registro
        Button(
            onClick = {
                registerViewModel.register()
                if (uiState.isRegistered) navigateToLogin()
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(containerColor = PinkPrimary)
        ) {
            Text("Registrarse")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Enlace para iniciar sesión
        TextButton(onClick = navigateToLogin) {
            Text("¿Ya tienes una cuenta? Inicia sesión", color = PinkPrimary)
        }

        // Mostrar mensaje de error si hay alguno
        if (uiState.errorMessage.isNotEmpty()) {
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = uiState.errorMessage,
                color = MaterialTheme.colorScheme.error
            )
        }

        // Mostrar indicador de carga
        if (uiState.isLoading) {
            Spacer(modifier = Modifier.height(16.dp))
            CircularProgressIndicator(color = PinkPrimary)
        }
    }
}