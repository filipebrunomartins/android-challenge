package com.challenge.movieflux.feature.login.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.challenge.movieflux.feature.login.LoginScreen
import com.challenge.movieflux.feature.login.LoginViewModel

@Composable
internal fun LoginScreenRoute(
    viewModel: LoginViewModel = hiltViewModel(),
    onBackBtnClick:()->Unit
){
    val loginUiState by viewModel.uiState.collectAsStateWithLifecycle()
    LoginScreen(
        uiState = loginUiState,
        onLoginClick = viewModel::login,
        onEnableBiometricClick = viewModel::enableBiometric,
        onBiometricRetry = viewModel::onBiometricFailed
    )
}