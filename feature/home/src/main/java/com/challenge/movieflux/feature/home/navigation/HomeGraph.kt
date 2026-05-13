package com.challenge.movieflux.feature.home.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.challenge.movieflux.core.navigation.MovieFluxRoutes
import com.challenge.movieflux.feature.home.HomeScreen

fun NavGraphBuilder.homeGraph() {
    composable(MovieFluxRoutes.HOME) {
        HomeScreen()
    }
}