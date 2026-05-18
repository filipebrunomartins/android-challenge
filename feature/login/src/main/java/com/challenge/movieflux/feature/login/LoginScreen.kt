package com.challenge.movieflux.feature.login

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.challenge.movieflux.core.designsystem.component.MovieFluxButton
import com.challenge.movieflux.core.designsystem.theme.MovieFluxTheme
import com.challenge.movieflux.core.security.MovieFluxBiometricPrompt

@Composable
fun LoginScreen(
    viewModel: LoginViewModel = hiltViewModel(),
    onLoginSuccess: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    LaunchedEffect(uiState) {
        when (uiState) {
            is LoginUiState.LoggedIn -> {
                onLoginSuccess()
            }
            is LoginUiState.Error -> {
                Toast.makeText(context, (uiState as LoginUiState.Error).message, Toast.LENGTH_SHORT).show()
                viewModel.clearError()
            }
            else -> {}
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Login",
                style = MovieFluxTheme.typography.headlineMedium
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = username,
                onValueChange = { username = it },
                label = { Text("Usuário") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                enabled = uiState !is LoginUiState.Loading
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Senha") },
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth(),
                enabled = uiState !is LoginUiState.Loading
            )

            Spacer(modifier = Modifier.height(20.dp))

            MovieFluxButton(
                onClick = { viewModel.login(username, password) },
                modifier = Modifier.fillMaxWidth(),
                enabled = uiState !is LoginUiState.Loading
            ) {
                Text("Entrar")
            }
        }
        when (uiState) {
            is LoginUiState.Loading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .clickable(enabled = false) { }
                        .background(Color.Black.copy(alpha = 0.12f)),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            is LoginUiState.AskToEnableBiometric -> {
                AlertDialog(
                    onDismissRequest = { viewModel.askBiometricNotNow() },
                    title = { Text("Ativar biometria") },
                    text = { Text("Deseja usar biometria nos próximos acessos?") },
                    confirmButton = {
                        TextButton(onClick = { viewModel.askBiometricConfirm() }) {
                            Text("Ativar")
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = { viewModel.askBiometricNotNow() }) {
                            Text("Agora não")
                        }
                    }
                )
            }

            is LoginUiState.AskFastLogin -> {
                AlertDialog(
                    onDismissRequest = { viewModel.askFatLoginNotNow() },
                    title = { Text("Validar biometria") },
                    text = { Text("Bem-vindo de volta. Deseja fazer o login rápido?") },
                    confirmButton = {
                        TextButton(onClick = { viewModel.askBiometricConfirm() }) {
                            Text("Validar")
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = { viewModel.askFatLoginNotNow() }) {
                            Text("Agora não")
                        }
                    }
                )
            }

            is LoginUiState.BiometricPrompt -> {
                MovieFluxBiometricPrompt(
                    triggerAuthentication = true,
                    onSuccess = { viewModel.onBiometricSuccess() },
                    onError = { message -> viewModel.onBiometricError(message) },
                    onFailed = {
                        Toast.makeText(context, "Digital não reconhecida", Toast.LENGTH_SHORT).show()
                    }
                )
            }

            is LoginUiState.BiometricError -> {
                AlertDialog(
                    onDismissRequest = { viewModel.clearError() },
                    title = { Text("Erro na biometria") },
                    text = { Text((uiState as LoginUiState.BiometricError).message) },
                    confirmButton = {
                        TextButton(onClick = { viewModel.askBiometricConfirm() }) {
                            Text("Tentar novamente")
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = { viewModel.onBiometricLogWithout() }) {
                            Text("Logar sem biometria")
                        }
                    }
                )
            }
            else -> {}
        }
    }
}
