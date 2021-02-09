package com.tech.bazaar.template.di

import android.content.Context
import com.tech.bazaar.template.analytics.AnalyticsInitializer
import com.tech.bazaar.template.analytics.AppEvents
import com.tech.bazaar.template.base.FireBaseGet
import com.tech.bazaar.template.base.abconfig.BazaarABConfig
import com.tech.bazaar.template.base.abconfig.BazaarAbTestRemoteConfig
import com.tech.bazaar.template.base.manager.IRepoManager
import com.tech.bazaar.template.environment.AppEnvironment
import com.tech.bazaar.template.helper.storage.IBazaarUserRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import org.greenrobot.eventbus.EventBus
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
class ConfigModule {

    @Singleton
    @Provides
    fun provideAnalyticsInitializer(
        @ApplicationContext context: Context,
        eventBus: EventBus,
        appEnvironment: AppEnvironment,
        bazaarUserRepository: IBazaarUserRepository
    ): AnalyticsInitializer {
        return AnalyticsInitializer(context, eventBus, appEnvironment, bazaarUserRepository)
    }

    @Provides
    fun provideEventBus(): EventBus {
        return EventBus.getDefault()
    }

    @Provides
    fun provideAppEvents(eventBus: EventBus): AppEvents {
        return AppEvents(eventBus)
    }

    @Singleton
    @Provides
    fun provideRemoteConfig(): BazaarAbTestRemoteConfig {
        return BazaarAbTestRemoteConfig()
    }
    @Provides
    fun provideConfig(remoteConfig: BazaarAbTestRemoteConfig): BazaarABConfig {
        return BazaarABConfig(remoteConfig)
    }

    @Provides
    fun provideFireBaseGet(): FireBaseGet {
        return FireBaseGet()
    }
}