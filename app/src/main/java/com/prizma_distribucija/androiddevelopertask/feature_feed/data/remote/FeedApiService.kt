package com.prizma_distribucija.androiddevelopertask.feature_feed.data.remote

import com.prizma_distribucija.androiddevelopertask.feature_feed.data.remote.dto.UserResponseDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface FeedApiService {

    @GET("user/{id}")
    suspend fun getUser(@Path("id") id: Int) : Response<UserResponseDto>
}