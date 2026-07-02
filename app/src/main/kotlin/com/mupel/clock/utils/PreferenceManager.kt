package com.mupel.clock.utils

import android.content.Context
import android.content.SharedPreferences

class PreferenceManager(context: Context) {

    companion object {
        private const val PREF_NAME = "mupel_clock_prefs"
        private const val KEY_24H_FORMAT = "is_24h_format"
    }

    private val prefs: SharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

    fun is24HourFormat(): Boolean = prefs.getBoolean(KEY_24H_FORMAT, false)

    fun setTimeFormat(is24h: Boolean) {
        prefs.edit().putBoolean(KEY_24H_FORMAT, is24h).apply()
    }
}
