package com.prizma_distribucija.androiddevelopertask.feature_login.domain

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.prizma_distribucija.androiddevelopertask.core.util.DispatcherProvider
import com.prizma_distribucija.androiddevelopertask.core.util.TestDispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock

@ExperimentalCoroutinesApi
class AuthenticationHelperTests {

    @Test
    fun `dataStore token is empty, shouldn't be authenticated`() = runTest {
        val dataStoreManager = mock(DataStoreManager::class.java)
        `when`(dataStoreManager.authToken).thenReturn(flowOf(""))

        val testDispatchers = TestDispatchers() as DispatcherProvider

        val authHelper = AuthenticationHelperImpl(dataStoreManager, testDispatchers)

        authHelper.isAuthenticated.test {
            val isAuthenticated = awaitItem()
            assertThat(isAuthenticated).isFalse()
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `dataStore token is not empty, should be authenticated`() = runTest {
        val dataStoreManager = mock(DataStoreManager::class.java)
        `when`(dataStoreManager.authToken).thenReturn(flowOf("random_token"))

        val testDispatchers = TestDispatchers() as DispatcherProvider

        val authHelper = AuthenticationHelperImpl(dataStoreManager, testDispatchers)

        authHelper.isAuthenticated.test {
            val isAuthenticated = awaitItem()
            assertThat(isAuthenticated).isTrue()
            cancelAndIgnoreRemainingEvents()
        }
    }
}