package com.tech.bazaar.template.login.viewmodel

import com.tech.bazaar.template.login.model.RotateTokenModel
import com.tech.bazaar.template.login.model.LoginModel
import com.tech.bazaar.template.login.model.LoginResponse
import com.tech.bazaar.template.retrofit.BackendApiService
import retrofit2.Response
import javax.inject.Inject

class LoginRepository @Inject constructor(
    private val apiService: BackendApiService
) {

    suspend fun login(loginModel: LoginModel): Response<LoginResponse> {
        return apiService.login(loginModel)
    }

    suspend fun rotateToken(tokenModel: RotateTokenModel): Response<LoginResponse> {
        return apiService.rotateToken(tokenModel)
    }
}