package com.tech.bazaar.template.base

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

class CoroutineDispatcher @Inject constructor() {

    fun io(): CoroutineDispatcher {
        return Dispatchers.IO
    }

    fun main(): CoroutineDispatcher {
        return Dispatchers.Main
    }
}