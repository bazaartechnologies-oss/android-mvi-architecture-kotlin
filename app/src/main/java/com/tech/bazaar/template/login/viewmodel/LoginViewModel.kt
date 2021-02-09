package com.tech.bazaar.template.login.viewmodel

import android.app.Application
import androidx.databinding.ObservableField
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.tech.bazaar.template.login.model.RotateTokenModel
import com.tech.bazaar.template.AppApplication
import com.tech.bazaar.template.R
import com.tech.bazaar.template.base.CoroutineDispatcher
import com.tech.bazaar.template.base.LogManager
import com.tech.bazaar.template.base.abconfig.BazaarABConfig
import com.tech.bazaar.template.base.manager.IRepoManager
import com.tech.bazaar.template.base.model.BazaarUser
import com.tech.bazaar.template.base.model.ApiState
import com.tech.bazaar.template.login.model.LoginModel
import com.tech.bazaar.template.login.model.LoginResponse
import com.tech.bazaar.template.login.model.LoginResponseModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class LoginViewModel @ViewModelInject constructor(
    application: Application,
    private val repoManager: IRepoManager,
    private val abConfig: BazaarABConfig,
    private val dispatcher: CoroutineDispatcher
) : AndroidViewModel(application) {

    private val Log = LogManager.getInstance(javaClass.simpleName)

    var loginLiveData = MutableLiveData<LoginResponseModel>()

    var phone = ObservableField<String>()
    var password = ObservableField<String>()

    fun isLoggedIn(): Boolean = repoManager.isLoggedIn()

    fun login() {

        val phone = phone.get()?.trim()
        val password = password.get()

        if (phone.isNullOrEmpty() || password.isNullOrEmpty()) {
            return
        }
        loginApi(phone, password)
    }

    private fun loginApi(email: String, password: String) {

        loginMessage(ApiState.IN_PROGRESS)

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
                    loginMessage(ApiState.SUCCESS)
                } else {
                    loginMessage(
                        ApiState.FAILURE,
                        false,
                        AppApplication.appContext.getString(R.string.login_failure_not_found_message)
                    )
                }
            } catch (error: Exception) {
                loginMessage(
                    ApiState.FAILURE,
                    false,
                    AppApplication.appContext.getString(R.string.network_failure_message)
                )
            }
        }
    }

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

    private fun loginMessage(apiState: ApiState, isSuccess: Boolean = true, message: String = "") {
        val loginResponse = LoginResponseModel()
        loginResponse.apiState = apiState
        loginResponse.success = isSuccess
        loginResponse.message = message
        loginLiveData.postValue(loginResponse)
    }
}