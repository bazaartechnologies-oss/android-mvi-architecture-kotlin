package com.tech.bazaar.template.base

interface BazaarDeviceIdentifier {
    fun getDeviceId(): String
    fun getMacAddress(): String
}