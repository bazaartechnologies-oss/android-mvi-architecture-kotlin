package com.tech.bazaar.template.base.manager

import com.tech.bazaar.template.base.model.BazaarUser
import com.tech.bazaar.template.helper.storage.BazaarUserRepository
import com.tech.bazaar.template.helper.storage.IBazaarUserRepository
import com.tech.bazaar.template.login.model.RotateTokenModel
import com.tech.bazaar.template.login.model.LoginModel
import com.tech.bazaar.template.login.model.LoginResponse
import com.tech.bazaar.template.login.repo.ILoginRepository
import retrofit2.Response
import javax.inject.Inject

class RepoManager @Inject constructor(
    private val loginRepository: ILoginRepository,
    private val bazaarUserRepository: IBazaarUserRepository
    ) : IRepoManager {

    /**
     *  Login API
     */
    override suspend fun login(loginModel: LoginModel): Response<LoginResponse> {
        return loginRepository.login(loginModel)
    }

    override suspend fun rotateToken(tokenModel: RotateTokenModel): Response<LoginResponse> {
        return loginRepository.rotateToken(tokenModel)
    }

    override val user: BazaarUser?
        get() = bazaarUserRepository.user

    override fun saveUser(bazaarUser: BazaarUser) {
        bazaarUserRepository.saveUser(bazaarUser)
    }

    override fun getBazaarUser(): BazaarUser? {
        return bazaarUserRepository.getBazaarUser()
    }

    override fun isLoggedIn(): Boolean {
        return bazaarUserRepository.isLoggedIn()
    }

    override fun clearUserCache() {
        bazaarUserRepository.clearUserCache()
    }

    override fun onLogout() {
        bazaarUserRepository.onLogout()
    }
}