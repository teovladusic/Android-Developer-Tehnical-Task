package com.prizma_distribucija.androiddevelopertask.feature_login.data.repository

import com.google.common.truth.Truth.assertThat
import com.google.gson.Gson
import com.prizma_distribucija.androiddevelopertask.core.util.TestDispatchers
import com.prizma_distribucija.androiddevelopertask.feature_login.data.remote.AuthApiService
import com.prizma_distribucija.androiddevelopertask.feature_login.data.remote.dto.RegisterResponseDto
import com.prizma_distribucija.androiddevelopertask.feature_login.data.remote.dto.UserDto
import com.prizma_distribucija.androiddevelopertask.feature_login.domain.SignUpRepository
import com.prizma_distribucija.androiddevelopertask.feature_login.domain.model.AuthResponse
import com.prizma_distribucija.androiddevelopertask.feature_login.domain.model.SignUpUserData
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
class SignUpRepositoryImplTests {

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
        .create(AuthApiService::class.java)

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

    private val gson = Gson()

    private lateinit var repository: SignUpRepository

    @Before
    fun setUp() {
        repository = SignUpRepositoryImpl(api, testDispatchers)
    }

    @Test
    fun `registerUser, response is successful, should return correct authResponse`() = runTest {
        val token = "random_token"
        val password = "password"
        val name = "name"
        val email = "email@random.com"
        val userDto = UserDto("avatar", name, email)
        val apiResponse = RegisterResponseDto(token, userDto)
        val jsonResponse = gson.toJson(apiResponse)

        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setBody(jsonResponse)
        )

        val userData = SignUpUserData(name, password, "fullName")

        val expected = AuthResponse(token, true, null)
        val actual = repository.registerUser(userData)

        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun `registerUser, response is failed with 400, should return correct authResponse`() = runTest {

        val apiResponse = RegisterResponseDto(null, null)

        val jsonResponse = gson.toJson(apiResponse)

        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(400)
                .setBody(jsonResponse)
        )

        val email = "email@random.com"
        val password = "password"
        val fullName = "fullName"
        val userData = SignUpUserData(email, password, fullName)

        val expected = AuthResponse(null, false, "")
        val actual = repository.registerUser(userData)

        assertThat(actual.isSuccessful).isEqualTo(expected.isSuccessful)
        assertThat(actual.token).isEqualTo(expected.token)
        assertThat(actual.errorMessage).isNotNull()
    }
}