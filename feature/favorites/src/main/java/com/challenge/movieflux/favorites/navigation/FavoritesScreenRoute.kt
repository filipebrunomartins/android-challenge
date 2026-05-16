package com.challenge.movieflux.favorites.navigation

import androidx.compose.runtime.Composable
import com.challenge.movieflux.favorites.FavoritesRoute

@Composable
internal fun FavoritesScreenRoute(onMovieClick: (Int) -> Unit) {
    FavoritesRoute(onMovieClick = onMovieClick)
}
