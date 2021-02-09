package com.tech.bazaar.template.environment

import javax.inject.Inject

class AppEnvironmentProvider @Inject constructor(
) : EnvironmentProvider {

    private val buildEnvironment = getBuildEnvironment()
    private var forcedEnvironment: AppEnvironment? = null

    override fun forceEnvironment(environment: AppEnvironment) {
        forcedEnvironment = environment
    }

    override fun getEnvironment(): AppEnvironment {
        return buildEnvironment
    }

    private fun getBuildEnvironment(): AppEnvironment = ProdEnvironment

}
