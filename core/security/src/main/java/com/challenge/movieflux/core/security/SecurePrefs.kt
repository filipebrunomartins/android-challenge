package com.challenge.movieflux.core.security

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey

class SecurePrefs(context: Context) {

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

    fun saveUser(username: String, password: String) {
        prefs.edit().apply {
            putString("user", username)
            putString("pass", password)
            apply()
        }
    }

    fun getUser(): String? = prefs.getString("user", null)

    fun getPassword(): String? = prefs.getString("pass", null)

    fun setBiometricEnabled(enabled: Boolean) {
        prefs.edit().putBoolean("biometric", enabled).apply()
    }

    fun isBiometricEnabled(): Boolean {
        return prefs.getBoolean("biometric", false)
    }
}