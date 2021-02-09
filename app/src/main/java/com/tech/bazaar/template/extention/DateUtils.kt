package com.tech.bazaar.template.extention

import java.util.Calendar
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DateUtils @Inject constructor(
    private val calendar: Calendar
) {

    fun currentTimeMillis(): Long {
        return System.currentTimeMillis()
    }

}