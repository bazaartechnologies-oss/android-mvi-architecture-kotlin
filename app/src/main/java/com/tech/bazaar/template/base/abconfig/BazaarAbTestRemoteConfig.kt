package com.tech.bazaar.template.base.abconfig

import com.apptimize.ApptimizeVar

class BazaarAbTestRemoteConfig {

    fun getBoolean(key: String): Boolean {
        return ApptimizeVar.createBoolean(key, false).value()
    }

}