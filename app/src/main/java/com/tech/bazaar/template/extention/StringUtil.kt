package com.tech.bazaar.template.extention

import java.util.Locale
import javax.inject.Inject

class StringUtil @Inject constructor() {

    fun toSentenceCase(string: String) =
        "${string.substring(0, 1).toUpperCase(Locale.ROOT)}${
            string.substring(1).toLowerCase(Locale.ROOT)
        }"
}