package com.prizma_distribucija.androiddevelopertask.feature_login.domain.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class SignUpUserData(
    @SerializedName("email")
    val email: String,

    @SerializedName("password")
    val password: String,

    @SerializedName("fullName")
    val fullName: String
) : Parcelable