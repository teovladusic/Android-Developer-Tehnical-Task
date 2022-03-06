package com.prizma_distribucija.androiddevelopertask.feature_login.domain.use_cases

import com.prizma_distribucija.androiddevelopertask.core.Resource
import com.prizma_distribucija.androiddevelopertask.feature_login.domain.SignUpRepository
import com.prizma_distribucija.androiddevelopertask.feature_login.domain.model.SignUpUserData
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class RegisterUserUseCase @Inject constructor(
    private val signUpRepository: SignUpRepository
) {

    suspend operator fun invoke(signUpUserData: SignUpUserData) = flow<Resource<String>> {
        emit(Resource.Loading())
        val response = signUpRepository.registerUser(signUpUserData)

        if (response.isSuccessful) {
            emit(Resource.Success(data = response.token))
        } else {
            emit(Resource.Error(message = response.errorMessage ?: "Unknown error appeared."))
        }
    }
}