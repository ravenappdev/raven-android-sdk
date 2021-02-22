package com.raven.ravenandroidsdk.api

import android.content.Context
import com.google.gson.GsonBuilder
import com.readystatesoftware.chuck.ChuckInterceptor
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.util.concurrent.TimeUnit

internal class ApiProvider(context: Context) {

    private var gson = GsonBuilder()
        .setLenient()
        .create()

    init {
        retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(getHttpClient(context))
            .build()
    }

    constructor(context: Context, secretKey: String): this(context) {
        retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(getHttpClient(context, secretKey))
            .build()
    }

    private fun getHttpClient(app: Context, secretKey: String? = null): OkHttpClient {

        val interceptor = HttpLoggingInterceptor()
        interceptor.apply { interceptor.level = HttpLoggingInterceptor.Level.BODY }
        val httpClient = OkHttpClient.Builder().callTimeout(15, TimeUnit.SECONDS)
        httpClient.addInterceptor(ChuckInterceptor(app.applicationContext))

        if (secretKey != null) {
            httpClient.addInterceptor(object : Interceptor {
                @Throws(IOException::class)
                override fun intercept(chain: Interceptor.Chain): Response {
                    val request: Request =
                        chain.request().newBuilder().addHeader("Authorization", "AuthKey $secretKey").build()
                    return chain.proceed(request)
                }
            })
        }

        return httpClient.build()
    }

    companion object {
        const val BASE_URL = "https://api.ravenapp.dev/"
        var retrofit: Retrofit? = null
    }

}