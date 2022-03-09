package com.prizma_distribucija.androiddevelopertask.feature_feed.domain.model

data class FeedResponse(
    val isSuccessful: Boolean,
    val data: List<Feed>?,
    val errorMessage: String?
)
