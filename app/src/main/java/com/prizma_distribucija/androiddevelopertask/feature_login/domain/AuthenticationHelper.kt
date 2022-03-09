package com.prizma_distribucija.androiddevelopertask.feature_login.domain

import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow

interface AuthenticationHelper {

    val isAuthenticated: StateFlow<Boolean>
}