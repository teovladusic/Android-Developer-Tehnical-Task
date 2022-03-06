package com.prizma_distribucija.androiddevelopertask.feature_login.domain

import com.prizma_distribucija.androiddevelopertask.feature_login.domain.model.AuthResponse
import com.prizma_distribucija.androiddevelopertask.feature_login.domain.model.SignUpUserData

interface SignUpRepository {

    suspend fun registerUser(userData: SignUpUserData): AuthResponse
}