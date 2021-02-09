package com.tech.bazaar.template.helper.storage

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.tech.bazaar.template.AppApplication
import com.tech.bazaar.template.SharedConstants.USER_PREFERENCE
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SharedHelper @Inject constructor(
    @ApplicationContext val context: Context
) {
    companion object {
        private lateinit var userPreferences: SharedPreferences
        private lateinit var orderPreferences: SharedPreferences
        private lateinit var revisitPreferences: SharedPreferences

    }

    fun clearSharedPreferences() {
        userPreferences = (context as AppApplication).getUserPreferences()
        userPreferences.edit().clear().apply()
    }
}

fun Application.getUserPreferences(): SharedPreferences {
    return applicationContext.getSharedPreferences(USER_PREFERENCE, Context.MODE_PRIVATE)
}