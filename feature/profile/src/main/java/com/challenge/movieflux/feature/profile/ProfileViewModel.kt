package com.challenge.movieflux.feature.profile

import androidx.lifecycle.ViewModel
import com.challenge.movieflux.core.domain.login.usecase.ClearSessionUseCase
import com.challenge.movieflux.core.domain.login.usecase.LogoutUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val clearSessionUseCase: ClearSessionUseCase,
    private val logoutUseCase: LogoutUseCase
) : ViewModel() {

    fun logout() {
        logoutUseCase()
    }

    fun clearSession() {
        clearSessionUseCase()
    }
}
