package com.challenge.movieflux.favorites.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.challenge.movieflux.core.navigation.MovieFluxRoutes

fun NavController.navigateToFavoritesScreen() {
    navigate(MovieFluxRoutes.FAVORITES)
}

fun NavGraphBuilder.favoritesGraph() {
    composable(route = MovieFluxRoutes.FAVORITES) {
        FavoritesScreenRoute()
    }
}