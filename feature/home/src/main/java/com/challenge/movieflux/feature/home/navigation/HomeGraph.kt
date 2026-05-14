package com.challenge.movieflux.feature.home.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.NavController
import com.challenge.movieflux.core.navigation.MovieFluxRoutes
import com.challenge.movieflux.feature.home.HomeScreen

fun NavController.navigateToHome() {
    navigate(MovieFluxRoutes.HOME) {
        popUpTo(MovieFluxRoutes.LOGIN) { inclusive = true }
    }
}

fun NavGraphBuilder.homeGraph() {
    composable(MovieFluxRoutes.HOME) {
        HomeScreen()
    }
}