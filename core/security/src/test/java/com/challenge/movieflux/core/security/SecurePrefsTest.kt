package com.challenge.movieflux.core.security

import android.content.SharedPreferences
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class SecurePrefsTest {

    private lateinit var sharedPrefs: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor
    private lateinit var securePrefs: SecurePrefs

    @Before
    fun setup() {
        sharedPrefs = mockk(relaxed = true)
        editor = mockk(relaxed = true)
        
        every { sharedPrefs.edit() } returns editor
        every { editor.putString(any(), any()) } returns editor
        every { editor.putBoolean(any(), any()) } returns editor
        every { editor.remove(any()) } returns editor
        
        securePrefs = SecurePrefs(sharedPrefs)
    }

    @Test
    fun `saveSession should save username and set logged_in to true`() {
        // Given
        val username = "test_user"

        // When
        securePrefs.saveSession(username)

        // Then
        verify { editor.putString("session_user", username) }
        verify { editor.putBoolean("logged_in", true) }
        verify { editor.apply() }
    }

    @Test
    fun `logout should remove username and set logged_in to false`() {
        // When
        securePrefs.logout()

        // Then
        verify { editor.remove("session_user") }
        verify { editor.putBoolean("logged_in", false) }
        verify { editor.apply() }
    }

    @Test
    fun `getSessionUser should return value from sharedPrefs`() {
        // Given
        val username = "admin"
        every { sharedPrefs.getString("session_user", null) } returns username

        // When
        val result = securePrefs.getSessionUser()

        // Then
        assertEquals(username, result)
    }

    @Test
    fun `isLoggedIn should return value from sharedPrefs`() {
        // Given
        every { sharedPrefs.getBoolean("logged_in", false) } returns true

        // When
        val result = securePrefs.isLoggedIn()

        // Then
        assertTrue(result)
    }
}
