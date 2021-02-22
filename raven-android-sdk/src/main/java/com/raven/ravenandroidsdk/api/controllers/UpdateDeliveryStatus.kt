package com.raven.ravenandroidsdk.api.controllers

import com.raven.ravenandroidsdk.RavenSdk
import com.raven.ravenandroidsdk.api.ApiEndpoints
import com.raven.ravenandroidsdk.api.dto.DeliveryStatus
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

internal class UpdateDeliveryStatus(): Callback<Void?> {

    private var callback: RavenSdk.RavenResponseCallback? = null

    constructor(callback: RavenSdk.RavenResponseCallback?): this() {
        this.callback = callback
    }

    fun start(appId: String, deliveryStatus: DeliveryStatus) {
        val call: Call<Void>? = ApiEndpoints.ravenApi?.setDeliveryStatus(appId, deliveryStatus)
        call?.enqueue(this)
    }

    override fun onResponse(call: Call<Void?>, response: Response<Void?>) {
        if (response.isSuccessful) {
            callback?.onSuccess()
        } else {
            println(response.errorBody())
            callback?.onFailure("Response Failure")
        }
    }

    override fun onFailure(call: Call<Void?>, t: Throwable) {
        t.printStackTrace()
        callback?.onFailure("Connection Error")
    }
}