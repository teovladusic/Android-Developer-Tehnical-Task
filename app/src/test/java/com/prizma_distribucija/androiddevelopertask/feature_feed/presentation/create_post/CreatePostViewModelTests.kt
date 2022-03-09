package com.prizma_distribucija.androiddevelopertask.feature_feed.presentation.create_post

import app.cash.turbine.test
import com.google.common.truth.Truth
import com.prizma_distribucija.androiddevelopertask.core.util.TestDispatchers
import com.prizma_distribucija.androiddevelopertask.feature_feed.domain.use_cases.CreatePostUseCase
import com.prizma_distribucija.androiddevelopertask.feature_feed.presentation.feed.FeedViewModel
import com.prizma_distribucija.androiddevelopertask.feature_login.domain.AuthenticationHelper
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.mockito.Mockito
import org.mockito.Mockito.mock

@ExperimentalCoroutinesApi
class CreatePostViewModelTests {

    @Test
    fun `isAuthenticated, should return false`() = runTest {
        val authHelper = Mockito.mock(AuthenticationHelper::class.java)
        Mockito.`when`(authHelper.isAuthenticated).thenReturn(MutableStateFlow(false).asStateFlow())

        val dispatcherProvider = TestDispatchers()
        val createPostUseCase = mock(CreatePostUseCase::class.java)

        val viewModel = CreatePostViewModel(dispatcherProvider, createPostUseCase, authHelper)

        viewModel.isAuthenticated.test {
            val isAuthenticated = awaitItem()
            Truth.assertThat(isAuthenticated).isFalse()
        }
    }

    @Test
    fun `isAuthenticated, should return true`() = runTest {
        val authHelper = Mockito.mock(AuthenticationHelper::class.java)
        Mockito.`when`(authHelper.isAuthenticated).thenReturn(MutableStateFlow(true).asStateFlow())

        val dispatcherProvider = TestDispatchers()
        val createPostUseCase = mock(CreatePostUseCase::class.java)

        val viewModel = CreatePostViewModel(dispatcherProvider, createPostUseCase, authHelper)

        viewModel.isAuthenticated.test {
            val isAuthenticated = awaitItem()
            Truth.assertThat(isAuthenticated).isTrue()
        }
    }
}