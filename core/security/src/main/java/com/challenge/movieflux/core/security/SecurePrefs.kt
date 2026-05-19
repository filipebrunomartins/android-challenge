package com.challenge.movieflux.core.security

import android.content.SharedPreferences
import javax.inject.Inject
import androidx.core.content.edit

class SecurePrefs @Inject constructor(
    private val prefs: SharedPreferences
) {

    fun saveSession(username: String) {
        prefs.edit {
            putString("session_user", username)
            putBoolean("logged_in", true)
        }
    }

    fun logout() {
        prefs.edit {
            remove("session_user")
            putBoolean("logged_in", false)
        }
    }

    fun setLoggedIn(loggedIn: Boolean) {
        prefs.edit {
            putBoolean("logged_in", loggedIn)
        }
    }

    fun clearSession() {
        prefs.edit {
            putBoolean("logged_in", false)
        }
    }

    fun getSessionUser(): String? {
        return prefs.getString("session_user", null)
    }

    fun isLoggedIn(): Boolean {
        return prefs.getBoolean("logged_in", false)
    }

    fun setBiometricEnabled(enabled: Boolean) {
        prefs.edit {
            putBoolean("biometric_enabled", enabled)
        }
    }

    fun isBiometricEnabled(): Boolean {
        return prefs.getBoolean("biometric_enabled", false)
    }
}
