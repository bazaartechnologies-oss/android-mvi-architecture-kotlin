package com.tech.bazaar.template.base.view

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import com.tech.bazaar.template.R
import com.tech.bazaar.template.analytics.AppEvents
import com.tech.bazaar.template.login.viewmodel.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class HomeActivity : AppCompatActivity() {

    @Inject
    lateinit var appEvents: AppEvents

    private val loginViewModel: LoginViewModel by viewModels()

    private val navController by lazy { findNavController(R.id.nav_host_fragment) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        showStatusBar()
        setupUi()
    }

    private fun setupUi() {
        if (loginViewModel.isLoggedIn()) {
            loginViewModel.rotateTokenIfRequired()
            navigateToCustomerView()
        } else {
            openLogin()
        }
    }

    private fun openLogin() {
        navController.navigate(R.id.loginFragment)
    }

    private fun navigateToCustomerView() {
        val currentDestination = navController.currentDestination?.id
        if (currentDestination == R.id.loginFragment) {
//            navController.navigate(
//                R.id.action_loginFragment_to_customerFragment
//            )
        }
    }

    private fun showStatusBar() {
        window?.clearFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                window.statusBarColor = Color.BLACK
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean =
        findNavController(R.id.nav_host_fragment).navigateUp()

}