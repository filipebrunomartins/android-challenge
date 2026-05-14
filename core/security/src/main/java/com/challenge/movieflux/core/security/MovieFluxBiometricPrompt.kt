package com.challenge.movieflux.core.security

import androidx.biometric.AuthenticationResult
import androidx.biometric.AuthenticationResultCallback
import androidx.biometric.compose.rememberAuthenticationLauncher
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.biometric.AuthenticationRequest
import androidx.compose.runtime.LaunchedEffect

@Composable
fun MovieFluxBiometricPrompt(
    triggerAuthentication: Boolean,
    onSuccess: () -> Unit,
    onError: (String) -> Unit,
    onFailed: () -> Unit = {}
) {

    val authCallback = object : AuthenticationResultCallback {

        override fun onAuthResult(
            result: AuthenticationResult
        ) {

            when (result) {

                is AuthenticationResult.Success -> {
                    onSuccess()
                }

                is AuthenticationResult.Error -> {
                    onError(result.errString.toString())
                }
            }
        }

        override fun onAuthAttemptFailed() {
            onFailed()
        }
    }

    val authLauncher = rememberAuthenticationLauncher(
        resultCallback = authCallback
    )

    val authRequest = remember {

        AuthenticationRequest.Biometric.Builder(
            title = "Login seguro",
            authFallback = AuthenticationRequest.Biometric.Fallback.DeviceCredential,
        )
            .setSubtitle("Use sua biometria")
            .setMinStrength(
                AuthenticationRequest.Biometric.Strength.Class3()
            )
            .build()
    }

    LaunchedEffect(Unit) {

        if (triggerAuthentication) {
            authLauncher.launch(authRequest)
        }
    }
}