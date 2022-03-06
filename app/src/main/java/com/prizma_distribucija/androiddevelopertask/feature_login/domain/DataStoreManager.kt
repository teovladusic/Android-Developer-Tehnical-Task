package com.prizma_distribucija.androiddevelopertask.feature_login.domain

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

interface DataStoreManager {

    suspend fun setAuthToken(token: String)

    val authToken: Flow<String>
}
