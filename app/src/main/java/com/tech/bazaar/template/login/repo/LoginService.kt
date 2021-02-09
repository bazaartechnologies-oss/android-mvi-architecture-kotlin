package com.tech.bazaar.template.login.repo

import com.tech.bazaar.template.login.model.RotateTokenModel
import com.tech.bazaar.template.login.model.LoginModel
import com.tech.bazaar.template.login.model.LoginResponse
import com.tech.bazaar.template.retrofit.BackendApiService
import retrofit2.Response
import javax.inject.Inject

class LoginService @Inject constructor(
    private val apiService: BackendApiService
) : ILoginService {

    override suspend fun login(loginModel: LoginModel): Response<LoginResponse> {
        return apiService.login(loginModel)
    }

    override suspend fun rotateToken(tokenModel: RotateTokenModel): Response<LoginResponse> {
        return apiService.rotateToken(tokenModel)
    }
}