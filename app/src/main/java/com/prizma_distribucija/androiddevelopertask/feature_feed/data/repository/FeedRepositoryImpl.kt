package com.prizma_distribucija.androiddevelopertask.feature_feed.data.repository

import com.prizma_distribucija.androiddevelopertask.core.util.DispatcherProvider
import com.prizma_distribucija.androiddevelopertask.feature_feed.data.remote.FeedApiService
import com.prizma_distribucija.androiddevelopertask.feature_feed.domain.FeedRepository
import com.prizma_distribucija.androiddevelopertask.feature_feed.domain.model.FeedResponse
import com.prizma_distribucija.androiddevelopertask.feature_feed.domain.model.mapper.FeedMapper
import kotlinx.coroutines.withContext
import javax.inject.Inject

class FeedRepositoryImpl @Inject constructor(
    private val feedApiService: FeedApiService,
    private val dispatcherProvider: DispatcherProvider,
    private val feedMapper: FeedMapper
) : FeedRepository {

    override suspend fun getFeed(): FeedResponse =
        withContext(dispatcherProvider.io) {
            val response = feedApiService.getFeed()

            return@withContext if (response.isSuccessful) {
                FeedResponse(
                    isSuccessful = true,
                    data = response.body()!!.map { feedMapper.mapFromDto(it) },
                    errorMessage = null
                )
            } else {
                FeedResponse(
                    isSuccessful = false,
                    data = null,
                    errorMessage = response.message()
                )
            }
        }
}