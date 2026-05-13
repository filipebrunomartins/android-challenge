package com.challenge.movieflux.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.challenge.movieflux.core.navigation.MovieFluxRoutes
import com.challenge.movieflux.feature.home.navigation.homeGraph
import com.challenge.movieflux.feature.login.navigation.loginGraph

@Composable
fun MovieFluxNavHost(
    navController: NavHostController = rememberNavController(),
    startDestination: String = MovieFluxRoutes.LOGIN
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        loginGraph(
            onBackBtnClick = {}
        )
        homeGraph()
    }
}