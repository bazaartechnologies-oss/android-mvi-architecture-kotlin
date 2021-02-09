package com.tech.bazaar.template.login.action

import com.tech.bazaar.template.base.mvicore.IIntent

sealed class LoginIntent : IIntent {
    object DoLogin : LoginIntent()
}