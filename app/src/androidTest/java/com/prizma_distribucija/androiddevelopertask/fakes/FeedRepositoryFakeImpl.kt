package com.prizma_distribucija.androiddevelopertask.fakes

import com.prizma_distribucija.androiddevelopertask.feature_feed.domain.FeedRepository
import com.prizma_distribucija.androiddevelopertask.feature_feed.domain.model.Author
import com.prizma_distribucija.androiddevelopertask.feature_feed.domain.model.Feed
import com.prizma_distribucija.androiddevelopertask.feature_feed.domain.model.FeedResponse
import com.prizma_distribucija.androiddevelopertask.feature_feed.domain.model.Video
import kotlinx.coroutines.delay

class FeedRepositoryFakeImpl : FeedRepository {

    companion object {
        var isSuccessful = false
        var delayTime = 0L
    }

    override suspend fun getFeed(): FeedResponse {
        delay(delayTime)

        val feed = Feed(1, Author(1, "name"), Video("handler", "url", 1), "description")

        return if (isSuccessful) {
            FeedResponse(
                isSuccessful = true,
                data = listOf(feed),
                errorMessage = null
            )
        } else {
            FeedResponse(
                isSuccessful = false,
                data = null,
                errorMessage = "error message"
            )
        }
    }
}