package com.challenge.movieflux.feature.profile.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.challenge.movieflux.core.navigation.MovieFluxRoutes

fun NavController.navigateToProfileScreen() {
    navigate(MovieFluxRoutes.PROFILE)
}

fun NavGraphBuilder.profileGraph(
    onLogout: () -> Unit
) {
    composable(route = MovieFluxRoutes.PROFILE) {
        ProfileScreenRoute(onLogout = onLogout)
    }
}
