package com.challenge.movieflux.core.data.repository

import com.challenge.movieflux.core.domain.login.repository.LoginRepository
import com.challenge.movieflux.core.security.SecurePrefs
import javax.inject.Inject

class LoginRepositoryImpl @Inject constructor(
    private val securePrefs: SecurePrefs
) : LoginRepository {

    override fun login(username: String, password: String): Boolean {
        val isValid = username == "admin" && password == "1234"

        if (isValid) {
            saveSession(username)
        }

        return isValid
    }

    override fun saveSession(username: String) {
        securePrefs.saveSession(username)
    }

    override fun clearSession() {
        //Todo refazer
        securePrefs.saveSession("")
    }

    override fun isLoggedIn(): Boolean {
        return securePrefs.getSessionUser().isNullOrBlank().not()
    }

    override fun setBiometricEnabled(enabled: Boolean) {
        securePrefs.setBiometricEnabled(enabled)
    }

    override fun isBiometricEnabled(): Boolean {
        return securePrefs.isBiometricEnabled()
    }
}