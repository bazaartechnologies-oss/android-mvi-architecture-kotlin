package com.tech.bazaar.template.retrofit.interceptor

import android.content.Context
import com.tech.bazaar.template.login.model.RenewTokenModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.tech.bazaar.template.BazaarConstants.AUTHORIZATION
import com.tech.bazaar.template.BazaarConstants.BEARER
import com.tech.bazaar.template.BazaarConstants.REFRESH_TOKEN_EXPIRED_CODE
import com.tech.bazaar.template.base.LogManager
import com.tech.bazaar.template.base.abconfig.BazaarABConfig
import com.tech.bazaar.template.base.model.BazaarUser
import com.tech.bazaar.template.base.model.ErrorResponse
import com.tech.bazaar.template.helper.storage.BazaarUserRepository
import com.tech.bazaar.template.login.model.LoginResponse
import com.tech.bazaar.template.retrofit.BackendApiService
import com.tech.bazaar.template.retrofit.RefreshTokenExpiredException
import dagger.Lazy
import dagger.hilt.android.qualifiers.ApplicationContext
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import retrofit2.Call
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ResponseInterceptor @Inject constructor(
    @ApplicationContext private val context: Context,
    private val backendApiService: Lazy<BackendApiService>,
    private val userRepository: BazaarUserRepository,
    private val abConfig: BazaarABConfig
) : Interceptor {

    private val Log = LogManager.getInstance(javaClass.simpleName)

    override fun intercept(chain: Interceptor.Chain): Response {
        val request: Request = chain.request()
        val response = chain.proceed(request)
        Log.i("Http Code: ${response.code}")

        when (response.code) {
            401 -> {
                if (!userRepository.user?.refreshToken.isNullOrEmpty()) {
                    val errorCode = parseErrorCode(response)
                    Log.i("Error Code: ${errorCode}")
                    if (errorCode == REFRESH_TOKEN_EXPIRED_CODE) {
                        Log.i("Refresh Token Expired!")
                        throw RefreshTokenExpiredException()
                    } else {
                        Log.i("Access Token Expired!")
                        return retryWithFreshToken(request, chain)
                    }
                }
            }
        }
        return response
    }

    private fun parseErrorCode(response: Response): Int {
        return try {
            val type = object : TypeToken<ErrorResponse>() {}.type
            val errorResponse: ErrorResponse? =
                Gson().fromJson(response.body?.charStream(), type)
            errorResponse?.code ?: 0
        } catch (exception: Exception) {
            Log.e("Error code exception: ", exception)
            return 0
        }
    }

    private fun retryWithFreshToken(req: Request, chain: Interceptor.Chain): Response {
        Log.i("Retrying with new token")
        val newToken = refreshToken()
        val newRequest: Request
        newRequest = req.newBuilder().header(AUTHORIZATION, "$BEARER $newToken").build()
        return chain.proceed(newRequest)
    }

    private fun refreshToken(): String {
        val phone = userRepository.user?.phone ?: ""
        val refreshToken = userRepository.user?.refreshToken ?: ""
        var token = userRepository.user?.token ?: ""

        try {
            val call: Call<LoginResponse> =
                backendApiService.get().renewAccessToken(RenewTokenModel(phone, refreshToken))

            val response = call.execute()
            if (response.isSuccessful && response.body() != null) {
                Log.i("Renew token success!")
                val data = response.body() as LoginResponse
                val user = userRepository.user
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
                            expiresAt = data.expiresAt
                        )

                    if (data.refreshToken.isNotEmpty()) {
                        updatedUser.refreshToken = data.refreshToken
                    } else {
                        updatedUser.refreshToken = refreshToken
                    }
                    userRepository.saveUser(updatedUser)
                }
                token = data.token
            }
        } catch (error: IOException) {
            Log.e("Renew Exception: ", error)
        }
        return token
    }
}