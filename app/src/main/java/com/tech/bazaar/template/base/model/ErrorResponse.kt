package com.tech.bazaar.template.base.model

import com.google.gson.annotations.SerializedName

class ErrorResponse {
    @SerializedName("message")
    var message: String = ""
    @SerializedName("errors")
    var errors: List<String> = listOf()
    @SerializedName("code")
    var code: Int = 0
}