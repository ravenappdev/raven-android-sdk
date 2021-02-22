package com.raven.ravenandroidsdk.api.controllers

import com.raven.ravenandroidsdk.PREF_USER_DEVICE_ID
import com.raven.ravenandroidsdk.RavenSdk
import com.raven.ravenandroidsdk.api.ApiEndpoints
import com.raven.ravenandroidsdk.models.Device
import com.raven.ravenandroidsdk.models.getCurrentUser
import com.raven.ravenandroidsdk.models.setCurrentUser
import com.raven.ravenandroidsdk.utils.Prefs
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

internal class RemoveTokenController : Callback<Void?> {

    private var callback: RavenSdk.RavenResponseCallback? = null
    private lateinit var userId: String

    fun start(appId: String, userId: String) {

        val call: Call<Void>?
        val deviceId: String? = Prefs.getString(PREF_USER_DEVICE_ID, null)
        call =
            deviceId?.let {
                ApiEndpoints.ravenApi?.removeDeviceToken(appId, userId, it)
            }
        this.userId = userId
        call?.enqueue(this)
    }


    override fun onResponse(call: Call<Void?>, response: Response<Void?>) {
        if (response.isSuccessful) {

            val currentUser = getCurrentUser()
            if (null != currentUser) {
                val list: List<Device>? = currentUser.devices?.filter { device: Device -> device.deviceSid != Prefs.getString(PREF_USER_DEVICE_ID, null) }
                currentUser.devices = list as? ArrayList<Device>?
                setCurrentUser(currentUser)
            }

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