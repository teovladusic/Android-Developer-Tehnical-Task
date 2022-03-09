package com.prizma_distribucija.androiddevelopertask.feature_feed.domain.use_cases

import com.prizma_distribucija.androiddevelopertask.core.Resource
import com.prizma_distribucija.androiddevelopertask.feature_feed.domain.ProfileRepository
import com.prizma_distribucija.androiddevelopertask.feature_feed.domain.model.GetUserResponse
import com.prizma_distribucija.androiddevelopertask.feature_feed.domain.model.User
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetProfileUseCase @Inject constructor(
    private val profileRepository: ProfileRepository
) {

    suspend operator fun invoke(id: Int) = flow<Resource<User>> {
        emit(Resource.Loading())

        val response = profileRepository.getUser(id)

        if (response.isSuccessful) {
            emit(Resource.Success(response.data))
        } else {
            emit(
                Resource.Error(
                    message = response.errorMessage ?: "Unknown error appeared.",
                    data = null
                )
            )
        }
    }
}