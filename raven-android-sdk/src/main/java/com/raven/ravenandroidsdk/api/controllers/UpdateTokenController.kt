package com.raven.ravenandroidsdk.api.controllers

import com.raven.ravenandroidsdk.PLATFORM
import com.raven.ravenandroidsdk.PREF_USER_DEVICE_ID
import com.raven.ravenandroidsdk.PREF_USER_FCM_TOKEN
import com.raven.ravenandroidsdk.RavenSdk
import com.raven.ravenandroidsdk.api.ApiEndpoints
import com.raven.ravenandroidsdk.models.Device
import com.raven.ravenandroidsdk.models.User
import com.raven.ravenandroidsdk.models.getCurrentUser
import com.raven.ravenandroidsdk.models.setCurrentUser
import com.raven.ravenandroidsdk.utils.Prefs
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

internal class UpdateTokenController : Callback<Device?> {

    private var callback: RavenSdk.RavenResponseCallback? = null
    private lateinit var userId: String

    fun start(appId: String, userId: String, deviceToken: String, isUpdate: Boolean) {

        val call: Call<Device>?
        if (isUpdate) {
            val deviceId: String? = Prefs.getString(PREF_USER_DEVICE_ID, null)
            call =
                deviceId?.let {
                    ApiEndpoints.ravenApi?.updateDeviceToken(appId, userId, it, Device(fcmToken = deviceToken,
                        platform = PLATFORM))
                }
        } else {
            call = ApiEndpoints.ravenApi?.addDeviceToken(appId, userId, Device(fcmToken = deviceToken,
                platform = PLATFORM))
        }

        this.userId = userId
        call?.enqueue(this)
    }


    override fun onResponse(call: Call<Device?>, response: Response<Device?>) {
        if (response.isSuccessful) {

            val device: Device? = response.body()

            var currentUser = getCurrentUser()
            if (null == currentUser) {
                currentUser = User(userId = userId)
            }
            if (null == currentUser.devices) {
                currentUser.devices = arrayListOf()
            }

            device?.let { currentUser.devices?.add(it) }
            setCurrentUser(currentUser)

            Prefs.putString(PREF_USER_DEVICE_ID, device?.deviceSid)
            Prefs.putString(PREF_USER_FCM_TOKEN, device?.fcmToken)

            callback?.onSuccess()
        } else {
            println(response.errorBody())
            callback?.onFailure("Response Failure")
        }
    }


    override fun onFailure(call: Call<Device?>, t: Throwable) {
        t.printStackTrace()
        callback?.onFailure("Connection Error")
    }
}