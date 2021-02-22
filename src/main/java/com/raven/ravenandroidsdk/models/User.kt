package com.raven.ravenandroidsdk.models

import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import com.raven.ravenandroidsdk.PREF_RAVEN_USER
import com.raven.ravenandroidsdk.utils.Prefs

internal data class User (

    @SerializedName("user_id")
    var userId: String,

    @SerializedName("preferences")
    var preference: Preferences? = null,

    @SerializedName("devices")
    var devices: ArrayList<Device>? = null,

    @SerializedName("mobile")
    var mobile: String? = null,

    @SerializedName("email")
    var email: String? = null,

    @SerializedName("whatsapp_mobile")
    var whatsAppMobile: String? = null
)

internal fun getCurrentUser(): User? {
    return Gson().fromJson(Prefs.getString(PREF_RAVEN_USER, null), User::class.java)
}

internal fun setCurrentUser(user: User?) {
    Prefs.putString(PREF_RAVEN_USER, Gson().toJson(user))
}