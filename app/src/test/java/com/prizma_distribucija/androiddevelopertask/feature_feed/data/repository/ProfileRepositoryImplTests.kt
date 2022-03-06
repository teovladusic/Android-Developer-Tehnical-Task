package com.prizma_distribucija.androiddevelopertask.feature_feed.data.repository

import com.google.common.truth.Truth.assertThat
import com.google.gson.Gson
import com.prizma_distribucija.androiddevelopertask.core.util.TestDispatchers
import com.prizma_distribucija.androiddevelopertask.feature_feed.data.remote.FeedApiService
import com.prizma_distribucija.androiddevelopertask.feature_feed.data.remote.dto.UserResponseDto
import com.prizma_distribucija.androiddevelopertask.feature_feed.domain.ProfileRepository
import com.prizma_distribucija.androiddevelopertask.feature_feed.domain.model.*
import com.prizma_distribucija.androiddevelopertask.feature_login.data.remote.AuthApiService
import com.prizma_distribucija.androiddevelopertask.feature_login.domain.LoginRepository
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
class ProfileRepositoryImplTests {

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

    private lateinit var repository: ProfileRepository

    private val planMapper = PlanMapper()
    private val athleteMapper = AthleteMapper()
    private val userMapper = UserMapper(athleteMapper, planMapper)

    @Before
    fun setUp() {
        repository = ProfileRepositoryImpl(testDispatchers, api, userMapper)
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

    @Test
    fun `getUser response is successful, should return correct response`() = runTest {
        val plan = Plan("type")
        val athletes = listOf(Athlete(1, "name", "avatar"))
        val user = User(
            id = 1,
            name = "name",
            avatar = "avatar",
            views = "views",
            videoPlays = "videoPlays",
            plan = plan,
            athletes = athletes
        )

        val apiResponse = userMapper.mapToDto(user)
        val jsonResponse = gson.toJson(apiResponse)

        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setBody(jsonResponse)
        )

        val response = repository.getUser(1)

        val expected = GetUserResponse(true, user, null)

        assertThat(response).isEqualTo(expected)
    }

    @Test
    fun `getUser response is failure, should return correct response`() = runTest {
        val apiResponse = UserResponseDto(null, null, null, null, null, null, null)
        val jsonResponse = gson.toJson(apiResponse)

        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(400)
                .setBody(jsonResponse)
        )

        val response = repository.getUser(1)

        assertThat(response.isSuccessful).isFalse()
        assertThat(response.data).isNull()
        assertThat(response.errorMessage).isNotNull()
    }
}