package com.challenge.movieflux.core.security

class SessionManager(
    private val securePrefs: SecurePrefs
) {

    fun isLoggedIn(username: String, password: String): Boolean {
        return securePrefs.getUser() == username &&
                securePrefs.getPassword() == password
    }

    fun createSession(username: String, password: String) {
        securePrefs.saveUser(username, password)
    }

    fun isBiometricEnabled(): Boolean {
        return securePrefs.isBiometricEnabled()
    }

    fun enableBiometric() {
        securePrefs.setBiometricEnabled(true)
    }
}