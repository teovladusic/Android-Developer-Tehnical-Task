package com.prizma_distribucija.androiddevelopertask.feature_feed.domain.use_cases

import com.prizma_distribucija.androiddevelopertask.core.Resource
import com.prizma_distribucija.androiddevelopertask.feature_feed.domain.FeedRepository
import com.prizma_distribucija.androiddevelopertask.feature_feed.domain.model.Feed
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetFeedUseCase @Inject constructor(
    private val feedRepository: FeedRepository
) {

    suspend operator fun invoke() = flow<Resource<List<Feed>>> {
        emit(Resource.Loading())
        val feedResponse = feedRepository.getFeed()

        if (feedResponse.isSuccessful) {
            emit(Resource.Success(data = feedResponse.data))
        } else {
            emit(
                Resource.Error(
                    data = null,
                    message = feedResponse.errorMessage ?: "Unknown error appeared."
                )
            )
        }
    }
}