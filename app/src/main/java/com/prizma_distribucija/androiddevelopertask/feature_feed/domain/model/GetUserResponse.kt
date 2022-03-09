package com.prizma_distribucija.androiddevelopertask.feature_feed.domain.model

data class GetUserResponse(
    val isSuccessful: Boolean,
    val data: User?,
    val errorMessage: String?
)