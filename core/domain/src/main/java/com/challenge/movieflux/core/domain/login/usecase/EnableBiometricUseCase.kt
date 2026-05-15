package com.challenge.movieflux.core.domain.login.usecase

import com.challenge.movieflux.core.domain.login.repository.LoginRepository
import javax.inject.Inject

class EnableBiometricUseCase @Inject constructor(
    private val repository: LoginRepository
) {
    operator fun invoke() {
        repository.setBiometricEnabled(true)
    }
}