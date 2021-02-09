package com.tech.bazaar.template.base.view

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.tech.bazaar.template.AppApplication
import com.tech.bazaar.template.extention.hideKeyboardFromCurrentFocus

abstract class BaseFragment : Fragment() {

    lateinit var navController: NavController

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        if (setStatusBarColor() != 0) {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                activity?.window?.statusBarColor = setStatusBarColor()
            }
        }
        super.onActivityCreated(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(
            view
        )
    }

    abstract fun setStatusBarColor(): Int

    override fun onPause() {
        super.onPause()
        hideKeyboardFromCurrentFocus()
    }

    fun getApplication(): AppApplication {
        return requireActivity().applicationContext as AppApplication
    }

    fun getHomeActivity(): HomeActivity {
        return requireActivity() as HomeActivity
    }
}
