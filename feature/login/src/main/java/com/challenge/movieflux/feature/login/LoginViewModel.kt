package com.challenge.movieflux.feature.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.challenge.movieflux.core.domain.login.usecase.CheckSessionUseCase
import com.challenge.movieflux.core.domain.login.usecase.EnableBiometricUseCase
import com.challenge.movieflux.core.domain.login.usecase.IsBiometricEnabledUseCase
import com.challenge.movieflux.core.domain.login.usecase.LoginUseCase
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
    private val enableBiometricUseCase: EnableBiometricUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<LoginUiState>(LoginUiState.Loading)
    val uiState: StateFlow<LoginUiState> = _uiState

    init {
        checkInitialState()
    }

    private fun checkInitialState() {
        viewModelScope.launch {
            _uiState.value = LoginUiState.Loading

            val biometricEnabled = isBiometricEnabledUseCase()

            _uiState.value = if (biometricEnabled) {
                LoginUiState.NeedBiometric
            } else {
                LoginUiState.LoggedIn
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

    fun ignoreBiometric() {
        _uiState.value = LoginUiState.LoggedIn
    }

    // -------------------------
    // ENABLE BIOMETRIC AFTER FIRST LOGIN
    // -------------------------
    fun enableBiometric() {
        viewModelScope.launch {
            enableBiometricUseCase()
            _uiState.value = LoginUiState.LoggedIn
        }
    }

    // -------------------------
    // CALLBACK DO BIOMETRIC PROMPT
    // -------------------------
    fun onBiometricSuccess() {
        _uiState.value = LoginUiState.LoggedIn
    }

    fun onBiometricFailed() {
        _uiState.value = LoginUiState.Error("Falha na biometria")
    }
}