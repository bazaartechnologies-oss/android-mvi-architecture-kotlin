package com.tech.bazaar.template.di

import android.content.Context
import com.google.gson.Gson
import com.tech.bazaar.template.base.BazaarDeviceIdentifier
import com.tech.bazaar.template.base.BazaarDeviceIdentifierImpl
import com.tech.bazaar.template.base.LocationManager
import com.tech.bazaar.template.dialog.BazaarGeneralDialogFactory
import com.tech.bazaar.template.environment.AppEnvironment
import com.tech.bazaar.template.environment.AppEnvironmentProvider
import com.tech.bazaar.template.environment.EnvironmentProvider
import com.tech.bazaar.template.helper.ImageSequentialLoader
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.Calendar

@Module
@InstallIn(ApplicationComponent::class)
class ApplicationModule {

    @Provides
    fun provideImageLoader(): ImageSequentialLoader {
        return ImageSequentialLoader()
    }

    @Provides
    fun providesCalendar()
            : Calendar {
        return Calendar.getInstance()
    }

    @Provides
    fun providesGson(): Gson {
        return Gson()
    }

    @Provides
    fun provideBazaarDialogFactory(
        @ApplicationContext context: Context
    ): BazaarGeneralDialogFactory {
        return BazaarGeneralDialogFactory(context.resources, context)
    }

    @Provides
    fun provideLocationManager(
        @ApplicationContext context: Context
    ): LocationManager {
        return LocationManager(context)
    }

    @Provides
    fun providesDeviceIdentifier(deviceIdentifierImpl: BazaarDeviceIdentifierImpl)
            : BazaarDeviceIdentifier {
        return deviceIdentifierImpl
    }

    @Provides
    fun provideEnvironmentProvider(appEnvironmentProvider: AppEnvironmentProvider): EnvironmentProvider {
        return appEnvironmentProvider
    }

    @Provides
    fun provideEnvironment(appEnvironmentProvider: AppEnvironmentProvider): AppEnvironment {
        return appEnvironmentProvider.getEnvironment()
    }

}