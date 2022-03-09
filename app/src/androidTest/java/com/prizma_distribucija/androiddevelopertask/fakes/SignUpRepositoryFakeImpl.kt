package com.prizma_distribucija.androiddevelopertask.fakes

import com.prizma_distribucija.androiddevelopertask.feature_login.domain.SignUpRepository
import com.prizma_distribucija.androiddevelopertask.feature_login.domain.model.AuthResponse
import com.prizma_distribucija.androiddevelopertask.feature_login.domain.model.SignUpUserData
import kotlinx.coroutines.delay

class SignUpRepositoryFakeImpl : SignUpRepository {

    companion object {
        var isSuccessful = false
        var delayTime = 0L
    }

    override suspend fun registerUser(userData: SignUpUserData): AuthResponse {
        delay(delayTime)
        return if (isSuccessful) {
            AuthResponse(
                isSuccessful = isSuccessful,
                token = "random_token",
                errorMessage = null
            )
        } else {
            AuthResponse(
                isSuccessful = isSuccessful,
                token = null,
                errorMessage = "error message"
            )
        }
    }
}