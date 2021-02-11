package com.tech.bazaar.template.login

import com.nhaarman.mockitokotlin2.verify
import com.tech.bazaar.template.BaseUnitTest
import com.tech.bazaar.template.login.model.LoginModel
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations

class LoginRepositoryTest: BaseUnitTest() {

    @Mock
    lateinit var loginModel: LoginModel

    @BeforeEach
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        initializeBase()
    }

    @Test
    fun testLogin() = runBlocking<Unit> {
        //act
        repoManager.login(loginModel)
        //assert
        verify(apiService).login(loginModel)
    }

}