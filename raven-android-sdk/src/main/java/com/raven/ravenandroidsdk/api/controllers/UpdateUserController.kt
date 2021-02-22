package com.raven.ravenandroidsdk.api.controllers

import com.google.gson.Gson
import com.raven.ravenandroidsdk.PREF_RAVEN_USER
import com.raven.ravenandroidsdk.RavenSdk
import com.raven.ravenandroidsdk.api.ApiEndpoints
import com.raven.ravenandroidsdk.models.User
import com.raven.ravenandroidsdk.models.getCurrentUser
import com.raven.ravenandroidsdk.models.setCurrentUser
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

internal class UpdateUserController : Callback<User?> {

    private var callback: RavenSdk.RavenResponseCallback? = null

    fun start(appId: String, user: User) {
        val call: Call<User>? = ApiEndpoints.ravenApi?.createUpdateUser(appId, user)
        call?.enqueue(this)
    }

    override fun onResponse(call: Call<User?>, response: Response<User?>) {
        if (response.isSuccessful) {
            val user: User? = response.body()
            var currentUser = getCurrentUser()
            if (null == currentUser) {
                currentUser = user?.userId?.let { User(userId = it) }
            }
            currentUser?.mobile = user?.mobile
            currentUser?.email = user?.email
            currentUser?.whatsAppMobile = user?.whatsAppMobile
            currentUser?.preference = user?.preference
            currentUser?.userId = user?.userId ?: ""
            setCurrentUser(currentUser)
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