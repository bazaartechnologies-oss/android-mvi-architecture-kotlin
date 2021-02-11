package com.tech.bazaar.template.retrofit.interceptor

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
        return chain.proceed(
            chain.request().newBuilder()
                .header("Replace with Header key", "Replace with header value")
                .build()
        )
    }
}