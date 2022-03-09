package com.prizma_distribucija.androiddevelopertask.feature_login.domain.use_cases

import app.cash.turbine.test
import com.google.common.truth.Truth
import com.prizma_distribucija.androiddevelopertask.core.Resource
import com.prizma_distribucija.androiddevelopertask.feature_login.domain.LoginRepository
import com.prizma_distribucija.androiddevelopertask.feature_login.domain.SignUpRepository
import com.prizma_distribucija.androiddevelopertask.feature_login.domain.model.AuthResponse
import com.prizma_distribucija.androiddevelopertask.feature_login.domain.model.LoginUserData
import com.prizma_distribucija.androiddevelopertask.feature_login.domain.model.SignUpUserData
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock

@ExperimentalCoroutinesApi
class LoginUserUseCaseTests {

    @Test
    fun `invoke, user successfully logged in, should emit loading, then success with correct data`() =
        runTest {
            val loginUserData = LoginUserData("email@random.com", "password")

            //repository response
            val loginResponse = AuthResponse("token", true, null)

            val repository = mock(LoginRepository::class.java)
            `when`(repository.loginUser(loginUserData)).thenReturn(loginResponse)

            val useCase = LoginUserUseCase(repository)

            useCase(loginUserData).test {
                val firstEmission = awaitItem()
                Truth.assertThat(firstEmission).isInstanceOf(Resource.Loading::class.java)

                val secondEmission = awaitItem()
                Truth.assertThat(secondEmission).isInstanceOf(Resource.Success::class.java)
                Truth.assertThat(secondEmission.data).isEqualTo("token")
                Truth.assertThat(secondEmission.message).isNull()

                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun `invoke, user failed to register, should emit loading, then error with correct data`() =
        runTest {
            val loginUserData = LoginUserData("email@random.com", "password")

            //repository response
            val loginResponse = AuthResponse(null, false, "Error")

            val repository = mock(LoginRepository::class.java)
            `when`(repository.loginUser(loginUserData)).thenReturn(loginResponse)

            val useCase = LoginUserUseCase(repository)

            useCase(loginUserData).test {
                val firstEmission = awaitItem()
                Truth.assertThat(firstEmission).isInstanceOf(Resource.Loading::class.java)

                val secondEmission = awaitItem()
                Truth.assertThat(secondEmission).isInstanceOf(Resource.Error::class.java)
                Truth.assertThat(secondEmission.data).isEqualTo(null)
                Truth.assertThat(secondEmission.message).isEqualTo("Error")

                cancelAndIgnoreRemainingEvents()
            }
        }
}