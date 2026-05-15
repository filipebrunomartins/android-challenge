package com.challenge.movieflux.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.challenge.movieflux.core.navigation.MovieFluxRoutes
import com.challenge.movieflux.favorites.navigation.favoritesGraph
import com.challenge.movieflux.feature.home.navigation.homeGraph
import com.challenge.movieflux.feature.home.navigation.navigateToHome
import com.challenge.movieflux.feature.login.navigation.loginGraph
import com.challenge.movieflux.feature.login.navigation.navigateToLoginScreen
import com.challenge.movieflux.feature.profile.navigation.profileGraph

@Composable
fun MovieFluxNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    startDestination: String = MovieFluxRoutes.LOGIN
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        loginGraph(
            onLoginSuccess = {
                navController.navigateToHome()
            }
        )
        homeGraph(
            onMovieClick = { movieId ->
                // TODO: Navigate to details
            }
        )
        favoritesGraph()
        profileGraph(
            onLogout = {
                navController.navigateToLoginScreen()
            }
        )
    }
}
