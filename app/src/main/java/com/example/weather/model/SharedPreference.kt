package com.example.weather.model

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import java.io.Serializable

class PreferencesUtils {

    companion object {

        fun getSharedPreferences(context: Context) =
            context.getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE)!!

        private const val PREFERENCES = "PREFERENCES"
        const val COUNTRY: String = "COUNTRY"
        const val YEAR: String = "YEAR"
        const val ISO: String = "ISO"
        const val WEATHER_MODEL: String = "WEATHER_MODEL"
    }
}

inline fun SharedPreferences.editMe(operation: (SharedPreferences.Editor) -> Unit) {
    val editMe = edit()
    operation(editMe)
    editMe.apply()
}

fun Serializable.toJson(): String? {
    return Gson().toJson(this)
}

fun <T> String.to(type: Class<T>): T where T : Serializable? {
    return Gson().fromJson(this, type)
}

fun SharedPreferences.Editor.setWeatherModel( serializable: Serializable?) = apply {
    putString(PreferencesUtils.WEATHER_MODEL, serializable?.toJson())
}

fun <T> SharedPreferences.getWeatherModel( type: Class<T>): T? where T : Serializable {
    return getString(PreferencesUtils.WEATHER_MODEL, "")?.to(type)
}

var SharedPreferences.country: String
    get() = getString(PreferencesUtils.COUNTRY, "") ?: ""
    set(value) = editMe { it.putString(PreferencesUtils.COUNTRY, value) }

var SharedPreferences.iso: String
    get() = getString(PreferencesUtils.ISO, "") ?: ""
    set(value) = editMe { it.putString(PreferencesUtils.ISO, value) }

var SharedPreferences.year: Int
    get() = getInt(PreferencesUtils.YEAR, 0)
    set(value) = editMe { it.putInt(PreferencesUtils.YEAR, value) }
