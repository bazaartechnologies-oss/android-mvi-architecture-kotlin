package com.tech.bazaar.template.base.model

import java.io.Serializable

data class BazaarUser(
    val userId: String = "",
    val firstName: String = "",
    val lastName: String = "",
    val phone: String = "",
    val company: String = "",
    val email: String = "",
    val token: String = "",
    val accessToken: String? = "",
    val referralCode: String = "",
    var authToken: String? = "",
    var refreshToken: String? = "",
    val expiresAt: String? = ""
) : Serializable