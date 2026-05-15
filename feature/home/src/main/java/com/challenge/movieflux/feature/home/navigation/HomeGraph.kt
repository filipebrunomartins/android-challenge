package com.challenge.movieflux.feature.home.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.NavController
import com.challenge.movieflux.core.navigation.MovieFluxRoutes
import com.challenge.movieflux.feature.home.HomeRoute

fun NavController.navigateToHome() {
    navigate(MovieFluxRoutes.HOME) {
        popUpTo(MovieFluxRoutes.LOGIN) { inclusive = true }
    }
}

fun NavGraphBuilder.homeGraph(
    onMovieClick: (Int) -> Unit
) {
    composable(MovieFluxRoutes.HOME) {
        HomeRoute(onMovieClick = onMovieClick)
    }
}