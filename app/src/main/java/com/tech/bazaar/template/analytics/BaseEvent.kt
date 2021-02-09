package com.tech.bazaar.template.analytics

data class BaseEvent(
    val eventName: String,
    val additionalProperties: MutableMap<String, Any>?
)