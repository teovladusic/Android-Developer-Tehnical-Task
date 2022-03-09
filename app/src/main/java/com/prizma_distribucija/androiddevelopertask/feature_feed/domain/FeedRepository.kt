package com.prizma_distribucija.androiddevelopertask.feature_feed.domain

import com.prizma_distribucija.androiddevelopertask.feature_feed.domain.model.FeedResponse

interface FeedRepository {

    suspend fun getFeed() : FeedResponse
}