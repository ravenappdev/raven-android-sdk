package com.raven.ravenandroidsdk.api.dto

import com.google.gson.annotations.SerializedName
import com.raven.ravenandroidsdk.models.Device

internal class RavenMessage {

    @SerializedName("event")
    var event: String? = ""

    @SerializedName("locale")
    var locale: String? = "en"

    @SerializedName("user")
    var user: Recipient? = null
}

internal class Recipient {

    @SerializedName("user_id")
    var userId: String? = null

    @SerializedName("mobile")
    var mobile: String? = null

    @SerializedName("email")
    var email: String? = null

    @SerializedName("devices")
    var devices: ArrayList<Device>? = null
}