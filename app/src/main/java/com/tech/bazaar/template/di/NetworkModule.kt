package com.tech.bazaar.template.di

import com.readystatesoftware.chuck.ChuckInterceptor
import com.tech.bazaar.template.AppApplication
import com.tech.bazaar.template.BazaarConstants.BACKEND_URL
import com.tech.bazaar.template.BuildConfig
import com.tech.bazaar.template.base.abconfig.BazaarABConfig
import com.tech.bazaar.template.base.manager.IRepoManager
import com.tech.bazaar.template.helper.storage.IBazaarUserRepository
import com.tech.bazaar.template.retrofit.BackendApiService
import com.tech.bazaar.template.retrofit.interceptor.BackendInterceptor
import com.tech.bazaar.template.retrofit.interceptor.NoConnectionInterceptor
import com.tech.bazaar.template.retrofit.interceptor.ResponseInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

@Module
@InstallIn(ApplicationComponent::class)
object NetworkModule {

    private const val READ_TIMEOUT = 15L
    private const val WRITE_TIMEOUT = 15L

    @Provides
    fun provideOkHttpClientForBackend(
        cache: Cache,
        httpLoggingInterceptor: HttpLoggingInterceptor,
        noConnectionInterceptor: NoConnectionInterceptor,
        backendInterceptor: BackendInterceptor,
        responseInterceptor: ResponseInterceptor,
        ): OkHttpClient {
        return OkHttpClient.Builder()
            .writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)
            .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
            .addInterceptor(httpLoggingInterceptor)
            .addInterceptor(noConnectionInterceptor)
            .addInterceptor(backendInterceptor)
            .addInterceptor(responseInterceptor)
            .addInterceptor(ChuckInterceptor(AppApplication.appContext))
            .cache(cache)
            .build()
    }

    @Provides
    fun provideCache(): Cache {
        val cacheSize = 10 * 1024 * 1024
        return Cache(AppApplication.appContext.cacheDir, cacheSize.toLong())
    }

    @Provides
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor {
        val httpLoggingInterceptor = HttpLoggingInterceptor()
        if (BuildConfig.DEBUG) {
            httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        } else {
            httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BASIC
        }
        return httpLoggingInterceptor
    }

    @Provides
    fun provideBackendRetrofit(
        okHttpClient: OkHttpClient,
        gsonFactory: GsonConverterFactory
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BACKEND_URL)
            .client(okHttpClient)
            .addConverterFactory(gsonFactory)
            .build()
    }

    @Provides
    fun provideGsonConverterFactory(): GsonConverterFactory = GsonConverterFactory.create()

    @Provides
    fun provideBackendInterceptor(userRepository: IBazaarUserRepository, abConfig: BazaarABConfig): BackendInterceptor {
        return BackendInterceptor(userRepository, abConfig)
    }

    @Provides
    fun provideBackendApiService(retrofit: Retrofit): BackendApiService {
        return retrofit.create(BackendApiService::class.java)
    }

}