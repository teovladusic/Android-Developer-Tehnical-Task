package com.prizma_distribucija.androiddevelopertask.feature_feed.data.repository

import com.google.common.truth.Truth
import com.google.gson.Gson
import com.prizma_distribucija.androiddevelopertask.core.util.TestDispatchers
import com.prizma_distribucija.androiddevelopertask.feature_feed.data.remote.FeedApiService
import com.prizma_distribucija.androiddevelopertask.feature_feed.data.remote.dto.AuthorDto
import com.prizma_distribucija.androiddevelopertask.feature_feed.data.remote.dto.FeedDto
import com.prizma_distribucija.androiddevelopertask.feature_feed.data.remote.dto.VideoDto
import com.prizma_distribucija.androiddevelopertask.feature_feed.domain.FeedRepository
import com.prizma_distribucija.androiddevelopertask.feature_feed.domain.ProfileRepository
import com.prizma_distribucija.androiddevelopertask.feature_feed.domain.model.*
import com.prizma_distribucija.androiddevelopertask.feature_feed.domain.model.mapper.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

@ExperimentalCoroutinesApi
class FeedRepositoryTests {

    private val testDispatchers = TestDispatchers()

    private val mockWebServer = MockWebServer()

    private val client = OkHttpClient.Builder()
        .connectTimeout(1, TimeUnit.SECONDS)
        .readTimeout(1, TimeUnit.SECONDS)
        .writeTimeout(1, TimeUnit.SECONDS)
        .build()

    private val api = Retrofit.Builder()
        .baseUrl(mockWebServer.url("/"))
        .addConverterFactory(GsonConverterFactory.create())
        .client(client)
        .build()
        .create(FeedApiService::class.java)

    private val gson = Gson()

    private lateinit var repository: FeedRepository

    private val authorMapper = AuthorMapper()
    private val videoMapper = VideoMapper()
    private val feedMapper = FeedMapper(authorMapper, videoMapper)

    @Before
    fun setUp() {
        repository = FeedRepositoryImpl(api, testDispatchers, feedMapper)
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

    @Test
    fun `getFeed response is successful, should return correct response`() = runTest {
        val feedDto = FeedDto(
            id = 1,
            author = AuthorDto(1, "name"),
            video =  VideoDto("handler", "url", 1),
            description = "description"
        )

        val feed = feedMapper.mapFromDto(feedDto)

        val jsonResponse = gson.toJson(listOf(feed))

        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setBody(jsonResponse)
        )

        val response = repository.getFeed()

        val expected = FeedResponse(true, listOf(feed), null)

        Truth.assertThat(response).isEqualTo(expected)
    }

    @Test
    fun `getFeed response is failure, should return correct response`() = runTest {
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(400)
        )

        val response = repository.getFeed()

        val expected = FeedResponse(false, null, "Client Error")

        Truth.assertThat(response).isEqualTo(expected)
    }
}