package com.challenge.movieflux.core.domain.login.repository

interface LoginRepository {
    fun login(username: String, password: String): Boolean
    fun saveSession(username: String)
    fun logout()
    fun clearSession()
    fun isLoggedIn(): Boolean
    fun setLoggedIn(loggedIn: Boolean)
    fun setBiometricEnabled(enabled: Boolean)
    fun isBiometricEnabled(): Boolean
    fun getSessionUser(): String?
}
