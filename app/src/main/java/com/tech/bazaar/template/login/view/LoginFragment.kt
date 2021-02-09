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
import com.tech.bazaar.template.R
import com.tech.bazaar.template.base.model.ApiState
import com.tech.bazaar.template.base.view.BaseFragment
import com.tech.bazaar.template.databinding.FragmentLoginBinding
import com.tech.bazaar.template.dialog.BazaarToast
import com.tech.bazaar.template.extention.hide
import com.tech.bazaar.template.extention.show
import com.tech.bazaar.template.extention.showMessageInToastCenter
import com.tech.bazaar.template.login.model.LoginResponseModel
import com.tech.bazaar.template.login.viewmodel.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : BaseFragment() {

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
        observeLoginLiveData()
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
            loginViewModel.login()
        }
    }

    private fun observeLoginLiveData() {

        val observer = Observer<LoginResponseModel> {

            when (it?.apiState) {
                ApiState.IN_PROGRESS -> {
                    binding.loginProgressBar.show()
                }

                ApiState.FAILURE -> {
                    binding.loginProgressBar.hide()
                    toast = binding.loginRoot.showMessageInToastCenter(context, it.message)
                }

                ApiState.SUCCESS -> {
                    binding.loginProgressBar.hide()
//                    navigate()
                }
                else -> {
                }
            }
        }

        loginViewModel
            .loginLiveData
            .observe(viewLifecycleOwner, observer)
    }

    override fun setStatusBarColor(): Int {
        return ContextCompat.getColor(requireContext(), R.color.white)
    }

    override fun onPause() {
        toast?.cancelToast()
        super.onPause()
    }

}