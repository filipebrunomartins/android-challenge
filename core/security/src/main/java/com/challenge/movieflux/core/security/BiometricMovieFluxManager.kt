package com.challenge.movieflux.core.security

import android.content.Context

class BiometricMovieFluxManager(
    private val context: Context
) {

    fun canAuthenticate(): Boolean {

        val biometricManager =
            androidx.biometric.BiometricManager.from(context)

        val result = biometricManager.canAuthenticate(
            androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG
        )

        return result ==
                androidx.biometric.BiometricManager.BIOMETRIC_SUCCESS
    }
}