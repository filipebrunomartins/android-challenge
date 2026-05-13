package com.challenge.movieflux.core.security

import android.content.Context

class BiometricMovieFluxManager(
    private val context: Context
) {

    fun canAuthenticate(): Boolean {
        val manager = androidx.biometric.BiometricManager.from(context)

        return manager.canAuthenticate(
            androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG
        ) == androidx.biometric.BiometricManager.BIOMETRIC_SUCCESS
    }
}