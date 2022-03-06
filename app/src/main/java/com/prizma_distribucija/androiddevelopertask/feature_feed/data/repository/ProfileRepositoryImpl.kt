package com.prizma_distribucija.androiddevelopertask.feature_feed.data.repository

import com.prizma_distribucija.androiddevelopertask.core.util.DispatcherProvider
import com.prizma_distribucija.androiddevelopertask.feature_feed.data.remote.FeedApiService
import com.prizma_distribucija.androiddevelopertask.feature_feed.domain.ProfileRepository
import com.prizma_distribucija.androiddevelopertask.feature_feed.domain.model.GetUserResponse
import com.prizma_distribucija.androiddevelopertask.feature_feed.domain.model.UserMapper
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ProfileRepositoryImpl @Inject constructor(
    private val dispatcherProvider: DispatcherProvider,
    private val feedApiService: FeedApiService,
    private val userMapper: UserMapper
) : ProfileRepository {

    override suspend fun getUser(id: Int): GetUserResponse =
        withContext(dispatcherProvider.io) {
            val response = feedApiService.getUser(id)

            if (response.isSuccessful) {
                GetUserResponse(
                    isSuccessful = true,
                    data = userMapper.mapFromDto(response.body()!!),
                    errorMessage = null
                )
            } else {
                GetUserResponse(
                    isSuccessful = false,
                    data = null,
                    errorMessage = response.message()
                )
            }
        }


}