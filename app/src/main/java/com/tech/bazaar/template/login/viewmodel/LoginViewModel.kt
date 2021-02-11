package com.tech.bazaar.template.login.viewmodel

import android.app.Application
import androidx.databinding.ObservableField
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.tech.bazaar.template.login.model.RotateTokenModel
import com.tech.bazaar.template.AppApplication
import com.tech.bazaar.template.R
import com.tech.bazaar.template.base.CoroutineDispatcher
import com.tech.bazaar.template.base.LogManager
import com.tech.bazaar.template.base.abconfig.BazaarABConfig
import com.tech.bazaar.template.base.manager.IRepoManager
import com.tech.bazaar.template.base.model.BazaarUser
import com.tech.bazaar.template.base.mvicore.IModel
import com.tech.bazaar.template.login.action.LoginIntent
import com.tech.bazaar.template.login.model.LoginModel
import com.tech.bazaar.template.login.model.LoginResponse
import com.tech.bazaar.template.login.state.LoginState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch

class LoginViewModel @ViewModelInject constructor(
    application: Application,
    private val repoManager: IRepoManager,
    private val abConfig: BazaarABConfig,
    private val dispatcher: CoroutineDispatcher
) : AndroidViewModel(application), IModel<LoginState, LoginIntent> {

    private val Log = LogManager.getInstance(javaClass.simpleName)

    override val intents: Channel<LoginIntent> = Channel(Channel.UNLIMITED)
    private val _state = MutableStateFlow<LoginState>(LoginState.Idle)
    override val state: StateFlow<LoginState>
        get() = _state

    init {
        intentHandler()
    }

    var phone = ObservableField<String>()
    var password = ObservableField<String>()

    /**
     * Observe Intents/ Actions from View layer
     */
    private fun intentHandler() {
        viewModelScope.launch {
            intents.consumeAsFlow().collect {
                when (it) {
                    is LoginIntent.DoLogin -> login()
                }
            }
        }
    }

    private fun updateState(loginState: LoginState) {
        _state.value = loginState
    }


    private fun login() {
        val phone = phone.get()?.trim()
        val password = password.get()

        if (phone.isNullOrEmpty() || password.isNullOrEmpty()) {
            return
        }
        loginApi(phone, password)
    }

    private fun loginApi(email: String, password: String) {
        updateState(LoginState.Loading)

        viewModelScope.launch {
            try {
                val response =
                    repoManager.login(LoginModel(email, password))
                if (response.isSuccessful && response.body() != null) {
                    val data = response.body() as LoginResponse
                    repoManager.saveUser(
                        BazaarUser(
                            accessToken = data.legacyToken,
                            token = data.token,
                            userId = data.userInfo.id,
                            firstName = data.userInfo.fullName,
                            phone = data.userInfo.phone
                        )
                    )
                    updateState(LoginState.LoginSuccess(data))
                } else {
                    updateState(LoginState.LoginError(AppApplication.appContext.getString(R.string.login_failure_not_found_message)))
                }
            } catch (error: Exception) {
                updateState(LoginState.LoginError(AppApplication.appContext.getString(R.string.network_failure_message)))
            }
        }
    }

    fun isLoggedIn(): Boolean = repoManager.isLoggedIn()

    fun rotateTokenIfRequired() {
        val refresh = repoManager.user?.refreshToken
        if (refresh.isNullOrEmpty()) {
            rotateTokenSilently()
        }
    }

    fun rotateTokenSilently() {
        val phone = repoManager.user?.phone ?: ""
        val token = repoManager.user?.token ?: ""

        CoroutineScope(dispatcher.io()).launch {
            try {
                val response =
                    repoManager.rotateToken(RotateTokenModel(phone, token))
                if (response.isSuccessful && response.body() != null) {
                    Log.i("Get Token background success")
                    val data = response.body() as LoginResponse
                    val user = repoManager.user
                    user?.let {
                        val updatedUser =
                            BazaarUser(
                                user.userId,
                                user.firstName,
                                user.lastName,
                                user.phone,
                                user.company,
                                user.email,
                                user.token,
                                data.legacyToken,
                                "",
                                data.token,
                                data.refreshToken,
                                data.expiresAt
                            )
                        repoManager.saveUser(updatedUser)
                    }
                }
            } catch (error: Exception) {
                Log.e("Backend Exception: ", error)
            }
        }
    }

}
