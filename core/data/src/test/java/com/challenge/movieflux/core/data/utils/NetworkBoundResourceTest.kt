package com.challenge.movieflux.core.data.utils

import app.cash.turbine.test
import com.challenge.movieflux.core.common.results.BaseResult
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import retrofit2.Response

@OptIn(ExperimentalCoroutinesApi::class)
class NetworkBoundResourceTest {

    private lateinit var networkBoundResource: NetworkBoundResource
    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setup() {
        networkBoundResource = NetworkBoundResource(testDispatcher)
    }

    @Test
    fun `downloadData should emit Loading then Success when response is successful`() = runTest {
        // Given
        val data = "Success Data"
        val apiCall = suspend { Response.success(data) }

        // When & Then
        networkBoundResource.downloadData(apiCall).test {
            assertEquals(BaseResult.Loading, awaitItem())
            val success = awaitItem()
            assertTrue(success is BaseResult.Success)
            assertEquals(data, (success as BaseResult.Success).data)
            awaitComplete()
        }
    }

    @Test
    fun `downloadData should emit Loading then Error when response is null`() = runTest {
        // Given
        val apiCall = suspend { Response.success<String>(null) }

        // When & Then
        networkBoundResource.downloadData(apiCall).test {
            assertEquals(BaseResult.Loading, awaitItem())
            val error = awaitItem()
            assertTrue(error is BaseResult.Error)
            assertEquals("Ocorreu um erro desconhecido", (error as BaseResult.Error).message)
            awaitComplete()
        }
    }

    @Test
    fun `downloadData should emit Loading then Error when exception occurs`() = runTest {
        // Given
        val apiCall: suspend () -> Response<String> = { throw java.io.IOException("Network Error") }

        // When & Then
        networkBoundResource.downloadData(apiCall).test {
            assertEquals(BaseResult.Loading, awaitItem())
            val error = awaitItem()
            assertTrue(error is BaseResult.Error)
            assertEquals("Ops! Sem conexão com a internet. Tente novamente.", (error as BaseResult.Error).message)
            awaitComplete()
        }
    }
}
