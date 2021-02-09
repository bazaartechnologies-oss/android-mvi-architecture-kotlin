package com.tech.bazaar.template.retrofit

import com.tech.bazaar.template.login.model.RenewTokenModel
import com.tech.bazaar.template.login.model.RotateTokenModel
import com.tech.bazaar.template.BazaarConstants.CLIENT_KEY_HEADER
import com.tech.bazaar.template.login.model.LoginModel
import com.tech.bazaar.template.login.model.LoginResponse
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface BackendApiService {

    @Headers(CLIENT_KEY_HEADER)
    @POST("/v1/auth/login")
    suspend fun login(@Body loginModel: LoginModel): Response<LoginResponse>

    @Headers(CLIENT_KEY_HEADER)
    @POST("/v3/auth/token/renew")
    fun renewAccessToken(@Body renewModel: RenewTokenModel): Call<LoginResponse>

    @Headers(CLIENT_KEY_HEADER)
    @POST("/v3/auth/token/rotate")
    suspend fun rotateToken(@Body tokenModel: RotateTokenModel): Response<LoginResponse>

}