package com.prizma_distribucija.androiddevelopertask.fakes

import com.prizma_distribucija.androiddevelopertask.feature_login.domain.DataStoreManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class DataStoreManagerFakeImpl : DataStoreManager {

    companion object {
        val dataStoreAuthToken = MutableStateFlow("")
    }

    override suspend fun setAuthToken(token: String) {
        dataStoreAuthToken.emit(token)
    }

    override val authToken: Flow<String>
        get() = dataStoreAuthToken.asStateFlow()
}