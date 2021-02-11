package com.tech.bazaar.template.login

import androidx.databinding.ObservableField
import com.tech.bazaar.template.login.model.RotateTokenModel
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.atLeast
import com.nhaarman.mockitokotlin2.doAnswer
import com.nhaarman.mockitokotlin2.never
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import com.tech.bazaar.template.BaseUnitTest
import com.tech.bazaar.template.base.model.BazaarUser
import com.tech.bazaar.template.login.action.LoginIntent
import com.tech.bazaar.template.login.model.LoginModel
import com.tech.bazaar.template.login.model.LoginResponse
import com.tech.bazaar.template.login.model.LoginResponse.UserInfo
import com.tech.bazaar.template.login.state.LoginState
import com.tech.bazaar.template.login.viewmodel.LoginViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.mockito.BDDMockito
import org.mockito.MockitoAnnotations
import retrofit2.Response
import java.io.IOException

class LoginViewModelTest: BaseUnitTest() {

    private lateinit var viewModel: LoginViewModel

    @BeforeEach
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        initializeBase()

        viewModel = LoginViewModel(application, repoManager, abConfig, coroutineDispatcher)

        whenever(coroutineDispatcher.main()).thenReturn(Dispatchers.Unconfined)
        whenever(coroutineDispatcher.io()).thenReturn(Dispatchers.Unconfined)
    }

    @Test
    fun test_isLoggedIn_true() {
        // arrange
        whenever(repoManager.isLoggedIn()).thenReturn(true)

        // act & assert
        Assertions.assertTrue(viewModel.isLoggedIn())
    }

    @Test
    fun test_isLoggedIn_false() {
        // arrange
        whenever(repoManager.isLoggedIn()).thenReturn(false)

        // act & assert
        Assertions.assertFalse(viewModel.isLoggedIn())
    }

    @Test
    fun test_login_when_password_empty() = runBlocking<Unit> {
        // arrange
        viewModel.phone = ObservableField(PHONE)
        viewModel.password = ObservableField("")

        // act
        viewModel.intents.send(LoginIntent.DoLogin)

        // assert
        verify(apiService, never()).login(any())
        assert(viewModel.state.value is LoginState.Idle)
    }

    @Test
    fun test_login_success() = runBlocking<Unit> {
        // arrange
        viewModel.phone = ObservableField(PHONE)
        viewModel.password = ObservableField(PASSWORD)

        whenever(repoManager.login(any())).thenReturn(
            Response.success(LoginResponse(TOKEN, LEGACY_TOKEN, userInfo(), ""))
        )

        // act
        viewModel.intents.send(LoginIntent.DoLogin)

        // assert
        verify(apiService).login(LoginModel(PHONE, PASSWORD))
        assert(viewModel.state.value is LoginState.LoginSuccess)
    }

    @Test
    fun test_login_failure() = runBlocking<Unit> {
        // arrange
        viewModel.phone = ObservableField(PHONE)
        viewModel.password = ObservableField(PASSWORD)

        whenever(repoManager.login(any())).thenReturn(
            Response.error(400, responseBody())
        )

        // act
        viewModel.intents.send(LoginIntent.DoLogin)

        // assert
        verify(apiService).login(LoginModel(PHONE, PASSWORD))
    }

    @Test
    fun test_login_exception() = runBlocking<Unit> {
        // arrange
        viewModel.phone = ObservableField(PHONE)
        viewModel.password = ObservableField(PASSWORD)
        doAnswer { throw IOException("") }.`when`(apiService).login(any())

        // act
        viewModel.intents.send(LoginIntent.DoLogin)

        // assert
        verify(apiService).login(LoginModel(PHONE, PASSWORD))
    }

    @Nested
    inner class RotateToken {

        @Test
        fun test_check_token_availability_available() = runBlocking<Unit> {
            // arrange
            whenever(repoManager.user).thenReturn(
                userModel
            )

            // act
            viewModel.rotateTokenIfRequired()

            // assert
            verify(userRepository, times(1)).user
            Assertions.assertEquals(repoManager.user, userModel)
        }

        @Test
        fun test_check_token_availability_success() = runBlocking<Unit> {
            // arrange
            val loginResponse = LoginResponse(TOKEN, LEGACY_TOKEN,
                UserInfo("", "", "", ""), TOKEN, "123")
            whenever(repoManager.user).thenReturn(
                userModelWithoutToken
            )
            whenever(repoManager.rotateToken(any())).thenReturn(
                Response.success(loginResponse)
            )

            // act
            viewModel.rotateTokenIfRequired()

            // assert
            verify(userRepository, atLeast(2)).user
            verify(apiService).rotateToken(any())
            verify(userRepository).saveUser(any())
            Assertions.assertEquals(repoManager.user, userModelWithoutToken)
        }

        @Test
        fun test_check_token_availability_failure() = runBlocking<Unit> {
            // arrange
            val tokenModel = RotateTokenModel(PHONE, TOKEN)
            whenever(userRepository.user).thenReturn(
                userModelWithoutToken
            )
            whenever(repoManager.rotateToken(any())).thenReturn(
                Response.error(400, responseBody())
            )

            // act
            viewModel.rotateTokenIfRequired()

            // assert
            verify(userRepository, atLeast(2)).user
            verify(apiService).rotateToken(any())
            verify(userRepository, never()).saveUser(any())
        }

        @Test
        fun test_check_token_availability_exception() = runBlocking<Unit> {
            // arrange
            val tokenModel = RotateTokenModel(PHONE, TOKEN)
            whenever(userRepository.user).thenReturn(
                BazaarUser(
                    phone = PHONE,
                    userId = ID,
                    token = "",
                    accessToken = TOKEN,
                    refreshToken = null
                )
            )
            BDDMockito.given(repoManager.rotateToken(any())).willAnswer {
                throw IOException("")
            }
            // act
            viewModel.rotateTokenIfRequired()

            // assert
            verify(apiService).rotateToken(any())
        }
    }

    private fun userInfo(): UserInfo {
        return UserInfo(ID, NAME, PHONE, "")
    }

    private fun responseBody() = ResponseBody.create("application/json".toMediaTypeOrNull(), "")

    val userModel = BazaarUser(
        userId = ID,
        token = TOKEN,
        accessToken = TOKEN,
        refreshToken = TOKEN
    )

    val userModelWithoutToken = BazaarUser(
        userId = ID,
        token = "",
        accessToken = TOKEN,
        refreshToken = ""
    )

    companion object {
        const val ID = "1"
        const val NAME = "test"
        const val PHONE = "123"
        const val PASSWORD = "123"
        const val TOKEN = "123"
        const val LEGACY_TOKEN = "123"
    }

}