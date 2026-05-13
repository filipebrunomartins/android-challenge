package com.challenge.movieflux.core.domain.login.usecase

import com.challenge.movieflux.core.domain.login.repository.LoginRepository
import javax.inject.Inject

class SaveSessionUseCase @Inject constructor(
    private val repository: LoginRepository
) {
    operator fun invoke(username: String) {
        repository.saveSession(username)
    }
}