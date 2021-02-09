package com.tech.bazaar.template.login.state

import com.tech.bazaar.template.base.mvicore.IState
import com.tech.bazaar.template.login.model.LoginResponse

sealed class LoginState: IState{
    object Idle: LoginState()
    object Loading : LoginState()
    data class LoginSuccess(val loginResponse: LoginResponse) : LoginState()
    data class LoginError(val error: String) : LoginState()
}

