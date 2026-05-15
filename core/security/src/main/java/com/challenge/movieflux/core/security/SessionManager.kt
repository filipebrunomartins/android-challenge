package com.challenge.movieflux.core.security

import javax.inject.Inject

class SessionManager @Inject constructor(
    private val securePrefs: SecurePrefs
) {

    fun login(
        username: String,
        password: String
    ): Boolean {

        val success =
            username == "admin" &&
                    password == "1234"

        if (success) {
            securePrefs.saveSession(username)
        }

        return success
    }

    fun logout() {
        securePrefs.logout()
    }

    fun isLoggedIn(): Boolean {
        return securePrefs.isLoggedIn()
    }

    fun enableBiometric(username: String) {
        securePrefs.setBiometricEnabled(true)
    }

    fun disableBiometric() {
        securePrefs.setBiometricEnabled(false)
    }

    fun isBiometricEnabled(): Boolean {
        return securePrefs.isBiometricEnabled()
    }
}