package com.challenge.movieflux.feature.login.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.challenge.movieflux.core.navigation.MovieFluxRoutes


fun NavController.navigateToLoginScreen() {
    navigate(MovieFluxRoutes.LOGIN)
}

fun NavGraphBuilder.loginGraph(
    onBackBtnClick: () -> Unit
) {
    composable(route = MovieFluxRoutes.LOGIN) {
        LoginScreenRoute(
            onBackBtnClick = onBackBtnClick
        )
    }
}