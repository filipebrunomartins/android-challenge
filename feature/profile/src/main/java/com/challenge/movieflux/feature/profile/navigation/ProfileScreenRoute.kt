package com.challenge.movieflux.feature.profile.navigation

import androidx.compose.runtime.Composable
import com.challenge.movieflux.feature.profile.ProfileScreen

@Composable
internal fun ProfileScreenRoute(
    onLogout: () -> Unit
) {
    ProfileScreen(onLogout = onLogout)
}
