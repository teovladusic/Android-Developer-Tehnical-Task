package com.prizma_distribucija.androiddevelopertask.feature_login.presentation.login

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.google.gson.Gson
import com.prizma_distribucija.androiddevelopertask.core.Resource
import com.prizma_distribucija.androiddevelopertask.core.util.TestDispatchers
import com.prizma_distribucija.androiddevelopertask.feature_login.data.remote.AuthApiService
import com.prizma_distribucija.androiddevelopertask.feature_login.data.remote.dto.LoginResponseDto
import com.prizma_distribucija.androiddevelopertask.feature_login.data.repository.LoginRepositoryImpl
import com.prizma_distribucija.androiddevelopertask.feature_login.domain.DataStoreManager
import com.prizma_distribucija.androiddevelopertask.feature_login.domain.LoginRepository
import com.prizma_distribucija.androiddevelopertask.feature_login.domain.model.LoginUserData
import com.prizma_distribucija.androiddevelopertask.feature_login.domain.use_cases.LoginUserUseCase
import com.prizma_distribucija.androiddevelopertask.feature_login.domain.use_cases.RegisterUserUseCase
import com.prizma_distribucija.androiddevelopertask.feature_login.fakes.DataStoreManagerFakeImpl
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

@ExperimentalCoroutinesApi
class LoginViewModelTests {

    lateinit var viewModel: LoginViewModel


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

    private lateinit var dataStoreManager: DataStoreManager

    @Before
    fun setUp() {
        val dispatcherProvider = TestDispatchers()
        val loginRepository = LoginRepositoryImpl(api, dispatcherProvider) as LoginRepository
        val loginUserUseCase = LoginUserUseCase(loginRepository)
        dataStoreManager = DataStoreManagerFakeImpl()
        viewModel = LoginViewModel(dispatcherProvider, loginUserUseCase, dataStoreManager)
    }

    @Test
    fun `onLoginClicked, email and password empty, should send ShowMessageEvent`() = runTest {
        val email = ""
        val password = ""
        val loginUserData = LoginUserData(email, password)

        viewModel.onLoginClicked(loginUserData)

        viewModel.loginEvents.test {
            val event = awaitItem()
            assertThat(event).isInstanceOf(LoginViewModel.LoginEvents.ShowMessage::class.java)
        }
    }

    @Test
    fun `onLoginClicked, email is empty, should send ShowMessageEvent`() = runTest {
        val email = ""
        val password = "password"
        val loginUserData = LoginUserData(email, password)

        viewModel.onLoginClicked(loginUserData)

        viewModel.loginEvents.test {
            val event = awaitItem()
            assertThat(event).isInstanceOf(LoginViewModel.LoginEvents.ShowMessage::class.java)
        }
    }

    @Test
    fun `onLoginClicked, password is empty, should send ShowMessageEvent`() = runTest {
        val email = "email@random.com"
        val password = ""
        val loginUserData = LoginUserData(email, password)

        viewModel.onLoginClicked(loginUserData)

        viewModel.loginEvents.test {
            val event = awaitItem()
            assertThat(event).isInstanceOf(LoginViewModel.LoginEvents.ShowMessage::class.java)
        }
    }

    @Test
    fun `onLoginClicked, data is valid, should login and emit status`() = runBlocking {
        val email = "email@random.com"
        val password = "password"
        val loginUserData = LoginUserData(email, password)

        val token = "token"
        val apiResponse = LoginResponseDto(token)
        val jsonResponse = Gson().toJson(apiResponse)

        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setBody(jsonResponse)
                .setBodyDelay(500, TimeUnit.MILLISECONDS)
        )

        val job = launch {
            viewModel.loginStatus.test {
                val firstEvent = awaitItem()
                assertThat(firstEvent).isInstanceOf(Resource.Success::class.java)

                cancelAndIgnoreRemainingEvents()
            }
        }

        viewModel.onLoginClicked(loginUserData)

        job.join()
        job.cancel()
    }

    @Test
    fun `onLoginSuccess, should save token to dataStore and emit LoggedInStatus`() = runTest {
        val token = "token"
        viewModel.onLoginSuccess(token)

        viewModel.loginEvents.test {
            val event = awaitItem()
            assertThat(event).isInstanceOf(LoginViewModel.LoginEvents.LoggedIn::class.java)
            cancelAndIgnoreRemainingEvents()
        }

        assertThat(dataStoreManager.authToken.first()).isEqualTo(token)
    }
}