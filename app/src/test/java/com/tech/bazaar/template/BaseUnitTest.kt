package com.tech.bazaar.template

import android.app.Application
import com.tech.bazaar.template.base.CoroutineDispatcher
import com.tech.bazaar.template.base.abconfig.BazaarABConfig
import com.tech.bazaar.template.base.manager.IRepoManager
import com.tech.bazaar.template.base.manager.RepoManager
import com.tech.bazaar.template.helper.storage.BazaarUserRepository
import com.tech.bazaar.template.helper.storage.IBazaarUserRepository
import com.tech.bazaar.template.login.repo.ILoginRepository
import com.tech.bazaar.template.login.repo.ILoginService
import com.tech.bazaar.template.login.repo.LoginRepository
import com.tech.bazaar.template.login.repo.LoginService
import com.tech.bazaar.template.retrofit.BackendApiService
import org.mockito.Mock

open class BaseUnitTest {
    @Mock
    lateinit var apiService: BackendApiService

    @Mock
    lateinit var application: Application

    @Mock
    lateinit var abConfig: BazaarABConfig

    @Mock
    lateinit var coroutineDispatcher: CoroutineDispatcher

    @Mock
    lateinit var userRepository: IBazaarUserRepository

    lateinit var repoManager: IRepoManager
    private lateinit var loginRepository: ILoginRepository
    private lateinit var loginService: LoginService


    fun initializeBase() {
        loginService = LoginService(apiService)
        loginRepository = LoginRepository(loginService)
        repoManager = RepoManager(loginRepository, userRepository)
    }
}
