package com.tech.bazaar.template.extention

import java.text.DecimalFormat

fun String.parseDouble(): Double {
    return try {
        this.toDoubleOrNull() ?: 0.0
    } catch (exception: NumberFormatException) {
        0.0
    }
}

fun getRoundPrice(price: String?): String {
    var cost = "0"

    if (price.isNullOrBlank())
        cost = "0"
    else if (price.startsWith("0"))
        cost = "0"
    else
        cost = price

    return formatNumber(cost)
}

fun formatNumber(price: String): String {

    if (price.isBlank()) {
        return "0"
    }

    if (price.toDouble() == 0.0) {
        return "0"
    }

    val formatted = DecimalFormat("#,###.##")
    return formatted.format(price.toDouble())
}

fun Int.doubleDigitString() = if (this >= 10) this.toString() else "0$this"
