package com.raven.ravenandroidsdk.models

import com.google.gson.annotations.SerializedName

internal data class Device (

    @SerializedName("fcm_token")
    var fcmToken: String? = null,

    @SerializedName("platform")
    var platform: String? = null,

    @SerializedName("notifications_disabled")
    var disabled: Boolean? = null,

    @SerializedName("device_sid")
    var deviceSid: String? = null
)