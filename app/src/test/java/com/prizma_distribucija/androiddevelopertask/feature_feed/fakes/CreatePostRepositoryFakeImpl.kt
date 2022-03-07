package com.prizma_distribucija.androiddevelopertask.feature_feed.fakes

import com.prizma_distribucija.androiddevelopertask.feature_feed.domain.CreatePostRepository
import com.prizma_distribucija.androiddevelopertask.feature_feed.domain.model.CreatePostResponse
import com.prizma_distribucija.androiddevelopertask.feature_feed.domain.model.Post
import kotlinx.coroutines.delay
import okhttp3.MultipartBody

class CreatePostRepositoryFakeImpl : CreatePostRepository {

    companion object {
        var isSuccessful = false
        var delayTime = 0L
    }

    override suspend fun createPost(image: MultipartBody.Part): CreatePostResponse {
        delay(delayTime)
        return if (isSuccessful) {
            CreatePostResponse(
                isSuccessful = true,
                data = Post(0),
                errorMessage = null
            )
        } else {
            CreatePostResponse(
                isSuccessful = false,
                data = null,
                errorMessage = "error message"
            )
        }
    }
}