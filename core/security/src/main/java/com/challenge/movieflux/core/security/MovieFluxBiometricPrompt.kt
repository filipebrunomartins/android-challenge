package com.challenge.movieflux.core.security

import androidx.biometric.AuthenticationRequest
import androidx.biometric.AuthenticationResult
import androidx.biometric.AuthenticationResultCallback
import androidx.biometric.compose.rememberAuthenticationLauncher
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState

@Composable
fun MovieFluxBiometricPrompt(
    triggerAuthentication: Boolean,
    onSuccess: () -> Unit,
    onError: (String) -> Unit,
    onFailed: () -> Unit = {}
) {

    val currentOnSuccess by rememberUpdatedState(onSuccess)
    val currentOnError by rememberUpdatedState(onError)
    val currentOnFailed by rememberUpdatedState(onFailed)

    val authCallback = remember {
        object : AuthenticationResultCallback {

            override fun onAuthResult(
                result: AuthenticationResult
            ) {

                when (result) {

                    is AuthenticationResult.Success -> {
                        currentOnSuccess()
                    }

                    is AuthenticationResult.Error -> {
                        currentOnError(result.errString.toString())
                    }
                }
            }

            override fun onAuthAttemptFailed() {
                currentOnFailed()
            }
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

    LaunchedEffect(triggerAuthentication) {

        if (triggerAuthentication) {
            authLauncher.launch(authRequest)
        }
    }
}