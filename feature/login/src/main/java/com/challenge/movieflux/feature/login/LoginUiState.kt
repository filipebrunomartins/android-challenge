package com.challenge.movieflux.feature.login


sealed interface LoginUiState {

    data object Loading : LoginUiState

    data object NeedBiometric : LoginUiState

    data object AskToEnableBiometric : LoginUiState

    data object LoggedIn : LoginUiState

    data class Error(val message: String) : LoginUiState
}