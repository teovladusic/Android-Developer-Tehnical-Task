package com.prizma_distribucija.androiddevelopertask.feature_feed.data.remote.dto

import com.google.gson.annotations.SerializedName

data class UserResponseDto(
    val id: Int?,
    val name: String?,
    val avatar: String?,
    val views: String?,
    val videoPlays: String?,
    @SerializedName("plan") val plan: PlanDto?,
    @SerializedName("athletes") val athletes: List<AthleteDto>?
)