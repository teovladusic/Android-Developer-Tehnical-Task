package com.prizma_distribucija.androiddevelopertask.feature_login.data.repository

import com.prizma_distribucija.androiddevelopertask.core.util.DispatcherProvider
import com.prizma_distribucija.androiddevelopertask.feature_login.data.remote.AuthApiService
import com.prizma_distribucija.androiddevelopertask.feature_login.domain.LoginRepository
import com.prizma_distribucija.androiddevelopertask.feature_login.domain.model.AuthResponse
import com.prizma_distribucija.androiddevelopertask.feature_login.domain.model.LoginUserData
import kotlinx.coroutines.withContext
import javax.inject.Inject

class LoginRepositoryImpl @Inject constructor(
    private val authApiService: AuthApiService,
    private val dispatcherProvider: DispatcherProvider
) : LoginRepository {

    override suspend fun loginUser(loginUserData: LoginUserData): AuthResponse =
        withContext(dispatcherProvider.io) {
            val response = authApiService.login(loginUserData)

            if (response.isSuccessful) {
                AuthResponse(
                    token = response.body()?.token ?: "",
                    isSuccessful = true,
                    errorMessage = null
                )
            } else {
                AuthResponse(
                    token = null,
                    isSuccessful = false,
                    errorMessage = response.message()
                )
            }
        }
}