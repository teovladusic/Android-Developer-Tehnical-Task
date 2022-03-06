package com.prizma_distribucija.androiddevelopertask.feature_login.presentation.sign_up

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.material.snackbar.Snackbar
import com.prizma_distribucija.androiddevelopertask.core.Resource
import com.prizma_distribucija.androiddevelopertask.core.util.DispatcherProvider
import com.prizma_distribucija.androiddevelopertask.feature_login.domain.DataStoreManager
import com.prizma_distribucija.androiddevelopertask.feature_login.domain.model.SignUpUserData
import com.prizma_distribucija.androiddevelopertask.feature_login.domain.use_cases.RegisterUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val dispatcherProvider: DispatcherProvider,
    private val registerUserUseCase: RegisterUserUseCase,
    private val dataStoreManager: DataStoreManager
) : ViewModel() {

    private val signUpEventsChannel = Channel<SignUpEvents>()
    val signUpEvents = signUpEventsChannel.receiveAsFlow()

    private val _registerUserStatus = MutableSharedFlow<Resource<String>>()
    val registerUserStatus = _registerUserStatus.asSharedFlow()

    fun onSignUpClicked(signUpUserData: SignUpUserData) {
        val isDataValidate = validateEmailPasswordAndFullName(
            signUpUserData.email,
            signUpUserData.password,
            signUpUserData.fullName
        )

        if (isDataValidate == false) {
            sendSignUpEvent(SignUpEvents.ShowMessage("Please fill out all fields."))
            return
        }

        viewModelScope.launch(dispatcherProvider.default) {
            registerUserUseCase(signUpUserData).collectLatest {
                _registerUserStatus.emit(it)
            }
        }
    }

    fun onRegisterSuccess(data: String) = viewModelScope.launch(dispatcherProvider.default) {
        dataStoreManager.setAuthToken(data)
        sendSignUpEvent(SignUpEvents.Registered)
    }

    private fun validateEmailPasswordAndFullName(
        email: String,
        password: String,
        fullName: String
    ): Boolean {
        return email.isNotBlank() && password.isNotBlank() && fullName.isNotBlank()
    }

    private fun sendSignUpEvent(event: SignUpEvents) =
        viewModelScope.launch(dispatcherProvider.default) {
            signUpEventsChannel.send(event)
        }

    sealed class SignUpEvents {
        data class ShowMessage(val message: String) : SignUpEvents()
        object Registered : SignUpEvents()
    }
}