package com.tech.bazaar.template.base

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.tech.bazaar.template.base.model.ErrorResponse
import okhttp3.ResponseBody
import javax.inject.Inject

class ResponseErrorParser @Inject constructor(
    private val gson: Gson
) {

    fun errorMessage(response: ResponseBody?): String {
        return try {
            val type = object : TypeToken<ErrorResponse>() {}.type
            val errorResponse: ErrorResponse? =
                gson.fromJson(response?.charStream(), type)
            if (errorResponse?.errors.isNullOrEmpty()) {
                ""
            } else {
                errorResponse?.errors?.get(0).orEmpty()
            }
        } catch (exception: Exception) {
            ""
        }
    }
}