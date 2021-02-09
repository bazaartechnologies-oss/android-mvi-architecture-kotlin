package com.tech.bazaar.template.di

import com.tech.bazaar.template.base.manager.IRepoManager
import com.tech.bazaar.template.base.manager.RepoManager
import com.tech.bazaar.template.helper.storage.BazaarUserRepository
import com.tech.bazaar.template.helper.storage.IBazaarUserRepository
import com.tech.bazaar.template.login.repo.ILoginRepository
import com.tech.bazaar.template.login.repo.ILoginService
import com.tech.bazaar.template.login.repo.LoginRepository
import com.tech.bazaar.template.login.repo.LoginService
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
    fun provideRepoManager(
        loginRepository: ILoginRepository,
        bazaarUserRepository: IBazaarUserRepository
    ): IRepoManager {
        return RepoManager(loginRepository, bazaarUserRepository)
    }

    @Provides
    @ActivityRetainedScoped
    fun provideLoginRepository(
        loginService: ILoginService
    ): ILoginRepository {
        return LoginRepository(loginService)
    }

    @Provides
    @ActivityRetainedScoped
    fun provideLoginService(
        apiService: BackendApiService
    ): ILoginService {
        return LoginService(apiService)
    }

}