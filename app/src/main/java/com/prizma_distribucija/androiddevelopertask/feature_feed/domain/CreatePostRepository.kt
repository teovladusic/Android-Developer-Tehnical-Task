package com.prizma_distribucija.androiddevelopertask.feature_feed.domain

import com.prizma_distribucija.androiddevelopertask.feature_feed.domain.model.CreatePostResponse
import okhttp3.MultipartBody

interface CreatePostRepository {

    suspend fun createPost(image: MultipartBody.Part) : CreatePostResponse
}