package com.wgroup.woooo_app.woooo.feature.auth.viewmodel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wgroup.woooo_app.woooo.feature.auth.domain.model.params.LoginRequestParams
import com.wgroup.woooo_app.woooo.feature.auth.domain.usecase.LoginUseCase
import com.wgroup.woooo_app.woooo.shared.base.doOnFailure
import com.wgroup.woooo_app.woooo.shared.base.doOnLoading
import com.wgroup.woooo_app.woooo.shared.base.doOnSuccess
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(private val loginUseCase: LoginUseCase) : ViewModel() {

    private val _loginResponse: MutableState<LoginState> = mutableStateOf(LoginState())
    val loginResponse: State<LoginState> = _loginResponse

    fun login() = viewModelScope.launch {
        loginUseCase.invoke(
            LoginRequestParams(
                email = "hamzaf875@gmail.com",
                deviceName = "Mobile",
                password = "1234343"
            )
        ).doOnSuccess {
            _loginResponse.value = LoginState(
                it,
            )
        }
            .doOnFailure {
                _loginResponse.value = LoginState(
                    error = it.toString(),
                )
            }
            .doOnLoading {
                _loginResponse.value = LoginState(
                    isLoading = true
                )
            }.collect{}
    }


}