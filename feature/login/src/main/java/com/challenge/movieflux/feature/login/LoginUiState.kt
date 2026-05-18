package com.challenge.movieflux.feature.login


sealed interface LoginUiState {

    data object Loading : LoginUiState

    data object Initial : LoginUiState

    data object AskToEnableBiometric : LoginUiState
    data object AskFastLogin : LoginUiState

    data object BiometricPrompt : LoginUiState

    data object LoggedIn : LoginUiState

    data class Error(val message: String) : LoginUiState

    data class BiometricError(
        val message: String,
        val canResumeWithoutBiometric: Boolean
    ) : LoginUiState
}