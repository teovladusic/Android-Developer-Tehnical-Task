package com.prizma_distribucija.androiddevelopertask.feature_login.data.remote.dto

import com.google.gson.annotations.SerializedName

data class UserDto(
    @SerializedName("avatar")
    val avatar: String,

    @SerializedName("name")
    val name: String,

    @SerializedName("email")
    val email: String
)