package com.prizma_distribucija.androiddevelopertask.feature_login.data.remote

import com.prizma_distribucija.androiddevelopertask.feature_login.data.remote.dto.LoginResponseDto
import com.prizma_distribucija.androiddevelopertask.feature_login.data.remote.dto.RegisterResponseDto
import com.prizma_distribucija.androiddevelopertask.feature_login.domain.model.LoginUserData
import com.prizma_distribucija.androiddevelopertask.feature_login.domain.model.SignUpUserData
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface AuthApiService {

    @Headers("Content-Type: application/json")
    @POST("registration")
    suspend fun register(
        @Body signUpUserData: SignUpUserData
    ): Response<RegisterResponseDto>

    @Headers("Content-Type: application/json")
    @POST("login")
    suspend fun login(
        @Body loginUserData: LoginUserData
    ) : Response<LoginResponseDto>
}