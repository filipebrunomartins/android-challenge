package com.challenge.movieflux.core.security

import android.content.Context
import androidx.biometric.BiometricManager
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.unmockkStatic
import org.junit.After
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class BiometricMovieFluxManagerTest {

    private lateinit var context: Context
    private lateinit var biometricManager: BiometricManager
    private lateinit var manager: BiometricMovieFluxManager

    @Before
    fun setup() {
        context = mockk()
        biometricManager = mockk()
        manager = BiometricMovieFluxManager(context)
        
        mockkStatic(BiometricManager::class)
        every { BiometricManager.from(context) } returns biometricManager
    }

    @After
    fun tearDown() {
        unmockkStatic(BiometricManager::class)
    }

    @Test
    fun `canAuthenticate should return true when BIOMETRIC_SUCCESS`() {
        // Given
        every { 
            biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_STRONG) 
        } returns BiometricManager.BIOMETRIC_SUCCESS

        // When
        val result = manager.canAuthenticate()

        // Then
        assertTrue(result)
    }

    @Test
    fun `canAuthenticate should return false when not successful`() {
        // Given
        every { 
            biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_STRONG) 
        } returns BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE

        // When
        val result = manager.canAuthenticate()

        // Then
        assertFalse(result)
    }
}
