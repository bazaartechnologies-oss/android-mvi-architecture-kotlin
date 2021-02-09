package com.tech.bazaar.template.login.repo

import com.tech.bazaar.template.login.model.RotateTokenModel
import com.tech.bazaar.template.login.model.LoginModel
import com.tech.bazaar.template.login.model.LoginResponse
import com.tech.bazaar.template.retrofit.BackendApiService
import retrofit2.Response
import javax.inject.Inject

interface ILoginService {
    suspend fun login(loginModel: LoginModel): Response<LoginResponse>
    suspend fun rotateToken(tokenModel: RotateTokenModel): Response<LoginResponse>
}