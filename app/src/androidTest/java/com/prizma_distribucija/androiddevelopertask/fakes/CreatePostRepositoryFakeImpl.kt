package com.prizma_distribucija.androiddevelopertask.fakes

import com.prizma_distribucija.androiddevelopertask.feature_feed.domain.CreatePostRepository
import com.prizma_distribucija.androiddevelopertask.feature_feed.domain.model.CreatePostResponse
import com.prizma_distribucija.androiddevelopertask.feature_feed.domain.model.Post
import okhttp3.MultipartBody

class CreatePostRepositoryFakeImpl : CreatePostRepository {
    companion object {
        var isSuccessful = false
    }

    override suspend fun createPost(image: MultipartBody.Part): CreatePostResponse {
        return if (isSuccessful) {
            CreatePostResponse(
                isSuccessful = true,
                Post(1),
                null
            )
        } else {
            CreatePostResponse(
                isSuccessful = false,
                null,
                "error message"
            )
        }
    }
}