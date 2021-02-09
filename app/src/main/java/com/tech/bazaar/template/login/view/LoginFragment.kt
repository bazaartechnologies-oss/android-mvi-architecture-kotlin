package com.tech.bazaar.template.login.view

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.tech.bazaar.template.R
import com.tech.bazaar.template.base.model.ApiState
import com.tech.bazaar.template.base.mvicore.IView
import com.tech.bazaar.template.base.view.BaseFragment
import com.tech.bazaar.template.databinding.FragmentLoginBinding
import com.tech.bazaar.template.dialog.BazaarToast
import com.tech.bazaar.template.extention.hide
import com.tech.bazaar.template.extention.show
import com.tech.bazaar.template.extention.showMessageInToastCenter
import com.tech.bazaar.template.login.action.LoginIntent
import com.tech.bazaar.template.login.state.LoginState
import com.tech.bazaar.template.login.viewmodel.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginFragment : BaseFragment(), IView<LoginState> {

    private lateinit var binding: FragmentLoginBinding

    private val loginViewModel: LoginViewModel by viewModels()

    private var toast: BazaarToast? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentLoginBinding.inflate(inflater)
        initBinding()
        initUI()
        onClicked()
        observeLoginState()
        return binding.root
    }

    private fun initBinding() {
        binding.mViewModel = loginViewModel
    }

    private fun initUI() {
        binding.passwordInput.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {}
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (s.isNotEmpty()) {
                    if (binding.passwordInput.transformationMethod == PasswordTransformationMethod.getInstance()) {
                        toggleShow()
                    } else {
                        toggleShow(false)
                    }
                } else {
                    binding.show.hide()
                    binding.hide.hide()
                }
            }
        })
    }

    private fun toggleShow(show: Boolean = true) {
        binding.show.show(show)
        binding.hide.show(!show)
    }

    private fun onClicked() {
        binding.show.setOnClickListener {
            binding.passwordInput.transformationMethod =
                HideReturnsTransformationMethod.getInstance()
            binding.passwordInput.text?.length?.let {
                binding.passwordInput.setSelection(it)
            }
        }

        binding.hide.setOnClickListener {
            binding.passwordInput.transformationMethod = PasswordTransformationMethod.getInstance()
            binding.passwordInput.text?.length?.let {
                binding.passwordInput.setSelection(it)
            }
        }

        binding.loginBtn.setOnClickListener {
            mainScope.launch {
                loginViewModel.intents.send(LoginIntent.DoLogin)
            }
        }
    }

    private fun observeLoginState() {
        mainScope.launch {
            // Triggers the flow and starts listening for values
            loginViewModel.state.collect { uiState ->
                // New value received
                render(uiState)
            }
        }
    }

    override fun setStatusBarColor(): Int {
        return ContextCompat.getColor(requireContext(), R.color.white)
    }

    override fun onPause() {
        toast?.cancelToast()
        super.onPause()
    }

    override fun render(uiState: LoginState) {
        when (uiState) {
            is LoginState.Idle -> {
                binding.loginProgressBar.hide()
            }
            is LoginState.Loading -> {
                binding.loginProgressBar.show()
            }
            is LoginState.LoginSuccess -> {
                binding.loginProgressBar.hide()
//                    navigate()
            }
            is LoginState.LoginError -> {
                binding.loginProgressBar.hide()
                toast = binding.loginRoot.showMessageInToastCenter(context, uiState.error)
            }
        }
    }

}