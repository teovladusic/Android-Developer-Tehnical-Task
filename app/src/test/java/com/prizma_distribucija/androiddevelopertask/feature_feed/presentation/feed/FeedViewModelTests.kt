package com.prizma_distribucija.androiddevelopertask.feature_feed.presentation.feed

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.prizma_distribucija.androiddevelopertask.feature_login.domain.AuthenticationHelper
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock

@ExperimentalCoroutinesApi
class FeedViewModelTests {

    @Test
    fun `isAuthenticated, should return false`() = runTest {
        val authHelper = mock(AuthenticationHelper::class.java)
        `when`(authHelper.isAuthenticated).thenReturn(MutableStateFlow(false).asStateFlow())

        val viewModel = FeedViewModel(authHelper)

        viewModel.isAuthenticated.test {
            val isAuthenticated = awaitItem()
            assertThat(isAuthenticated).isFalse()
        }
    }

    @Test
    fun `isAuthenticated, should return true`() = runTest {
        val authHelper = mock(AuthenticationHelper::class.java)
        `when`(authHelper.isAuthenticated).thenReturn(MutableStateFlow(true).asStateFlow())

        val viewModel = FeedViewModel(authHelper)

        viewModel.isAuthenticated.test {
            val isAuthenticated = awaitItem()
            assertThat(isAuthenticated).isTrue()
        }
    }
}