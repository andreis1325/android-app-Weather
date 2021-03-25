package com.example.weather.net.repo

import com.example.weather.model.WeatherModel
import com.example.weather.net.responses.CurrentWeatherModel
import com.example.weather.net.responses.DailyWeatherModel
import com.example.weather.net.services.WeatherService
import io.reactivex.Observable

class WeatherRepo(private val api: WeatherService) {

    fun getWeather(latitude: Double, longitude: Double): Observable<WeatherModel> =
        api.getWeather(latitude, longitude).map { weatherResponseModel ->

            WeatherModel(
                weatherResponseModel.timezone ?: "",
                weatherResponseModel.current ?: CurrentWeatherModel(),
                weatherResponseModel.daily ?: mutableListOf(DailyWeatherModel())
            )
        }
}