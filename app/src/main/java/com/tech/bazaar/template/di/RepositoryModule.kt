package com.tech.bazaar.template.di

import com.tech.bazaar.template.login.viewmodel.LoginRepository
import com.tech.bazaar.template.retrofit.BackendApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.scopes.ActivityRetainedScoped

@Module
@InstallIn(ActivityRetainedComponent::class)
object RepositoryModule {

    @Provides
    @ActivityRetainedScoped
    fun provideLoginRepository(
        apiService: BackendApiService
    ): LoginRepository {
        return LoginRepository(apiService)
    }

}