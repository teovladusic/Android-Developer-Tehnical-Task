package com.prizma_distribucija.androiddevelopertask.feature_feed.data.remote.dto

data class FeedDto(
    val id: Int,
    val author: AuthorDto,
    val video: VideoDto,
    val description: String
)