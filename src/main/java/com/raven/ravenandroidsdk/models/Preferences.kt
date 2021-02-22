package com.raven.ravenandroidsdk.models

import com.google.gson.annotations.SerializedName

internal data class Preferences (

    @SerializedName("locale")
    var locale: String? = null,

    @SerializedName("channel_preferences")
    var channelPreferences: Map<String, ChannelPreference>? = null,

    @SerializedName("disabled")
    var disabled: Boolean? = null,

    @SerializedName("device_sid")
    var deviceSid: String? = null
)

internal data class ChannelPreference (

    @SerializedName("disabled")
    var disabled: Boolean? = null
)