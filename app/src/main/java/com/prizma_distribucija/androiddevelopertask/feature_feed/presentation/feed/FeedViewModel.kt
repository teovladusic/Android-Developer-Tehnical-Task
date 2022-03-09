package com.prizma_distribucija.androiddevelopertask.feature_feed.presentation.feed

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prizma_distribucija.androiddevelopertask.core.Resource
import com.prizma_distribucija.androiddevelopertask.core.util.DispatcherProvider
import com.prizma_distribucija.androiddevelopertask.feature_feed.domain.model.Feed
import com.prizma_distribucija.androiddevelopertask.feature_feed.domain.use_cases.GetFeedUseCase
import com.prizma_distribucija.androiddevelopertask.feature_login.domain.AuthenticationHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FeedViewModel @Inject constructor(
    private val authenticationHelper: AuthenticationHelper,
    private val feedUseCase: GetFeedUseCase,
    private val dispatcherProvider: DispatcherProvider
) : ViewModel() {

    val isAuthenticated = authenticationHelper.isAuthenticated

    private val _getFeedStatus = MutableSharedFlow<Resource<List<Feed>>>(1)
    val getFeedStatus = _getFeedStatus.asSharedFlow()

    init {
        viewModelScope.launch(dispatcherProvider.io) {
            feedUseCase().collectLatest {
                _getFeedStatus.emit(it)
            }
        }
    }

    private var _viewPagerPosition = 0
    val viewPagerPosition get() = _viewPagerPosition

    private val _shouldVideoPlay = MutableSharedFlow<Boolean>(1)
    val shouldVideoPlay = _shouldVideoPlay.asSharedFlow()

    fun onPageChanged(position: Int) = viewModelScope.launch(dispatcherProvider.default) {
        _viewPagerPosition = position
        _shouldVideoPlay.emit(true)
    }

    fun onVideoClick() = viewModelScope.launch(dispatcherProvider.default) {
        _shouldVideoPlay.emit(
            !shouldVideoPlay.first()
        )
    }
}