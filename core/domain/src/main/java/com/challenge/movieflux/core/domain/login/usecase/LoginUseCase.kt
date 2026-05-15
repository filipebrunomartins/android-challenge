package com.challenge.movieflux.core.domain.login.usecase

import com.challenge.movieflux.core.domain.login.repository.LoginRepository
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val repository: LoginRepository
) {
    operator fun invoke(username: String, password: String): Boolean {
        val savedUser = repository.getSessionUser()

        if (!savedUser.isNullOrBlank() && savedUser != username) {
            repository.setBiometricEnabled(false)
            repository.logout()
        }

        val success = repository.login(username, password)

        if (success) {
            repository.saveSession(username)
        }

        return success
    }
}
