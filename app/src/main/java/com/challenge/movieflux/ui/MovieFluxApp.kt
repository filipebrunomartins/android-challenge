package com.challenge.movieflux.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navOptions
import com.challenge.movieflux.core.designsystem.icon.MovieFluxIcons
import com.challenge.movieflux.core.navigation.MovieFluxRoutes
import com.challenge.movieflux.favorites.navigation.navigateToFavoritesScreen
import com.challenge.movieflux.feature.home.navigation.navigateToHome
import com.challenge.movieflux.feature.profile.navigation.navigateToProfileScreen
import com.challenge.movieflux.navigation.MovieFluxNavHost

@Composable
fun MovieFluxApp(
    navController: NavHostController = rememberNavController()
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    val topLevelDestinations = listOf(
        TopLevelDestination.HOME,
        TopLevelDestination.FAVORITES,
        TopLevelDestination.PROFILE
    )

    val showBottomBar = topLevelDestinations.any { it.route == currentDestination?.route }

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                MovieFluxBottomBar(
                    destinations = topLevelDestinations,
                    onNavigateToDestination = { destination ->
                        val topLevelNavOptions = navOptions {
                            popUpTo(MovieFluxRoutes.HOME) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }

                        when (destination) {
                            TopLevelDestination.HOME -> navController.navigateToHome(topLevelNavOptions)
                            TopLevelDestination.FAVORITES -> navController.navigateToFavoritesScreen(topLevelNavOptions)
                            TopLevelDestination.PROFILE -> navController.navigateToProfileScreen(topLevelNavOptions)
                        }
                    },
                    currentDestination = currentDestination
                )
            }
        }
    ) { padding ->
        MovieFluxNavHost(
            navController = navController,
            modifier = Modifier.padding(padding)
        )
    }
}

//Todo ir para o designSystem
@Composable
private fun MovieFluxBottomBar(
    destinations: List<TopLevelDestination>,
    onNavigateToDestination: (TopLevelDestination) -> Unit,
    currentDestination: NavDestination?
) {
    NavigationBar {
        destinations.forEach { destination ->
            val selected = currentDestination.isTopLevelDestinationInHierarchy(destination)
            NavigationBarItem(
                selected = selected,
                onClick = { onNavigateToDestination(destination) },
                icon = {
                    Icon(
                        imageVector = destination.icon,
                        contentDescription = null
                    )
                },
                label = { Text(destination.label) }
            )
        }
    }
}

private fun NavDestination?.isTopLevelDestinationInHierarchy(destination: TopLevelDestination) =
    this?.hierarchy?.any {
        it.route?.contains(destination.route, true) ?: false
    } ?: false

enum class TopLevelDestination(
    val route: String,
    val icon: ImageVector,
    val label: String
) {
    HOME(MovieFluxRoutes.HOME, MovieFluxIcons.Home, "Home"),
    FAVORITES(MovieFluxRoutes.FAVORITES, MovieFluxIcons.Favorite, "Favorites"),
    PROFILE(MovieFluxRoutes.PROFILE, MovieFluxIcons.Person, "Profile")
}
