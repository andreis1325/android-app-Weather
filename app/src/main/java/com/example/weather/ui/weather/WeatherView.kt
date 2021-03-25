package com.example.weather.ui.weather

import com.example.gallery_settings.ui.base.BaseMvpView
import com.example.weather.net.responses.DailyWeatherModel

interface WeatherView: BaseMvpView {

    fun updateWeatherInfoAndFinishRefresh(
        timezone: String,
        date: String,
        icon: String,
        temperature: String,
        temperatureFeelLike: String,
        description: String,
        sunriseTime: String,
        sunsetTime: String,
        humidity: String,
        windSpeed: String,
        dailyForecastWeather: MutableList<DailyWeatherModel>
    )

    fun updateWeatherAndStartRefresh()
}