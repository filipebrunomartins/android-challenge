package com.challenge.movieflux.feature.login.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.challenge.movieflux.core.navigation.MovieFluxRoutes

fun NavController.navigateToLoginScreen() {
    navigate(MovieFluxRoutes.LOGIN) {
        popUpTo(0) { inclusive = true }
    }
}

fun NavGraphBuilder.loginGraph(
    onLoginSuccess: () -> Unit
) {
    composable(route = MovieFluxRoutes.LOGIN) {
        LoginScreenRoute(
            onLoginSuccess = onLoginSuccess
        )
    }
}
