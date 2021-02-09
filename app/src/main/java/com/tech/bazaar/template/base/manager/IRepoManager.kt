package com.tech.bazaar.template.base.manager

import com.tech.bazaar.template.helper.storage.IBazaarUserRepository
import com.tech.bazaar.template.login.model.RotateTokenModel
import com.tech.bazaar.template.login.model.LoginModel
import com.tech.bazaar.template.login.model.LoginResponse
import com.tech.bazaar.template.login.repo.ILoginRepository
import retrofit2.Response

interface IRepoManager : ILoginRepository, IBazaarUserRepository {
}