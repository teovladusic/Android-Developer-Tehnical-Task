package com.prizma_distribucija.androiddevelopertask.feature_feed.data.repository

import com.google.common.truth.Truth.assertThat
import com.google.gson.Gson
import com.prizma_distribucija.androiddevelopertask.core.util.TestDispatchers
import com.prizma_distribucija.androiddevelopertask.feature_feed.data.remote.FeedApiService
import com.prizma_distribucija.androiddevelopertask.feature_feed.data.remote.dto.PostDto
import com.prizma_distribucija.androiddevelopertask.feature_feed.domain.CreatePostRepository
import com.prizma_distribucija.androiddevelopertask.feature_feed.domain.model.Post
import com.prizma_distribucija.androiddevelopertask.feature_feed.domain.model.PostMapper
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

@ExperimentalCoroutinesApi
class CreatePostRepositoryImplTests {

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

    lateinit var repository: CreatePostRepository

    private val postMapper = PostMapper()

    @Before
    fun setUp() {
        repository = CreatePostRepositoryImpl(testDispatchers, api, postMapper)
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

    @Test
    fun `createPost, api response is success, should return correct data`() = runTest {
        val postDto = PostDto(1)
        val post = Post(1)

        val jsonResponse = gson.toJson(postDto)

        mockWebServer.enqueue(MockResponse()
            .setResponseCode(200)
            .setBody(jsonResponse)
        )

        val body = MultipartBody.Part.createFormData("upload", "filename")

        val createPostResponse = repository.createPost(body)

        assertThat(createPostResponse.data).isEqualTo(post)
        assertThat(createPostResponse.isSuccessful).isTrue()
        assertThat(createPostResponse.errorMessage).isNull()
    }

    @Test
    fun `createPost, api response is failure, should return correct data`() = runTest {
        mockWebServer.enqueue(MockResponse()
            .setResponseCode(400)
        )

        val body = MultipartBody.Part.createFormData("upload", "filename")

        val createPostResponse = repository.createPost(body)

        assertThat(createPostResponse.data).isNull()
        assertThat(createPostResponse.isSuccessful).isFalse()
        assertThat(createPostResponse.errorMessage).isNotNull()
    }
}