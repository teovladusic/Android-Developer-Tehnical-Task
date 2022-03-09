package com.prizma_distribucija.androiddevelopertask.feature_login.fakes

import com.prizma_distribucija.androiddevelopertask.feature_login.domain.DataStoreManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class DataStoreManagerFakeImpl : DataStoreManager {

    private val authTokenStateFlow = MutableStateFlow("")

    override suspend fun setAuthToken(token: String) {
        authTokenStateFlow.emit(token)
    }

    override val authToken: Flow<String>
        get() = authTokenStateFlow.asStateFlow()
}