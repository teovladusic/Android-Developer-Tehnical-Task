package com.prizma_distribucija.androiddevelopertask.feature_feed.presentation.feed

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.prizma_distribucija.androiddevelopertask.core.Resource
import com.prizma_distribucija.androiddevelopertask.core.util.TestDispatchers
import com.prizma_distribucija.androiddevelopertask.feature_feed.domain.FeedRepository
import com.prizma_distribucija.androiddevelopertask.feature_feed.domain.model.Author
import com.prizma_distribucija.androiddevelopertask.feature_feed.domain.model.Feed
import com.prizma_distribucija.androiddevelopertask.feature_feed.domain.model.FeedResponse
import com.prizma_distribucija.androiddevelopertask.feature_feed.domain.model.Video
import com.prizma_distribucija.androiddevelopertask.feature_feed.domain.use_cases.GetFeedUseCase
import com.prizma_distribucija.androiddevelopertask.feature_login.domain.AuthenticationHelper
import com.prizma_distribucija.androiddevelopertask.feature_login.domain.model.AuthResponse
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.StandardTestDispatcher
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

        val repository = mock(FeedRepository::class.java)
        `when`(repository.getFeed()).thenReturn(FeedResponse(false, null, "error message"))

        val feedUseCase = GetFeedUseCase(repository)
        val dispatcherProvider = TestDispatchers()

        val viewModel = FeedViewModel(authHelper, feedUseCase, dispatcherProvider)

        viewModel.isAuthenticated.test {
            val isAuthenticated = awaitItem()
            assertThat(isAuthenticated).isFalse()
        }
    }

    @Test
    fun `isAuthenticated, should return true`() = runTest {
        val authHelper = mock(AuthenticationHelper::class.java)
        `when`(authHelper.isAuthenticated).thenReturn(MutableStateFlow(true).asStateFlow())

        val repository = mock(FeedRepository::class.java)
        `when`(repository.getFeed()).thenReturn(FeedResponse(false, null, "error message"))

        val feedUseCase = GetFeedUseCase(repository)
        val dispatcherProvider = TestDispatchers()

        val viewModel = FeedViewModel(authHelper, feedUseCase, dispatcherProvider)

        viewModel.isAuthenticated.test {
            val isAuthenticated = awaitItem()
            assertThat(isAuthenticated).isTrue()
        }
    }

    @Test
    fun `init, should emit getFeedStatus`() = runBlocking {
        val authHelper = mock(AuthenticationHelper::class.java)
        `when`(authHelper.isAuthenticated).thenReturn(MutableStateFlow(true).asStateFlow())

        val repository = mock(FeedRepository::class.java)
        `when`(repository.getFeed()).thenReturn(FeedResponse(false, null, "error message"))

        val feedUseCase = GetFeedUseCase(repository)
        val dispatcherProvider = TestDispatchers()

        lateinit var viewModel: FeedViewModel

        val job = launch {
            viewModel.getFeedStatus.test {
                val emission = awaitItem()
                assertThat(emission).isInstanceOf(Resource.Error::class.java)
            }
        }

        viewModel = FeedViewModel(authHelper, feedUseCase, dispatcherProvider)

        job.join()
        job.cancel()
    }

    @Test
    fun `onPageChanged, should emit shouldPlayVideo = true`() = runBlocking {
        val dispatcherProvider = TestDispatchers()
        val authHelper = mock(AuthenticationHelper::class.java)
        `when`(authHelper.isAuthenticated).thenReturn(MutableStateFlow(true).asStateFlow())
        val repository = mock(FeedRepository::class.java)
        `when`(repository.getFeed()).thenReturn(
            FeedResponse(
                isSuccessful = true,
                data = listOf(
                    Feed(
                        1,
                        Author(1, "name"),
                        Video("handler", "url", 1),
                        "description"
                    )
                ),
                null
            )
        )

        val feedUseCase = GetFeedUseCase(repository)
        val viewModel = FeedViewModel(authHelper, feedUseCase, dispatcherProvider)

        val job = launch {
            viewModel.shouldVideoPlay.test {
                val item = awaitItem()
                assertThat(item).isTrue()

                cancelAndIgnoreRemainingEvents()
            }
        }

        viewModel.onPageChanged(0)

        job.join()
        job.cancel()
    }

    @Test
    fun `onVideoClick, should emit opposite value to shouldVideoPlay`() = runBlocking {
        val dispatcherProvider = TestDispatchers()
        val authHelper = mock(AuthenticationHelper::class.java)
        `when`(authHelper.isAuthenticated).thenReturn(MutableStateFlow(true).asStateFlow())
        val repository = mock(FeedRepository::class.java)
        `when`(repository.getFeed()).thenReturn(
            FeedResponse(
                isSuccessful = true,
                data = listOf(
                    Feed(
                        1,
                        Author(1, "name"),
                        Video("handler", "url", 1),
                        "description"
                    )
                ),
                null
            )
        )

        val feedUseCase = GetFeedUseCase(repository)
        val viewModel = FeedViewModel(authHelper, feedUseCase, dispatcherProvider)

        val job1 = launch {
            viewModel.shouldVideoPlay.test {
                assertThat(awaitItem()).isTrue()
            }
        }

        viewModel.onPageChanged(0)

        job1.join()
        job1.cancel()

        val job2 = launch {
            viewModel.shouldVideoPlay.test {
                assertThat(awaitItem()).isFalse()
            }
        }

        viewModel.onVideoClick()

        job2.join()
        job2.cancel()
    }


    @Test
    fun `onPageChanged, should correctly set position`() = runTest {
        val dispatcherProvider = TestDispatchers()
        val authHelper = mock(AuthenticationHelper::class.java)
        `when`(authHelper.isAuthenticated).thenReturn(MutableStateFlow(true).asStateFlow())
        val repository = mock(FeedRepository::class.java)
        `when`(repository.getFeed()).thenReturn(
            FeedResponse(
                isSuccessful = true,
                data = listOf(
                    Feed(
                        1,
                        Author(1, "name"),
                        Video("handler", "url", 1),
                        "description"
                    )
                ),
                null
            )
        )

        val feedUseCase = GetFeedUseCase(repository)
        val viewModel = FeedViewModel(authHelper, feedUseCase, dispatcherProvider)

        viewModel.onPageChanged(243)

        assertThat(viewModel.viewPagerPosition).isEqualTo(243)
    }
}