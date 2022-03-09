package com.prizma_distribucija.androiddevelopertask.feature_feed.data.remote

import com.prizma_distribucija.androiddevelopertask.feature_feed.data.remote.dto.FeedDto
import com.prizma_distribucija.androiddevelopertask.feature_feed.data.remote.dto.PostDto
import com.prizma_distribucija.androiddevelopertask.feature_feed.data.remote.dto.UserResponseDto
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.*

interface FeedApiService {

    @GET("user/{id}")
    suspend fun getUser(@Path("id") id: Int): Response<UserResponseDto>

    @Multipart
    @POST("post")
    suspend fun createPost(@Part image: MultipartBody.Part): Response<PostDto>

    @GET("feed")
    suspend fun getFeed() : Response<List<FeedDto>>

}