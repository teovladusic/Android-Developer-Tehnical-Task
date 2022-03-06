package com.prizma_distribucija.androiddevelopertask.feature_login.domain

import com.prizma_distribucija.androiddevelopertask.feature_login.domain.model.AuthResponse
import com.prizma_distribucija.androiddevelopertask.feature_login.domain.model.LoginUserData

interface LoginRepository {

    suspend fun loginUser(loginUserData: LoginUserData) : AuthResponse
}