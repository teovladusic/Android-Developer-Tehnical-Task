package com.prizma_distribucija.androiddevelopertask.feature_feed.domain.model

data class Feed(
    val id: Int,
    val author: Author,
    val video: Video,
    val description: String
)