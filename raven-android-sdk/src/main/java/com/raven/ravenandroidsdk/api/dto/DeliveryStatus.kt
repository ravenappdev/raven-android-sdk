package com.raven.ravenandroidsdk.api.dto

import com.google.gson.annotations.SerializedName
import com.raven.ravenandroidsdk.models.Status
import java.util.*

internal data class DeliveryStatus (

    @SerializedName("notification_id")
    var notificationId: String? = null,

    @SerializedName("timestamp")
    var timestamp: Long? = Date().time,

    @SerializedName("current_timestamp")
    var currentTimestamp: Long? = Date().time,

    @SerializedName("type")
    var type: Status? = Status.DELIVERED
)

