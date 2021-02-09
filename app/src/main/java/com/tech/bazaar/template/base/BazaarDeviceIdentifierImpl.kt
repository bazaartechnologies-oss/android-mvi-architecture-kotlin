package com.tech.bazaar.template.base

import android.content.Context
import com.google.android.gms.ads.identifier.AdvertisingIdClient
import com.google.firebase.crashlytics.FirebaseCrashlytics
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class BazaarDeviceIdentifierImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : BazaarDeviceIdentifier {

    private var advertisingId: String = "N/A"

    init {
        determineAdvertisingInfo()
    }

    override fun getDeviceId(): String {
        return advertisingId
    }

    override fun getMacAddress(): String {
        return "N/A"
    }

    private fun determineAdvertisingInfo() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val info = AdvertisingIdClient.getAdvertisingIdInfo(context)
                advertisingId = if (!info.isLimitAdTrackingEnabled) {
                    info.id
                } else {
                    "N/A"
                }
            } catch (ex: Exception) {
                FirebaseCrashlytics.getInstance().recordException(ex)
            }
        }
    }
}