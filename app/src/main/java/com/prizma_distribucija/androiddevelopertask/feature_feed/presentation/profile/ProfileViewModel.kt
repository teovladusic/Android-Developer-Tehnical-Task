package com.prizma_distribucija.androiddevelopertask.feature_feed.presentation.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prizma_distribucija.androiddevelopertask.core.Resource
import com.prizma_distribucija.androiddevelopertask.core.util.DispatcherProvider
import com.prizma_distribucija.androiddevelopertask.feature_feed.domain.model.User
import com.prizma_distribucija.androiddevelopertask.feature_feed.domain.use_cases.GetProfileUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val dispatcherProvider: DispatcherProvider,
    private val getProfileUseCase: GetProfileUseCase
) : ViewModel() {

    private val _user = MutableStateFlow<User?>(null)
    val user = _user.asStateFlow()

    private val _getUserStatus = MutableSharedFlow<Resource<User>>()
    val getUserStatus = _getUserStatus.asSharedFlow()

    init {
        viewModelScope.launch(dispatcherProvider.io) {
            getProfileUseCase(1).collectLatest {
                _getUserStatus.emit(it)
            }
        }
    }

    fun onGetUserSuccess(user: User) = viewModelScope.launch(dispatcherProvider.default) {
        _user.emit(user)
    }
}