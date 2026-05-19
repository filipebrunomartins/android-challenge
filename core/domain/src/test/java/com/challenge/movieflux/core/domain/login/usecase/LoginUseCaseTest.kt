package com.challenge.movieflux.core.domain.login.usecase

import com.challenge.movieflux.core.domain.login.repository.LoginRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class LoginUseCaseTest {

    private val repository: LoginRepository = mockk(relaxed = true)
    private val useCase = LoginUseCase(repository)

    @Test
    fun `invoke should return true when login is successful`() {
        // Given
        val username = "admin"
        val password = "123"
        every { repository.login(username, password) } returns true

        // When
        val result = useCase(username, password)

        // Then
        assertTrue(result)
        verify { repository.login(username, password) }
        verify { repository.saveSession(username) }
    }

    @Test
    fun `invoke should return false when login fails`() {
        // Given
        val username = "wrong"
        val password = "wrong"
        every { repository.login(username, password) } returns false

        // When
        val result = useCase(username, password)

        // Then
        assertFalse(result)
        verify { repository.login(username, password) }
        verify(exactly = 0) { repository.saveSession(any()) }
    }

    @Test
    fun `invoke should disable biometric and logout when user changes`() {
        // Given
        val username = "new_user"
        val password = "123"
        every { repository.getSessionUser() } returns "old_user"
        every { repository.login(username, password) } returns true

        // When
        useCase(username, password)

        // Then
        verify { repository.setBiometricEnabled(false) }
        verify { repository.logout() }
        verify { repository.login(username, password) }
    }
}
