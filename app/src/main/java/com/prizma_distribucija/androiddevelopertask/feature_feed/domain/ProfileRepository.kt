package com.prizma_distribucija.androiddevelopertask.feature_feed.domain

import com.prizma_distribucija.androiddevelopertask.feature_feed.domain.model.GetUserResponse

interface ProfileRepository {

    suspend fun getUser(id: Int) : GetUserResponse
}