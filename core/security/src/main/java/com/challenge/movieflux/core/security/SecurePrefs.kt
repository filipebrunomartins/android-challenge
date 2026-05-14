package com.challenge.movieflux.core.security

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import androidx.core.content.edit

class SecurePrefs @Inject constructor(
    @ApplicationContext context: Context
) {

    private val masterKey = MasterKey.Builder(context)
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        .build()

    private val prefs = EncryptedSharedPreferences.create(
        context,
        "movieflux_secure",
        masterKey,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    fun saveSession(username: String) {

        prefs.edit {
            putString("session_user", username)
                .putBoolean("logged_in", true)
        }
    }

    fun clearSession() {
        prefs.edit {
            clear()
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