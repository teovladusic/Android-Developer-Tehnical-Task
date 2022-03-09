package com.prizma_distribucija.androiddevelopertask.feature_login.domain.model

data class AuthResponse(
    val token: String?,
    val isSuccessful: Boolean,
    val errorMessage: String?
)