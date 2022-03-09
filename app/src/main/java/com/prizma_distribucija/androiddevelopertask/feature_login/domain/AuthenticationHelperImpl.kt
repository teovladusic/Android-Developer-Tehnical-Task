package com.prizma_distribucija.androiddevelopertask.feature_login.domain

import com.prizma_distribucija.androiddevelopertask.core.util.DispatcherProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

class AuthenticationHelperImpl @Inject constructor(
    private val dataStoreManager: DataStoreManager,
    dispatcherProvider: DispatcherProvider
) : AuthenticationHelper {

    private val _isAuthenticated = MutableStateFlow(true)

    override val isAuthenticated: StateFlow<Boolean>
        get() = _isAuthenticated.asStateFlow()

    init {
        CoroutineScope(dispatcherProvider.default).launch {
            dataStoreManager.authToken.collectLatest {
                val isAuthenticated = it.isNotEmpty()
                _isAuthenticated.emit(isAuthenticated)
            }
        }
    }
}