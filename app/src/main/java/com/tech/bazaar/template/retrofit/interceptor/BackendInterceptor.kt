package com.tech.bazaar.template.retrofit.interceptor

import com.tech.bazaar.template.BazaarConstants.AUTHORIZATION
import com.tech.bazaar.template.BazaarConstants.BEARER
import com.tech.bazaar.template.BazaarConstants.LEGACY_TOKEN
import com.tech.bazaar.template.base.abconfig.BazaarABConfig
import com.tech.bazaar.template.helper.storage.IBazaarUserRepository
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class BackendInterceptor @Inject constructor(
    private val userRepository: IBazaarUserRepository,
    private val abConfig: BazaarABConfig
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val authorization: String = userRepository.user?.token ?: ""
        val authToken: String = userRepository.user?.authToken ?: ""
        val legacyToken = userRepository.user?.accessToken ?: ""
        val token = if (!userRepository.user?.refreshToken.isNullOrEmpty()) authToken else authorization
        return chain.proceed(
            chain.request().newBuilder()
                .header(AUTHORIZATION, "$BEARER $token")
                .header(LEGACY_TOKEN, legacyToken)
                .build()
        )
    }
}