package com.prizma_distribucija.androiddevelopertask.feature_login.data.repository

import com.prizma_distribucija.androiddevelopertask.core.util.DispatcherProvider
import com.prizma_distribucija.androiddevelopertask.feature_login.data.remote.AuthApiService
import com.prizma_distribucija.androiddevelopertask.feature_login.domain.SignUpRepository
import com.prizma_distribucija.androiddevelopertask.feature_login.domain.model.AuthResponse
import com.prizma_distribucija.androiddevelopertask.feature_login.domain.model.SignUpUserData
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SignUpRepositoryImpl @Inject constructor(
    private val authApiService: AuthApiService,
    private val dispatcherProvider: DispatcherProvider
) : SignUpRepository {

    override suspend fun registerUser(userData: SignUpUserData): AuthResponse =
        withContext(dispatcherProvider.io) {
            val response = authApiService.register(userData)

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