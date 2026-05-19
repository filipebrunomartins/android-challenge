package com.challenge.movieflux.favorites.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.challenge.movieflux.core.navigation.MovieFluxRoutes

fun NavController.navigateToFavoritesScreen(navOptions: NavOptions? = null) {
    navigate(MovieFluxRoutes.FAVORITES, navOptions)
}

fun NavGraphBuilder.favoritesGraph(onMovieClick: (Int) -> Unit) {
    composable(route = MovieFluxRoutes.FAVORITES) {
        FavoritesScreenRoute(onMovieClick = onMovieClick)
    }
}
