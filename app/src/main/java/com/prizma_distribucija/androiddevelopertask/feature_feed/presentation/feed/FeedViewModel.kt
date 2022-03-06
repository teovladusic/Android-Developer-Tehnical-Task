package com.prizma_distribucija.androiddevelopertask.feature_feed.presentation.feed

import androidx.lifecycle.ViewModel
import com.prizma_distribucija.androiddevelopertask.feature_login.domain.AuthenticationHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FeedViewModel @Inject constructor(
    private val authenticationHelper: AuthenticationHelper
): ViewModel() {

    val isAuthenticated = authenticationHelper.isAuthenticated
}