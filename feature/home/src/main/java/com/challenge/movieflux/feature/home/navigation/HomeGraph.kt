package com.challenge.movieflux.feature.home.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.challenge.movieflux.core.navigation.MovieFluxRoutes
import com.challenge.movieflux.feature.home.HomeRoute
import com.challenge.movieflux.feature.home.MovieDetailRoute

fun NavController.navigateToHome(navOptions: NavOptions? = null) {
    if (navOptions != null) {
        navigate(MovieFluxRoutes.HOME, navOptions)
    } else {
        navigate(MovieFluxRoutes.HOME) {
            popUpTo(MovieFluxRoutes.LOGIN) { inclusive = true }
        }
    }
}

fun NavController.navigateToMovieDetail(movieId: Int) {
    navigate(MovieFluxRoutes.DETAIL.replace("{movieId}", movieId.toString()))
}

fun NavGraphBuilder.homeGraph(
    onMovieClick: (Int) -> Unit,
    onBackClick: () -> Unit
) {
    composable(MovieFluxRoutes.HOME) {
        HomeRoute(onMovieClick = onMovieClick)
    }

    composable(
        route = MovieFluxRoutes.DETAIL,
        arguments = listOf(
            navArgument("movieId") { type = NavType.IntType }
        )
    ) {
        MovieDetailRoute(onBackClick = onBackClick)
    }
}
