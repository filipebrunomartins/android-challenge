package com.challenge.movieflux.core.domain.login.repository

interface LoginRepository {

    fun login(username: String, password: String): Boolean

    fun saveSession(username: String)

    fun clearSession()

    fun isLoggedIn(): Boolean

    fun setBiometricEnabled(enabled: Boolean)

    fun isBiometricEnabled(): Boolean
}