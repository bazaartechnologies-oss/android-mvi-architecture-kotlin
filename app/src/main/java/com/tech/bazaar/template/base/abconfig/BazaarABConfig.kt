package com.tech.bazaar.template.base.abconfig

import com.apptimize.ApptimizeVar
import com.tech.bazaar.template.BuildConfig
import javax.inject.Inject

class BazaarABConfig @Inject constructor(remoteConfig: BazaarAbTestRemoteConfig) {

    /**
     * Dynamic variable for AB Testing
     */
    val dynamicVariableTest: Boolean
        get() {
            return bannerTest.value() || BuildConfig.DEBUG
        }


    companion object {
        // Initialize Dynamic Variables
        private val bannerTest = ApptimizeVar.createBoolean("dynamic_variable", false)
    }
}
