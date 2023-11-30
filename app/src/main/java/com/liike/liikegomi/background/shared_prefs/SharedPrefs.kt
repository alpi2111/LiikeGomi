package com.liike.liikegomi.background.shared_prefs

import android.content.Context
import android.content.SharedPreferences
import android.util.Log

object SharedPrefs {
    private lateinit var preferences: SharedPreferences
    private const val PREFERENCES_NAME: String = "liike_gomi"
    val arePreferencesAvailable: Boolean
        get() = this::preferences.isInitialized

    fun initPrefs(context: Context) {
        try {
            preferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)
        } catch (e: Exception) {
            Log.e("${PREFERENCES_NAME}_initPrefs", e.message ?: "Unknown error")
            e.printStackTrace()
        }
    }

    fun string(key: SharedPreferenceKeys, value: String) {
        preferences.edit().putString(key.value, value).apply()
    }

    fun string(key: SharedPreferenceKeys): String? {
        return preferences.getString(key.value, null)
    }

    fun bool(key: SharedPreferenceKeys, value: Boolean) {
        preferences.edit().putBoolean(key.value, value).apply()
    }

    fun bool(key: SharedPreferenceKeys): Boolean {
        return preferences.getBoolean(key.value, false)
    }

    fun delete(key: SharedPreferenceKeys) {
        preferences.edit().remove(key.value).apply()
    }

    fun deleteAll() {
        preferences.edit().clear().apply()
    }
}