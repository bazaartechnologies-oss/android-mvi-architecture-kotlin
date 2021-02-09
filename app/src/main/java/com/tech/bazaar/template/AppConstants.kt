package com.tech.bazaar.template

object BazaarConstants {
    const val BACKEND_URL = "https://bazaar-tech/"
    const val AUTHORIZATION = "Authorization"
    const val LEGACY_TOKEN = "sdad"
    const val CLIENT_KEY = "sdasd"
    const val USER_CLIENT_KEY = "akjkdfjds"
    const val CLIENT_KEY_HEADER = "$CLIENT_KEY: $USER_CLIENT_KEY"
    const val BEARER = "Bearer"
    const val REFRESH_TOKEN_EXPIRED_CODE = 1001
    const val TIME_FORMAT = "yyyy-MM-dd HH:mm:ss"
}

object SharedConstants {
    const val USER_KEY = "bazaarUserKey"
    const val USER_PREFERENCE = "UserPreference"
}

object EventConstants {
    const val EVENT_APP_OPEN = "app_open"
}

object PropertiesConstants {
    private const val PREFIX = "BT_"
    const val PROPERTY_IS_LOGGED_IN = PREFIX + "IsLoggedIn"
}