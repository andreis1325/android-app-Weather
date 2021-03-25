package com.example.weather.net.repo

import android.content.SharedPreferences
import com.example.weather.model.WeatherModel
import com.example.weather.model.getWeatherModel
import com.example.weather.model.setWeatherModel

class SharedPreferencesRepo(private val sharedPreferences: SharedPreferences) {

    fun setWeatherModel(weatherModel: WeatherModel) {
        sharedPreferences.edit().setWeatherModel(weatherModel).apply()
    }

    fun getWeatherModel(): WeatherModel? {
        return sharedPreferences.getWeatherModel(WeatherModel::class.java)
    }
}
