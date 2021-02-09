package com.tech.bazaar.template.environment


object ProdEnvironment : AppEnvironment {

    override fun apptimizeKey(): String {
        return ""
    }

    override fun isApptimizeEnabled(): Boolean = true

}