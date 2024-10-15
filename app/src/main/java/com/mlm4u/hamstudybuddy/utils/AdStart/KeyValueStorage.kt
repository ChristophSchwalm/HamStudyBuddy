package com.mlm4u.hamstudybuddy.utils.AdStart

import android.content.Context
import com.russhwolf.settings.Settings
import com.russhwolf.settings.SharedPreferencesSettings
import com.russhwolf.settings.get
import com.russhwolf.settings.set

class KeyValueStorage(context: Context) {

    private val settings: Settings = SharedPreferencesSettings(
        context.getSharedPreferences("MyPreferences", Context.MODE_PRIVATE)
    )

    fun putLong(key: String, value: Long) {
        settings[key] = value
    }

    fun getLong(key: String, defaultValue: Long): Long {
        return settings[key, defaultValue]
    }
}