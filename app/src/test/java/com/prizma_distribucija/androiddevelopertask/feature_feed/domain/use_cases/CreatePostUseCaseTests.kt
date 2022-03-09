package com.prizma_distribucija.androiddevelopertask.feature_feed.domain.use_cases

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.prizma_distribucija.androiddevelopertask.core.Resource
import com.prizma_distribucija.androiddevelopertask.feature_feed.domain.CreatePostRepository
import com.prizma_distribucija.androiddevelopertask.feature_feed.domain.model.CreatePostResponse
import com.prizma_distribucija.androiddevelopertask.feature_feed.domain.model.Post
import com.prizma_distribucija.androiddevelopertask.feature_feed.fakes.CreatePostRepositoryFakeImpl
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.mockito.Mockito.*
import java.io.File

@ExperimentalCoroutinesApi
class CreatePostUseCaseTests {

    @Test
    fun `response is successful, should emit loading and success with correct data`() = runTest {
        CreatePostRepositoryFakeImpl.isSuccessful = true
        CreatePostRepositoryFakeImpl.delayTime = 0L
        val repository = CreatePostRepositoryFakeImpl()
        val data = Post(0)

        val useCase = CreatePostUseCase(repository)

        val file = mock(File::class.java)

        useCase(file).test {
            val firstEmission = awaitItem()
            assertThat(firstEmission).isInstanceOf(Resource.Loading::class.java)
            val secondEmission = awaitItem()
            assertThat(secondEmission).isInstanceOf(Resource.Success::class.java)
            assertThat(secondEmission.data).isEqualTo(data)
            assertThat(secondEmission.message).isNull()

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `response is failure, should emit loading and error with correct data`() = runTest {
        CreatePostRepositoryFakeImpl.isSuccessful = false
        CreatePostRepositoryFakeImpl.delayTime = 0L
        val repository = CreatePostRepositoryFakeImpl()

        val useCase = CreatePostUseCase(repository)

        val file = mock(File::class.java)

        useCase(file).test {
            val firstEmission = awaitItem()
            assertThat(firstEmission).isInstanceOf(Resource.Loading::class.java)
            val secondEmission = awaitItem()
            assertThat(secondEmission).isInstanceOf(Resource.Error::class.java)
            assertThat(secondEmission.message).isEqualTo("error message")
            assertThat(secondEmission.data).isNull()

            cancelAndIgnoreRemainingEvents()
        }
    }
}