package com.tech.bazaar.template.login.repo

import com.tech.bazaar.template.login.model.RotateTokenModel
import com.tech.bazaar.template.login.model.LoginModel
import com.tech.bazaar.template.login.model.LoginResponse
import com.tech.bazaar.template.retrofit.BackendApiService
import retrofit2.Response
import javax.inject.Inject

class LoginRepository @Inject constructor(
    private val loginService: ILoginService
) : ILoginRepository {

    override suspend fun login(loginModel: LoginModel): Response<LoginResponse> {
        return loginService.login(loginModel)
    }

    override suspend fun rotateToken(tokenModel: RotateTokenModel): Response<LoginResponse> {
        return loginService.rotateToken(tokenModel)
    }
}