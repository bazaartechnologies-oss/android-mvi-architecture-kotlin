package com.tech.bazaar.template.login.repo

import com.tech.bazaar.template.login.model.RotateTokenModel
import com.tech.bazaar.template.login.model.LoginModel
import com.tech.bazaar.template.login.model.LoginResponse
import retrofit2.Response

interface ILoginRepository {
    /**
     * Use this function to call Login API
     */
    suspend fun login(loginModel: LoginModel): Response<LoginResponse>

    /**
     * Use this function to fetch Rotate Token
     */
    suspend fun rotateToken(tokenModel: RotateTokenModel): Response<LoginResponse>
}