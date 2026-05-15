package com.challenge.movieflux.feature.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.challenge.movieflux.core.domain.login.usecase.CheckSessionUseCase
import com.challenge.movieflux.core.domain.login.usecase.EnableBiometricUseCase
import com.challenge.movieflux.core.domain.login.usecase.IsBiometricEnabledUseCase
import com.challenge.movieflux.core.domain.login.usecase.LoginUseCase
import com.challenge.movieflux.core.domain.login.usecase.SetLoggedInUseCase
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
    private val setLoggedInUseCase: SetLoggedInUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<LoginUiState>(LoginUiState.Initial)
    val uiState: StateFlow<LoginUiState> = _uiState

    init {
        checkInitialState()
    }

    private fun checkInitialState() {
        viewModelScope.launch {
            _uiState.value = LoginUiState.Loading

            val sessionActive = checkSessionUseCase()
            if (sessionActive) {
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
                _uiState.value = LoginUiState.Error("Usuário ou senha inválidos")
                return@launch
            }

            val biometricEnabled = isBiometricEnabledUseCase()

            _uiState.value = if (biometricEnabled) {
                LoginUiState.LoggedIn
            } else {
                LoginUiState.AskToEnableBiometric
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

    fun onBiometricFailed() {
        _uiState.value = LoginUiState.Error("Falha na biometria")
    }

    fun clearError() {
        _uiState.value = LoginUiState.Initial
    }
}