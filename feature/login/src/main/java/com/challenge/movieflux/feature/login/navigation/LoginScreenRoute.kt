package com.challenge.movieflux.feature.login.navigation

import androidx.compose.runtime.Composable
import com.challenge.movieflux.feature.login.LoginScreen

@Composable
internal fun LoginScreenRoute(
    onLoginSuccess: () -> Unit
) {
    LoginScreen(
        onLoginSuccess = onLoginSuccess
    )
}
