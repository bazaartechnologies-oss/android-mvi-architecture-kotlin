package com.tech.bazaar.template.analytics

import com.tech.bazaar.template.PropertiesConstants.PROPERTY_IS_LOGGED_IN
import org.greenrobot.eventbus.EventBus

class AppEvents(private val eventBus: EventBus) {

    fun onAppOpen(isLoggedIn: Boolean) {
        val properties = mutableMapOf<String, Any>()
        properties[PROPERTY_IS_LOGGED_IN] = isLoggedIn
        eventBus.post(BaseEvent("app_open", properties))
    }

}