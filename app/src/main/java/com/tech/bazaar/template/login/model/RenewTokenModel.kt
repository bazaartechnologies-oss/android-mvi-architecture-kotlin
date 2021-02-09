package com.tech.bazaar.template.login.model

import com.google.gson.annotations.SerializedName

data class RenewTokenModel(
    @SerializedName("username")
    val username: String,
    @SerializedName("token")
    val refreshToken: String
)