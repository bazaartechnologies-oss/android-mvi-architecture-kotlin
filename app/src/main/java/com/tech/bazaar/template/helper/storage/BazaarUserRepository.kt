package com.tech.bazaar.template.helper.storage

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.tech.bazaar.template.AppApplication
import com.tech.bazaar.template.SharedConstants.USER_KEY
import com.tech.bazaar.template.base.model.BazaarUser
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BazaarUserRepository @Inject constructor(
    @ApplicationContext private val context: Context,
    private val sharedHelper: SharedHelper
): IBazaarUserRepository {

    override var user: BazaarUser? = null
        private set


    init {
        user = getBazaarUser()
    }

    override fun saveUser(bazaarUser: BazaarUser) {
        user = bazaarUser // update cached user
        val preferences = (context as AppApplication).getUserPreferences()
        val editor: SharedPreferences.Editor
        editor = preferences.edit()
        editor.putString(USER_KEY, Gson().toJson(bazaarUser).toString())
        editor.apply()
        editor.commit()
    }

    override fun getBazaarUser(): BazaarUser? {
        val userPreference = (context as AppApplication).getUserPreferences()
        val userJson = userPreference.getString(USER_KEY, "");
        return Gson().fromJson(userJson, BazaarUser::class.java)
    }

    override fun isLoggedIn(): Boolean {
        val accessToken = user?.token
        val authToken = user?.authToken
        val userName = user?.firstName

        if ((accessToken.isNullOrEmpty() && authToken.isNullOrEmpty()) || userName.isNullOrEmpty()) {
            onLogout()
            return false
        }
        return true
    }

    override fun clearUserCache() {
        user = null
    }

    override fun onLogout() {
        clearUserCache()
        sharedHelper.clearSharedPreferences()
    }


}