package com.tech.bazaar.template.login.model

import com.google.gson.annotations.SerializedName

data class LoginResponse(
    @SerializedName("token")
    val token: String,
    @SerializedName("legacyToken")
    val legacyToken: String?,
    @SerializedName("userInfo")
    val userInfo: UserInfo,
    @SerializedName("refreshToken")
    val refreshToken: String,
    @SerializedName("expiresAt")
    val expiresAt: String = ""
) {
    data class UserInfo(
        @SerializedName("id")
        val id: String,
        @SerializedName("fullName")
        val fullName: String,
        @SerializedName("phoneNumber")
        val phone: String,
        @SerializedName("nationalId")
        val nationalId: String
    )

}