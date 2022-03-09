package com.prizma_distribucija.androiddevelopertask.feature_feed.domain.use_cases

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.prizma_distribucija.androiddevelopertask.core.Resource
import com.prizma_distribucija.androiddevelopertask.feature_feed.domain.FeedRepository
import com.prizma_distribucija.androiddevelopertask.feature_feed.domain.model.Author
import com.prizma_distribucija.androiddevelopertask.feature_feed.domain.model.Feed
import com.prizma_distribucija.androiddevelopertask.feature_feed.domain.model.FeedResponse
import com.prizma_distribucija.androiddevelopertask.feature_feed.domain.model.Video
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock

@ExperimentalCoroutinesApi
class GetFeedUseCaseTests {

    @Test
    fun `invoke, response is successful, should emit loading and success with correct data`() =
        runTest {
            val feed = Feed(1, Author(1, "name"), Video("handler", "url", 1), "description")
            val feedResponse =
                FeedResponse(isSuccessful = true, data = listOf(feed), errorMessage = null)
            val repository = mock(FeedRepository::class.java)
            `when`(repository.getFeed()).thenReturn(feedResponse)

            val useCase = GetFeedUseCase(repository)

            useCase().test {
                val firstEmission = awaitItem()
                assertThat(firstEmission).isInstanceOf(Resource.Loading::class.java)
                val secondEmission = awaitItem()
                assertThat(secondEmission).isInstanceOf(Resource.Success::class.java)
                assertThat(secondEmission.data!![0]).isEqualTo(feed)

                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun `invoke, response is failure, should emit loading and success with correct data`() =
        runTest {
            val feedResponse =
                FeedResponse(isSuccessful = false, data = null, errorMessage = "error message")
            val repository = mock(FeedRepository::class.java)
            `when`(repository.getFeed()).thenReturn(feedResponse)

            val useCase = GetFeedUseCase(repository)

            useCase().test {
                val firstEmission = awaitItem()
                assertThat(firstEmission).isInstanceOf(Resource.Loading::class.java)
                val secondEmission = awaitItem()
                assertThat(secondEmission).isInstanceOf(Resource.Error::class.java)
                assertThat(secondEmission.data).isNull()
                assertThat(secondEmission.message).isEqualTo("error message")

                cancelAndIgnoreRemainingEvents()
            }
        }
}