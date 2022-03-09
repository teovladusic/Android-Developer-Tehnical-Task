package com.prizma_distribucija.androiddevelopertask.feature_login.domain

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

private val Context.dataStore by preferencesDataStore("authentication")

class DataStoreManagerImpl @Inject constructor(
    @ApplicationContext appContext: Context
) : DataStoreManager {
    private val authDataStore = appContext.dataStore

    private val authKey = stringPreferencesKey("auth")

    override suspend fun setAuthToken(token: String) {
        authDataStore.edit { auth ->
            auth[authKey] = token
        }
    }

    override val authToken: Flow<String> = authDataStore.data.map {
        it[authKey] ?: ""
    }
}