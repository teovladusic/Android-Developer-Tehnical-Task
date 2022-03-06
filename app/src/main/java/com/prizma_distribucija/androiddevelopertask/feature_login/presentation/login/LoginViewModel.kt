package com.prizma_distribucija.androiddevelopertask.feature_login.presentation.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.material.snackbar.Snackbar
import com.prizma_distribucija.androiddevelopertask.core.Resource
import com.prizma_distribucija.androiddevelopertask.core.util.DispatcherProvider
import com.prizma_distribucija.androiddevelopertask.feature_login.domain.DataStoreManager
import com.prizma_distribucija.androiddevelopertask.feature_login.domain.model.LoginUserData
import com.prizma_distribucija.androiddevelopertask.feature_login.domain.use_cases.LoginUserUseCase
import com.prizma_distribucija.androiddevelopertask.feature_login.presentation.sign_up.SignUpViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val dispatcherProvider: DispatcherProvider,
    private val loginUserUseCase: LoginUserUseCase,
    private val dataStoreManager: DataStoreManager
) : ViewModel() {

    private val loginEventsChannel = Channel<LoginEvents>()
    val loginEvents = loginEventsChannel.receiveAsFlow()

    private val _loginStatus = MutableSharedFlow<Resource<String>>()
    val loginStatus = _loginStatus.asSharedFlow()

    private fun validateEmailAndPassword(email: String, password: String): Boolean {
        return email.isNotBlank() && password.isNotBlank()
    }

    fun onLoginClicked(loginUserData: LoginUserData) {
        val isDataValidate = validateEmailAndPassword(loginUserData.email, loginUserData.password)

        if (isDataValidate == false) {
            sendLoginEvent(LoginEvents.ShowMessage("Please fill out all fields."))
            return
        }

        viewModelScope.launch(dispatcherProvider.default) {
            loginUserUseCase(loginUserData).collectLatest { status ->
                _loginStatus.emit(status)
            }
        }
    }

    fun onLoginSuccess(data: String) = viewModelScope.launch(dispatcherProvider.default) {
        dataStoreManager.setAuthToken(data)
        sendLoginEvent(LoginEvents.LoggedIn)
    }

    private fun sendLoginEvent(event: LoginEvents) =
        viewModelScope.launch(dispatcherProvider.default) {
            loginEventsChannel.send(event)
        }

    sealed class LoginEvents {
        data class ShowMessage(val message: String) : LoginEvents()
        object LoggedIn : LoginEvents()
    }
}