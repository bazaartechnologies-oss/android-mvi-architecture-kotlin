package com.tech.bazaar.template.login.model

import com.google.gson.annotations.SerializedName

data class RotateTokenModel(
    @SerializedName("username")
    val username: String,
    @SerializedName("token")
    val accessToken: String
)