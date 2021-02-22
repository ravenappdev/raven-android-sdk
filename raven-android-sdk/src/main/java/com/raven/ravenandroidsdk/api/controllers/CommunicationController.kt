package com.raven.ravenandroidsdk.api.controllers

import com.raven.ravenandroidsdk.RavenSdk
import com.raven.ravenandroidsdk.api.ApiEndpoints
import com.raven.ravenandroidsdk.api.dto.RavenMessage
import com.raven.ravenandroidsdk.models.User
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

internal class CommunicationController(): Callback<User?> {

    private var callback: RavenSdk.RavenResponseCallback? = null

    constructor(callback: RavenSdk.RavenResponseCallback?): this() {
        this.callback = callback
    }

    fun start(appId: String, message: RavenMessage) {
        val call: Call<User>? = ApiEndpoints.ravenApi?.sendCommunication(appId, message)
        call?.enqueue(this)
    }

    override fun onResponse(call: Call<User?>, response: Response<User?>) {
        if (response.isSuccessful) {
            callback?.onSuccess()
        } else {
            println(response.errorBody())
            callback?.onFailure("Response Failure")
        }
    }

    override fun onFailure(call: Call<User?>, t: Throwable) {
        t.printStackTrace()
        callback?.onFailure("Connection Error")
    }
}