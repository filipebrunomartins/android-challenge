package com.challenge.movieflux.feature.profile

import androidx.lifecycle.ViewModel
import com.challenge.movieflux.core.domain.login.usecase.ClearSessionUseCase
import com.challenge.movieflux.core.domain.login.usecase.DisableBiometricUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val clearSessionUseCase: ClearSessionUseCase,
    private val disableBiometricUseCase: DisableBiometricUseCase
) : ViewModel() {

    fun logout() {
        clearSessionUseCase()
        disableBiometricUseCase()
    }

    fun clearSession() {
        clearSessionUseCase()
    }
}
