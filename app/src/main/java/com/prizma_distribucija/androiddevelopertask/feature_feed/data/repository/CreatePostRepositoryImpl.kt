package com.prizma_distribucija.androiddevelopertask.feature_feed.data.repository

import com.prizma_distribucija.androiddevelopertask.core.util.DispatcherProvider
import com.prizma_distribucija.androiddevelopertask.feature_feed.data.remote.FeedApiService
import com.prizma_distribucija.androiddevelopertask.feature_feed.data.remote.dto.PostDto
import com.prizma_distribucija.androiddevelopertask.feature_feed.domain.CreatePostRepository
import com.prizma_distribucija.androiddevelopertask.feature_feed.domain.model.CreatePostResponse
import com.prizma_distribucija.androiddevelopertask.feature_feed.domain.model.Post
import com.prizma_distribucija.androiddevelopertask.feature_feed.domain.model.PostMapper
import kotlinx.coroutines.withContext
import okhttp3.MultipartBody
import javax.inject.Inject

class CreatePostRepositoryImpl @Inject constructor(
    private val dispatcherProvider: DispatcherProvider,
    private val feedApiService: FeedApiService,
    private val postMapper: PostMapper
) : CreatePostRepository {

    override suspend fun createPost(image: MultipartBody.Part): CreatePostResponse =
        withContext(dispatcherProvider.io) {
            val response = feedApiService.createPost(image)

            if (response.isSuccessful) {
                CreatePostResponse(
                    isSuccessful = true,
                    data = postMapper.mapFromDto(response.body() ?: PostDto(0)),
                    errorMessage = null
                )
            } else {
                CreatePostResponse(
                    isSuccessful = false,
                    data = null,
                    errorMessage = response.message()
                )
            }
        }

}