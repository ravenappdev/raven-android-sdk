package com.raven.ravenandroidsdk.utils

import android.content.Context
import android.content.SharedPreferences

internal object Prefs {

    private var mPrefs: SharedPreferences? = null

    fun init(context: Context?, prefsName: String?, mode: Int) {
        mPrefs = context?.getSharedPreferences(prefsName, mode)
    }

    private fun getPreferences(): SharedPreferences? {
        if (mPrefs != null) {
            return mPrefs
        }
        throw RuntimeException(
            "Prefs class not correctly instantiated. Please call Builder.setContext().build() in the Application class onCreate."
        )
    }

    fun getString(key: String, default: String?): String? {
        return getPreferences()?.getString(key, default)
    }

    fun putString(key: String?, value: String?) {
        val editor: SharedPreferences.Editor? = getPreferences()?.edit()
        editor?.putString(key, value)
        editor?.apply()
    }
}