package com.challenge.movieflux.core.data.repository

import com.challenge.movieflux.core.security.SecurePrefs
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class LoginRepositoryImplTest {

    private lateinit var securePrefs: SecurePrefs
    private lateinit var repository: LoginRepositoryImpl

    @Before
    fun setup() {
        securePrefs = mockk(relaxed = true)
        repository = LoginRepositoryImpl(securePrefs)
    }

    @Test
    fun `login with admin credentials should return true and save session`() {
        // When
        val result = repository.login("admin", "1234")

        // Then
        assertTrue(result)
        verify { securePrefs.saveSession("admin") }
    }

    @Test
    fun `login with user credentials should return true and save session`() {
        // When
        val result = repository.login("user", "1234")

        // Then
        assertTrue(result)
        verify { securePrefs.saveSession("user") }
    }

    @Test
    fun `login with invalid credentials should return false and not save session`() {
        // When
        val result = repository.login("wrong", "wrong")

        // Then
        assertFalse(result)
        verify(exactly = 0) { securePrefs.saveSession(any()) }
    }

    @Test
    fun `logout should call securePrefs logout`() {
        // When
        repository.logout()

        // Then
        verify { securePrefs.logout() }
    }

    @Test
    fun `isLoggedIn should return value from securePrefs`() {
        // Given
        every { securePrefs.isLoggedIn() } returns true

        // When
        val result = repository.isLoggedIn()

        // Then
        assertTrue(result)
        verify { securePrefs.isLoggedIn() }
    }

    @Test
    fun `getSessionUser should return value from securePrefs`() {
        // Given
        val expectedUser = "admin"
        every { securePrefs.getSessionUser() } returns expectedUser

        // When
        val result = repository.getSessionUser()

        // Then
        assertEquals(expectedUser, result)
        verify { securePrefs.getSessionUser() }
    }
}
