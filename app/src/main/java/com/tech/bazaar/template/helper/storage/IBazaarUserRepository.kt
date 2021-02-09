package com.tech.bazaar.template.helper.storage

import com.tech.bazaar.template.base.model.BazaarUser

interface IBazaarUserRepository{

    /**
     * get bazaar user
     */
    val user: BazaarUser?

    /**
     * Save Bazaar User
     */
    fun saveUser(bazaarUser: BazaarUser)

    /**
     * Get Bazaar User
     */
    fun getBazaarUser(): BazaarUser?

    /**
     * Call this function to check if user is logged in
     */
    fun isLoggedIn(): Boolean

    /**
     * Call this function to clear cache of User Preference
     */
    fun clearUserCache()

    /**
     * Call this function to Logout current user
     */
    fun onLogout()
}