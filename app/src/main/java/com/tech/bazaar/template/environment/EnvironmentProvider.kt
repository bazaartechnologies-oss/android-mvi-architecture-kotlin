package com.tech.bazaar.template.environment


interface EnvironmentProvider {

    fun forceEnvironment(environment: AppEnvironment)

    fun getEnvironment(): AppEnvironment
}
