package com.challenge.movieflux.feature.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.challenge.movieflux.core.domain.login.usecase.CheckSessionUseCase
import com.challenge.movieflux.core.domain.login.usecase.EnableBiometricUseCase
import com.challenge.movieflux.core.domain.login.usecase.IsBiometricEnabledUseCase
import com.challenge.movieflux.core.domain.login.usecase.LoginUseCase
import com.challenge.movieflux.core.domain.login.usecase.SetLoggedInUseCase
import com.challenge.movieflux.core.security.BiometricMovieFluxManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
    private val checkSessionUseCase: CheckSessionUseCase,
    private val isBiometricEnabledUseCase: IsBiometricEnabledUseCase,
    private val enableBiometricUseCase: EnableBiometricUseCase,
    private val setLoggedInUseCase: SetLoggedInUseCase,
    private val biometricManager: BiometricMovieFluxManager
) : ViewModel() {

    private val _uiState = MutableStateFlow<LoginUiState>(LoginUiState.Initial)
    val uiState: StateFlow<LoginUiState> = _uiState

    private var isAuthenticated = false

    init {
        checkInitialState()
    }

    private fun checkInitialState() {
        viewModelScope.launch {
            _uiState.value = LoginUiState.Loading

            val sessionActive = checkSessionUseCase()
            if (sessionActive) {
                isAuthenticated = true
                _uiState.value = LoginUiState.LoggedIn
                return@launch
            }

            val biometricEnabled = isBiometricEnabledUseCase()
            _uiState.value = if (biometricEnabled) {
                LoginUiState.AskFastLogin
            } else {
                LoginUiState.Initial
            }
        }
    }

    fun login(username: String, password: String) {
        viewModelScope.launch {
            _uiState.value = LoginUiState.Loading

            val success = loginUseCase(username, password)

            if (!success) {
                isAuthenticated = false
                _uiState.value = LoginUiState.Error("Usuário ou senha inválidos")
                return@launch
            }

            isAuthenticated = true
            val biometricEnabled = isBiometricEnabledUseCase()
            val canAuthenticate = biometricManager.canAuthenticate()

            _uiState.value = if (biometricEnabled) {
                LoginUiState.LoggedIn
            } else if (canAuthenticate) {
                LoginUiState.AskToEnableBiometric
            } else {
                LoginUiState.LoggedIn
            }
        }
    }

    private fun enableBiometric() {
        viewModelScope.launch {
            enableBiometricUseCase()
            setLoggedInUseCase(true)
            _uiState.value = LoginUiState.LoggedIn
        }
    }

    fun askBiometricConfirm() {
        _uiState.value = LoginUiState.BiometricPrompt
    }

    fun askBiometricNotNow() {
        _uiState.value = LoginUiState.LoggedIn
    }

    fun askFatLoginNotNow() {
        _uiState.value = LoginUiState.Initial
    }

    fun onBiometricSuccess() {
        enableBiometric()
    }

    fun onBiometricError(message: String) {
        _uiState.value = LoginUiState.BiometricError(message, isAuthenticated)
    }

    fun onBiometricLogWithout() {
        viewModelScope.launch {
            setLoggedInUseCase(true)
            _uiState.value = LoginUiState.LoggedIn
        }
    }

    fun clearError() {
        _uiState.value = LoginUiState.Initial
    }
}