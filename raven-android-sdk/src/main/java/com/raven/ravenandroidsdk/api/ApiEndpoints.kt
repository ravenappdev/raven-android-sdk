package com.raven.ravenandroidsdk.api

import com.raven.ravenandroidsdk.api.dto.DeliveryStatus
import com.raven.ravenandroidsdk.models.Device
import com.raven.ravenandroidsdk.api.dto.RavenMessage
import com.raven.ravenandroidsdk.models.User
import retrofit2.Call
import retrofit2.http.*


internal interface ApiEndpoints {

    @POST("v1/apps/{appId}/users")
    fun createUpdateUser(@Path("appId") appId: String, @Body user: User?): Call<User>?

    @GET("v1/apps/{appId}/users/{userId}")
    fun getUser(@Path("appId") appId: String, @Path("userId") userId: String): Call<User>?

    @POST("v1/apps/{appId}/events/send")
    fun sendCommunication(@Path("appId") appId: String, @Body msg: RavenMessage?): Call<User>?

    @POST("v1/apps/{appId}/users/{userId}/devices")
    fun addDeviceToken(@Path("appId") appId: String, @Path("userId") userId: String,
                       @Body device: Device?): Call<Device>?

    @POST("v1/apps/{appId}/users/{userId}/devices/{deviceId}")
    fun updateDeviceToken(@Path("appId") appId: String, @Path("userId") userId: String,
                          @Path("deviceId") deviceId: String, @Body device: Device?): Call<Device>?

    @DELETE("v1/apps/{appId}/users/{userId}/devices/{deviceId}")
    fun removeDeviceToken(@Path("appId") appId: String, @Path("userId") userId: String,
                          @Path("deviceId") deviceId: String): Call<Void>?

    @POST("v1/apps/{appId}/push/status")
    fun setDeliveryStatus(@Path("appId") appId: String, @Body status: DeliveryStatus): Call<Void>?

    companion object {
        var ravenApi = ApiProvider.retrofit?.create(ApiEndpoints::class.java)
    }
}