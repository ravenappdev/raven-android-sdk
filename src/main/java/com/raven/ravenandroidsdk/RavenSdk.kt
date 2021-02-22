package com.raven.ravenandroidsdk

import android.app.Application
import android.content.Context
import android.content.ContextWrapper
import com.raven.ravenandroidsdk.api.ApiProvider
import com.raven.ravenandroidsdk.api.controllers.CommunicationController
import com.raven.ravenandroidsdk.api.controllers.GetUserController
import com.raven.ravenandroidsdk.api.controllers.RemoveTokenController
import com.raven.ravenandroidsdk.api.controllers.UpdateDeliveryStatus
import com.raven.ravenandroidsdk.api.controllers.UpdateTokenController
import com.raven.ravenandroidsdk.api.controllers.UpdateUserController
import com.raven.ravenandroidsdk.api.dto.DeliveryStatus
import com.raven.ravenandroidsdk.models.*
import com.raven.ravenandroidsdk.api.dto.RavenMessage
import com.raven.ravenandroidsdk.api.dto.Recipient
import com.raven.ravenandroidsdk.models.User
import com.raven.ravenandroidsdk.utils.Prefs

object RavenSdk {

    const val RAVEN_NOTIFICATION_ID = "raven_notification_id"

    /*
    Method to initialize Raven SDK when you want to trigger raven events via SDK.
    Invoke on app launch
     */
    @Throws(IllegalArgumentException::class)
    fun initialize(context: Application, appId: String, secretKey: String) {
        initPrefs(context, appId)
        ApiProvider(context, secretKey)
        fetchCurrentUser()
    }


    /*
    Method to initialize Raven SDK when you do not want to trigger raven events via SDK.
    Invoke on app launch
     */
    @Throws(IllegalArgumentException::class)
    fun initialize(context: Application, appId: String) {
        initPrefs(context, appId)
        ApiProvider(context)
        fetchCurrentUser()
    }


    /*
    Set user identifier.
    Invoke once on login to set the user identifier
     */
    private fun setUserId(userId: String) {

        if (userId.isEmpty()) {
            return
        }

        //check if user id is set, or its the first time
        val currentUserId = Prefs.getString(PREF_USER_ID, null)
        if (currentUserId == userId) {
            return
        }

        //user switch without logout
        if (currentUserId != null && currentUserId != userId) {
            logout()
        }

        Prefs.putString(PREF_USER_ID, userId)
    }


    /*
    Set user mobile
     */
    fun setUserMobile(userId: String, mobile: String) {

        //throws exception if uninitialized
        checkIfSDKInitialized()

        //set user id
        setUserId(userId)

        if (mobile.isEmpty() || userId.isEmpty()) {
            return
        }

        //check if cached mobile is same as the argument
        val currentUser = getCurrentUser()
        if (currentUser?.mobile == mobile) {
            return
        }

        //Check for currently set user id
        val user = User(userId = userId, mobile = mobile)
        UpdateUserController().start(Prefs.getString(PREF_APP_ID, null) ?: return, user)
    }


    /*
    Set user
     */
    fun setUser(userId: String, mobile: String? = null, email: String? = null) {

        //throws exception if uninitialized
        checkIfSDKInitialized()

        //set user id
        setUserId(userId)

        if (userId.isEmpty() || ((mobile == null || mobile.isEmpty()) && (email == null  || email.isEmpty()))) {
            return
        }

        //check if cached mobile/email is same as the argument
        val currentUser = getCurrentUser()
        if (currentUser?.mobile == mobile && currentUser?.email == email) {
            return
        }

        val user = User(userId = userId, mobile = mobile, email = email)
        UpdateUserController().start(Prefs.getString(PREF_APP_ID, null) ?: return, user)
    }


    /*
    Set user email
     */
    fun setUserEmail(userId: String, email: String) {

        //throws exception if uninitialized
        checkIfSDKInitialized()

        //set user id
        setUserId(userId)

        if (email.isEmpty() || userId.isEmpty()) {
            return
        }

        //check if cached mobile is same as the argument
        val currentUser = getCurrentUser()
        if (currentUser?.email == email) {
            return
        }

        val user = User(userId = userId, email = email)
        UpdateUserController().start(Prefs.getString(PREF_APP_ID, null) ?: return, user)
    }


    /*
   Set user email
    */
    fun setDeviceToken(token: String) {

        //throws exception if uninitialized
        checkIfSDKInitialized()

        if (token.isEmpty()) {
            return
        }

        //check if cached token is same as the argument
        val currentUser = getCurrentUser()
        var isUpdate = false
        for (item in currentUser?.devices ?: arrayListOf()) {
            if (item.fcmToken == token && item.platform == PLATFORM) {
                return
            }

            if (item.deviceSid == Prefs.getString(PREF_USER_DEVICE_ID, null)) {
                if (item.fcmToken != token && item.platform == PLATFORM) {
                    isUpdate = true
                }
            }
        }

        val userId = Prefs.getString(PREF_USER_ID, null)
        if (userId != null) {
            UpdateTokenController().start(Prefs.getString(PREF_APP_ID, null) ?: return, userId, token, isUpdate)
        }
    }


    /*
    Logout user
     */
    fun logout() {

        //de register the device
        val userId = Prefs.getString(PREF_USER_ID, null)
        if (userId != null) {
            RemoveTokenController().start(Prefs.getString(PREF_APP_ID, null) ?: return, userId)
        }

        //reset the preferences
        Prefs.putString(PREF_USER_ID, null)
        Prefs.putString(PREF_RAVEN_USER, null)
    }


    /*
    Update message delivery status
     */
    fun updateStatus(notificationId: String, status: Status) {
        val deliveryStatus = DeliveryStatus(notificationId = notificationId, type = status)
        UpdateDeliveryStatus().start(Prefs.getString(PREF_APP_ID, null) ?: return, deliveryStatus)
    }


    /*
    Method to get the currently active user from raven backend
     */
    private fun fetchCurrentUser() {

        //throws exception if uninitialized
        checkIfSDKInitialized()

        //Check for currently set user id
        val userId = Prefs.getString(PREF_USER_ID, null)

        //if present, get the user from raven backend
        if (userId != null && userId.isNotEmpty()) {
            GetUserController().start(Prefs.getString(PREF_APP_ID, null) ?: return, userId)
        }
    }


    private fun initPrefs(context: Context?, appId: String) {

        if (appId.isEmpty()) {
            throw IllegalArgumentException("App ID cannot be empty")
        }

        //Initialize Prefs lib
        Prefs.init(context, context?.packageName + "_" + "raven", ContextWrapper.MODE_PRIVATE)

        Prefs.putString(PREF_APP_ID, appId)
    }


    private fun checkIfSDKInitialized() {
        if (Prefs.getString(PREF_APP_ID, null) == null) {
            throw IllegalStateException("Raven Client SDK not initialized")
        }
    }


    @Throws(IllegalStateException::class)
    fun sendMessage(template: String, id: String, callback: RavenResponseCallback?) {

        checkIfSDKInitialized()

        val message = RavenMessage()
        message.event = template

        val recipient = Recipient()
        recipient.userId = id
        message.user = recipient

        //api call
        Prefs.getString(PREF_APP_ID, null)?.let { CommunicationController(callback).start(it, message) }
    }


    interface RavenResponseCallback {
        fun onSuccess()
        fun onFailure(error: String?)
    }
}