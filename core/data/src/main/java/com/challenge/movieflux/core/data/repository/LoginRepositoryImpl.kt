package com.challenge.movieflux.core.data.repository

import com.challenge.movieflux.core.domain.login.repository.LoginRepository
import com.challenge.movieflux.core.security.SecurePrefs
import javax.inject.Inject

class LoginRepositoryImpl @Inject constructor(
    private val securePrefs: SecurePrefs
) : LoginRepository {

    override fun login(username: String, password: String): Boolean {
        //Todo validar com 2 logins diferentes
        val isValid = (username == "admin" && password == "1234") ||
                (username == "user" && password == "1234")

        if (isValid) {
            saveSession(username)
        }

        return isValid
    }

    //Todo salva somente username?
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