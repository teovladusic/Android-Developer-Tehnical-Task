package com.prizma_distribucija.androiddevelopertask.feature_feed.domain.model

data class CreatePostResponse(
    val isSuccessful: Boolean,
    val data: Post?,
    val errorMessage: String?
)