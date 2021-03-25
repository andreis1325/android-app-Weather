package com.example.weather.model

import com.example.weather.net.responses.CurrentWeatherModel
import com.example.weather.net.responses.DailyWeatherModel
import java.io.Serializable

data class WeatherModel(
    val timezone: String = "",
    val currentWeather: CurrentWeatherModel = CurrentWeatherModel(),
    val daily: MutableList<DailyWeatherModel> = mutableListOf()
) : Serializable




