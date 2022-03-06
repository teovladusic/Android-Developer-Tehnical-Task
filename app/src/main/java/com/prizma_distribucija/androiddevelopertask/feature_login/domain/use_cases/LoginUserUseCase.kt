package com.prizma_distribucija.androiddevelopertask.feature_login.domain.use_cases

import com.prizma_distribucija.androiddevelopertask.core.Resource
import com.prizma_distribucija.androiddevelopertask.feature_login.domain.LoginRepository
import com.prizma_distribucija.androiddevelopertask.feature_login.domain.model.LoginUserData
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class LoginUserUseCase @Inject constructor(
    private val loginRepository: LoginRepository
) {

    suspend operator fun invoke(loginUserData: LoginUserData) = flow<Resource<String>> {
        emit(Resource.Loading())
        val response = loginRepository.loginUser(loginUserData)

        if (response.isSuccessful) {
            emit(Resource.Success(data = response.token))
        } else {
            emit(Resource.Error(message = response.errorMessage ?: "Unknown error appeared."))
        }
    }
}