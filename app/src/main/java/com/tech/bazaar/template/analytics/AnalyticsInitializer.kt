package com.tech.bazaar.template.analytics

import android.content.Context
import android.os.Bundle
import com.apptimize.Apptimize
import com.apptimize.ApptimizeOptions
import com.google.firebase.analytics.FirebaseAnalytics
import com.tech.bazaar.template.BuildConfig
import com.tech.bazaar.template.environment.AppEnvironment
import com.tech.bazaar.template.helper.storage.IBazaarUserRepository
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe

class AnalyticsInitializer(
    private val context: Context,
    private val eventBus: EventBus,
    private val appEnvironment: AppEnvironment,
    private val bazaarUserRepository: IBazaarUserRepository
) {

    private lateinit var fireabaseAnalytics: FirebaseAnalytics

    fun start() {
        eventBus.register(this)
        fireabaseAnalytics = FirebaseAnalytics.getInstance(context)
        setupApptimize()
    }

    private fun setupApptimize() {
        val token = appEnvironment.apptimizeKey()
        val options = ApptimizeOptions()
            .setVisualChangesEnabled(false)

        options.logLevel = when {
            BuildConfig.DEBUG -> ApptimizeOptions.LogLevel.VERBOSE
            BuildConfig.DEBUG -> ApptimizeOptions.LogLevel.DEBUG
            else -> ApptimizeOptions.LogLevel.OFF
        }
        Apptimize.setup(context, token, options)
        Apptimize.setUserAttribute("android_version_code", BuildConfig.VERSION_CODE)
        Apptimize.setup(context, appEnvironment.apptimizeKey())
        setUserForApptimize()
    }

    private fun setUserForApptimize() {
        bazaarUserRepository.user ?.let { user ->
            Apptimize.setCustomerUserId(user.userId)
            Apptimize.setPilotTargetingId(user.userId)
            Apptimize.setUserAttribute("user_id", user.userId)
            Apptimize.setUserAttribute("first_name", user.firstName ?: "")
            Apptimize.setUserAttribute("last_name", user.lastName ?: "")
            Apptimize.setUserAttribute("email", user.email ?: "")
            Apptimize.setUserAttribute("mobile_number", user.phone ?: "")
            Apptimize.setUserAttribute("language", "en")

        }
    }
    
    @Subscribe
    fun trackFirebaseEvent(event: BaseEvent) {
        fireabaseAnalytics.logEvent(event.eventName, Bundle().apply {
            if (event.additionalProperties != null) {
                event.additionalProperties.forEach {
                    putString(it.key, it.value.toString())
                }
            }
        })
    }

}