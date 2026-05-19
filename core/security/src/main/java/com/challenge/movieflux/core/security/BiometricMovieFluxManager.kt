package com.challenge.movieflux.core.security

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class BiometricMovieFluxManager @Inject constructor(
    @ApplicationContext private val context: Context
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