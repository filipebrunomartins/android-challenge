package com.challenge.movieflux.feature.login

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import com.challenge.movieflux.core.designsystem.component.MovieFluxButton
import com.challenge.movieflux.core.designsystem.component.MovieFluxOutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.ui.unit.dp

@Composable
fun LoginScreen(
    uiState: LoginUiState,
    onLoginClick: (String, String) -> Unit,
    onEnableBiometricClick: () -> Unit,
    onBiometricRetry: () -> Unit
) {

    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    val context = LocalContext.current

    when (uiState) {

        is LoginUiState.Loading -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }

        is LoginUiState.Error -> {
            LaunchedEffect(uiState.message) {
                Toast.makeText(context, uiState.message, Toast.LENGTH_SHORT).show()
            }
        }

        is LoginUiState.LoggedOut -> {

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center
            ) {

                Text(
                    text = "Login",
                    style = MaterialTheme.typography.headlineMedium
                )

                Spacer(modifier = Modifier.height(16.dp))

                // USER
                OutlinedTextField(
                    value = username,
                    onValueChange = { username = it },
                    label = { Text("Usuário") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(12.dp))

                // PASSWORD
                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Senha") },
                    singleLine = true,
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(20.dp))

                MovieFluxButton(
                    onClick = {
                        onLoginClick(username, password)
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Entrar")
                }
            }
        }

        is LoginUiState.NeedBiometric -> {

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center
            ) {

                Text(
                    text = "Ativar biometria?",
                    style = MaterialTheme.typography.headlineMedium
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Deseja usar biometria para próximos acessos?"
                )

                Spacer(modifier = Modifier.height(20.dp))

                MovieFluxButton(
                    onClick = onEnableBiometricClick,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Sim, ativar")
                }

                Spacer(modifier = Modifier.height(12.dp))

                MovieFluxOutlinedButton(
                    onClick = onEnableBiometricClick,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Agora não")
                }
            }
        }

        is LoginUiState.LoggedIn -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text("Redirecionando...")
            }
        }
    }
}