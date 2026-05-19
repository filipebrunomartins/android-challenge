package com.challenge.movieflux.feature.login

import app.cash.turbine.test
import com.challenge.movieflux.core.domain.login.usecase.CheckSessionUseCase
import com.challenge.movieflux.core.domain.login.usecase.EnableBiometricUseCase
import com.challenge.movieflux.core.domain.login.usecase.IsBiometricEnabledUseCase
import com.challenge.movieflux.core.domain.login.usecase.LoginUseCase
import com.challenge.movieflux.core.domain.login.usecase.SetLoggedInUseCase
import com.challenge.movieflux.core.security.BiometricMovieFluxManager
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class LoginViewModelTest {

    private val loginUseCase: LoginUseCase = mockk()
    private val checkSessionUseCase: CheckSessionUseCase = mockk()
    private val isBiometricEnabledUseCase: IsBiometricEnabledUseCase = mockk()
    private val enableBiometricUseCase: EnableBiometricUseCase = mockk()
    private val setLoggedInUseCase: SetLoggedInUseCase = mockk()
    private val biometricManager: BiometricMovieFluxManager = mockk()

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        
        // Default mocks for init block
        coEvery { checkSessionUseCase() } returns false
        coEvery { isBiometricEnabledUseCase() } returns false
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    private fun createViewModel() = LoginViewModel(
        loginUseCase,
        checkSessionUseCase,
        isBiometricEnabledUseCase,
        enableBiometricUseCase,
        setLoggedInUseCase,
        biometricManager
    )

    @Test
    fun `init should set state to LoggedIn if session is active`() = runTest {
        coEvery { checkSessionUseCase() } returns true
        
        val viewModel = createViewModel()
        
        viewModel.uiState.test {
            assertEquals(LoginUiState.Initial, awaitItem())
            testDispatcher.scheduler.advanceUntilIdle()
            assertEquals(LoginUiState.Loading, awaitItem())
            assertEquals(LoginUiState.LoggedIn, awaitItem())
        }
    }

    @Test
    fun `login success without biometric hardware should set state to LoggedIn`() = runTest {
        val username = "admin"
        val password = "123"
        coEvery { loginUseCase(username, password) } returns true
        coEvery { biometricManager.canAuthenticate() } returns false
        
        val viewModel = createViewModel()
        testDispatcher.scheduler.advanceUntilIdle()
        
        viewModel.login(username, password)
        
        viewModel.uiState.test {
            // After advanceUntilIdle in setup, current state is Initial (from checkInitialState branch)
            assertEquals(LoginUiState.Initial, awaitItem())
            assertEquals(LoginUiState.Loading, awaitItem())
            assertEquals(LoginUiState.LoggedIn, awaitItem())
        }
    }

    @Test
    fun `login success with biometric hardware should ask to enable biometric`() = runTest {
        val username = "admin"
        val password = "123"
        coEvery { loginUseCase(username, password) } returns true
        coEvery { biometricManager.canAuthenticate() } returns true
        
        val viewModel = createViewModel()
        testDispatcher.scheduler.advanceUntilIdle()
        
        viewModel.login(username, password)
        
        viewModel.uiState.test {
            assertEquals(LoginUiState.Initial, awaitItem())
            assertEquals(LoginUiState.Loading, awaitItem())
            assertEquals(LoginUiState.AskToEnableBiometric, awaitItem())
        }
    }

    @Test
    fun `login failure should set error state`() = runTest {
        val username = "wrong"
        val password = "wrong"
        coEvery { loginUseCase(username, password) } returns false
        
        val viewModel = createViewModel()
        testDispatcher.scheduler.advanceUntilIdle()
        
        viewModel.login(username, password)
        
        viewModel.uiState.test {
            assertEquals(LoginUiState.Initial, awaitItem())
            assertEquals(LoginUiState.Loading, awaitItem())
            val errorState = awaitItem()
            assertTrue(errorState is LoginUiState.Error)
            assertEquals("Usuário ou senha inválidos", (errorState as LoginUiState.Error).message)
        }
    }

    @Test
    fun `onBiometricSuccess should call useCases and set state to LoggedIn`() = runTest {
        coEvery { enableBiometricUseCase() } returns Unit
        coEvery { setLoggedInUseCase(true) } returns Unit
        
        val viewModel = createViewModel()
        testDispatcher.scheduler.advanceUntilIdle()
        
        viewModel.onBiometricSuccess()
        testDispatcher.scheduler.advanceUntilIdle()
        
        assertEquals(LoginUiState.LoggedIn, viewModel.uiState.value)
        coVerify { enableBiometricUseCase() }
        coVerify { setLoggedInUseCase(true) }
    }
}
