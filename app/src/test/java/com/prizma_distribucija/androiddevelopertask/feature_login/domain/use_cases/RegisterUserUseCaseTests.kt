package com.prizma_distribucija.androiddevelopertask.feature_login.domain.use_cases

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.prizma_distribucija.androiddevelopertask.core.Resource
import com.prizma_distribucija.androiddevelopertask.feature_login.domain.SignUpRepository
import com.prizma_distribucija.androiddevelopertask.feature_login.domain.model.AuthResponse
import com.prizma_distribucija.androiddevelopertask.feature_login.domain.model.SignUpUserData
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock

@ExperimentalCoroutinesApi
class RegisterUserUseCaseTests {

    @Test
    fun `invoke, user successfully registered, should emit loading, then success with correct data`() =
        runTest {
            val signUpUserData = SignUpUserData("email@random.com", "password", "fullName")

            //repository response
            val registerResponse = AuthResponse("token", true, null)

            val repository = mock(SignUpRepository::class.java)
            `when`(repository.registerUser(signUpUserData)).thenReturn(registerResponse)

            val useCase = RegisterUserUseCase(repository)

            useCase(signUpUserData).test {
                val firstEmission = awaitItem()
                assertThat(firstEmission).isInstanceOf(Resource.Loading::class.java)

                val secondEmission = awaitItem()
                assertThat(secondEmission).isInstanceOf(Resource.Success::class.java)
                assertThat(secondEmission.data).isEqualTo("token")
                assertThat(secondEmission.message).isNull()

                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun `invoke, user failed to register, should emit loading, then error with correct data`() =
        runTest {
            val signUpUserData = SignUpUserData("email@random.com", "password", "fullName")

            //repository response
            val registerResponse = AuthResponse(null, false, "Error")

            val repository = mock(SignUpRepository::class.java)
            `when`(repository.registerUser(signUpUserData)).thenReturn(registerResponse)

            val useCase = RegisterUserUseCase(repository)

            useCase(signUpUserData).test {
                val firstEmission = awaitItem()
                assertThat(firstEmission).isInstanceOf(Resource.Loading::class.java)

                val secondEmission = awaitItem()
                assertThat(secondEmission).isInstanceOf(Resource.Error::class.java)
                assertThat(secondEmission.data).isEqualTo(null)
                assertThat(secondEmission.message).isEqualTo("Error")

                cancelAndIgnoreRemainingEvents()
            }
        }
}