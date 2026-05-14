package com.challenge.movieflux.feature.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.challenge.movieflux.core.designsystem.component.MovieFluxButton

@Composable
internal fun ProfileScreen(
    viewModel: ProfileViewModel = hiltViewModel(),
    onLogout: () -> Unit
){
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("PROFILE")
            MovieFluxButton(
                onClick = {
                    viewModel.logout()
                    onLogout()
                },
                text = { Text("Logout") }
            )
            MovieFluxButton(
                onClick = {
                    viewModel.clearSession()
                    onLogout()
                },
                text = { Text("Limpar sessão") }
            )
        }
    }
}
