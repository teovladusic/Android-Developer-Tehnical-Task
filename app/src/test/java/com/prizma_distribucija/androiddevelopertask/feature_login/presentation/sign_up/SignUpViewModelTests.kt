package com.prizma_distribucija.androiddevelopertask.feature_login.presentation.sign_up

import androidx.lifecycle.ViewModel
import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.google.gson.Gson
import com.prizma_distribucija.androiddevelopertask.core.Resource
import com.prizma_distribucija.androiddevelopertask.core.util.TestDispatchers
import com.prizma_distribucija.androiddevelopertask.feature_login.data.remote.AuthApiService
import com.prizma_distribucija.androiddevelopertask.feature_login.data.remote.dto.RegisterResponseDto
import com.prizma_distribucija.androiddevelopertask.feature_login.data.remote.dto.UserDto
import com.prizma_distribucija.androiddevelopertask.feature_login.data.repository.SignUpRepositoryImpl
import com.prizma_distribucija.androiddevelopertask.feature_login.domain.DataStoreManager
import com.prizma_distribucija.androiddevelopertask.feature_login.domain.SignUpRepository
import com.prizma_distribucija.androiddevelopertask.feature_login.domain.model.SignUpUserData
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
import org.junit.After
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

@ExperimentalCoroutinesApi
class SignUpViewModelTests : ViewModel() {

    private lateinit var registerUserUseCase: RegisterUserUseCase

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
        val repository = SignUpRepositoryImpl(api, dispatcherProvider) as SignUpRepository
        registerUserUseCase = RegisterUserUseCase(repository)
        dataStoreManager = DataStoreManagerFakeImpl()
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }


    @Test
    fun `onSignUpClicked, email, password and fullName are empty, should send ShowMessageEvent`() =
        runTest {
            val dispatcherProvider = TestDispatchers()
            val viewModel =
                SignUpViewModel(dispatcherProvider, registerUserUseCase, dataStoreManager)

            val email = ""
            val password = ""
            val fullName = ""

            val signUpUserData = SignUpUserData(email, password, fullName)

            viewModel.onSignUpClicked(signUpUserData)

            viewModel.signUpEvents.test {
                val event = awaitItem()
                assertThat(event).isInstanceOf(SignUpViewModel.SignUpEvents.ShowMessage::class.java)
            }
        }

    @Test
    fun `onSignUpClicked, password and fullName are empty, should send ShowMessageEvent`() =
        runTest {
            val dispatcherProvider = TestDispatchers()
            val viewModel =
                SignUpViewModel(dispatcherProvider, registerUserUseCase, dataStoreManager)

            val email = "random@email.com"
            val password = ""
            val fullName = ""

            val signUpUserData = SignUpUserData(email, password, fullName)
            viewModel.onSignUpClicked(signUpUserData)

            viewModel.signUpEvents.test {
                val event = awaitItem()
                assertThat(event).isInstanceOf(SignUpViewModel.SignUpEvents.ShowMessage::class.java)
            }
        }

    @Test
    fun `onSignUpClicked, fullName is empty, should send ShowMessageEvent`() = runTest {
        val dispatcherProvider = TestDispatchers()
        val viewModel = SignUpViewModel(dispatcherProvider, registerUserUseCase, dataStoreManager)

        val email = "random@email.com"
        val password = "password"
        val fullName = ""

        val signUpUserData = SignUpUserData(email, password, fullName)
        viewModel.onSignUpClicked(signUpUserData)

        viewModel.signUpEvents.test {
            val event = awaitItem()
            assertThat(event).isInstanceOf(SignUpViewModel.SignUpEvents.ShowMessage::class.java)
        }
    }

    @Test
    fun `onSignUpClicked, password is empty, should send ShowMessageEvent`() = runTest {
        val dispatcherProvider = TestDispatchers()
        val viewModel = SignUpViewModel(dispatcherProvider, registerUserUseCase, dataStoreManager)

        val email = "random@email.com"
        val password = ""
        val fullName = "Full Name"

        val signUpUserData = SignUpUserData(email, password, fullName)
        viewModel.onSignUpClicked(signUpUserData)

        viewModel.signUpEvents.test {
            val event = awaitItem()
            assertThat(event).isInstanceOf(SignUpViewModel.SignUpEvents.ShowMessage::class.java)
        }
    }

    @Test
    fun `onSignUpClicked, email and fullName are empty, should send ShowMessageEvent`() = runTest {
        val dispatcherProvider = TestDispatchers()
        val viewModel = SignUpViewModel(dispatcherProvider, registerUserUseCase, dataStoreManager)

        val email = ""
        val password = "password"
        val fullName = ""

        val signUpUserData = SignUpUserData(email, password, fullName)
        viewModel.onSignUpClicked(signUpUserData)

        viewModel.signUpEvents.test {
            val event = awaitItem()
            assertThat(event).isInstanceOf(SignUpViewModel.SignUpEvents.ShowMessage::class.java)
        }
    }

    @Test
    fun `onSignUpClicked, email is empty, should send ShowMessageEvent`() = runTest {
        val dispatcherProvider = TestDispatchers()
        val viewModel = SignUpViewModel(dispatcherProvider, registerUserUseCase, dataStoreManager)

        val email = ""
        val password = "password"
        val fullName = "Full Name"
        val signUpUserData = SignUpUserData(email, password, fullName)

        viewModel.onSignUpClicked(signUpUserData)

        viewModel.signUpEvents.test {
            val event = awaitItem()
            assertThat(event).isInstanceOf(SignUpViewModel.SignUpEvents.ShowMessage::class.java)
        }
    }

    @Test
    fun `onSignUpClicked, email and password are empty, should send ShowMessageEvent`() = runTest {
        val dispatcherProvider = TestDispatchers()
        val viewModel = SignUpViewModel(dispatcherProvider, registerUserUseCase, dataStoreManager)

        val email = ""
        val password = ""
        val fullName = "Full Name"

        val signUpUserData = SignUpUserData(email, password, fullName)
        viewModel.onSignUpClicked(signUpUserData)

        viewModel.signUpEvents.test {
            val event = awaitItem()
            assertThat(event).isInstanceOf(SignUpViewModel.SignUpEvents.ShowMessage::class.java)
        }
    }

    @Test
    fun `onSignUpClicked, data is valid, should signUp and emit status`() = runBlocking {
        val dispatcherProvider = TestDispatchers()
        val viewModel = SignUpViewModel(dispatcherProvider, registerUserUseCase, dataStoreManager)

        val email = "random@email.com"
        val password = "password"
        val fullName = "fullName"

        val signUpUserData = SignUpUserData(email, password, fullName)

        val gson = Gson()

        val token = "random_token"
        val name = "name"
        val userDto = UserDto("avatar", name, email)
        val apiResponse = RegisterResponseDto(token, userDto)
        val jsonResponse = gson.toJson(apiResponse)

        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setBodyDelay(500, TimeUnit.MILLISECONDS)
                .setBody(jsonResponse)
        )

        val job = launch {
            viewModel.registerUserStatus.test {
                val firstEvent = awaitItem()
                assertThat(firstEvent).isInstanceOf(Resource.Success::class.java)

                cancelAndIgnoreRemainingEvents()
            }
        }

        viewModel.onSignUpClicked(signUpUserData)

        job.join()
        job.cancel()
    }

    @Test
    fun `onRegisterSuccess, should save token in dataStore and send SignUpEventsRegistered`() = runTest {
        val dispatcherProvider = TestDispatchers()
        val viewModel = SignUpViewModel(dispatcherProvider, registerUserUseCase, dataStoreManager)
        val token = "token"
        viewModel.onRegisterSuccess(token)

        viewModel.signUpEvents.test {
            val event = awaitItem()
            assertThat(event).isInstanceOf(SignUpViewModel.SignUpEvents.Registered::class.java)
            assertThat(dataStoreManager.authToken.first()).isEqualTo(token)
        }
    }
}