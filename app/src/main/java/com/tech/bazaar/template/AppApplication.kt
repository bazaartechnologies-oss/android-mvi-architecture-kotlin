package com.tech.bazaar.template

import android.app.Application
import coil.ImageLoader
import coil.ImageLoaderFactory
import coil.util.CoilUtils
import coil.util.DebugLogger
import com.google.firebase.FirebaseApp
import com.tech.bazaar.template.analytics.AnalyticsInitializer
import com.tech.bazaar.template.retrofit.interceptor.ResponseHeaderInterceptor
import dagger.hilt.android.HiltAndroidApp
import okhttp3.Dispatcher
import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltAndroidApp
class AppApplication : Application(), ImageLoaderFactory {

    @Inject
    lateinit var analyticsInitializer: AnalyticsInitializer

    companion object {
        lateinit var appContext: Application
    }

    override fun onCreate() {
        super.onCreate()
        appContext = this
        analyticsInitializer.start()
        FirebaseApp.initializeApp(this)

    }

    override fun newImageLoader(): ImageLoader {
        return ImageLoader.Builder(this)
            .availableMemoryPercentage(0.25)
            .crossfade(true)
            .okHttpClient {
                val cacheControlInterceptor =
                    ResponseHeaderInterceptor("Cache-Control", "max-age=604800,public")

                val dispatcher = Dispatcher().apply { maxRequestsPerHost = maxRequests }

                OkHttpClient.Builder()
                    .cache(CoilUtils.createDefaultCache(this))
                    .dispatcher(dispatcher)
                    .writeTimeout(20, TimeUnit.SECONDS)
                    .readTimeout(20, TimeUnit.SECONDS)
                    .addNetworkInterceptor(cacheControlInterceptor)
                    .build()
            }
            .apply {
                if (BuildConfig.DEBUG) {
                    logger(DebugLogger())
                }
            }
            .build()
    }
}