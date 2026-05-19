package com.challenge.movieflux.core.domain.login.usecase

import com.challenge.movieflux.core.domain.login.repository.LoginRepository
import javax.inject.Inject

class DisableBiometricUseCase @Inject constructor(
    private val repository: LoginRepository
) {
    operator fun invoke() {
        repository.setBiometricEnabled(false)
    }
}
