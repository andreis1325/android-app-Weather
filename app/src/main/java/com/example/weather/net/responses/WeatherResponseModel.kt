package com.example.weather.net.responses

data class WeatherResponseModel(
    val timezone: String? = null,
    val current: CurrentWeatherModel? = null,
    val daily: MutableList<DailyWeatherModel>? = null
)

data class CurrentWeatherModel(
    val dt: Long? = 0,
    val sunrise: Long? = 0,
    val sunset: Long? = 0,
    val temp: Double? = 0.0,
    val feels_like: Double? = 0.0,
    val humidity: Int? = 0,
    val wind_speed: Double? = 0.0,
    val weather: MutableList<WeatherInfoModel>? = mutableListOf()
)

data class WeatherInfoModel(
    val id: Int? = 0,
    val description: String? = null,
    val icon: String? = null
)

data class DailyWeatherModel(
    val dt: Long? = 0,
    val sunrise: Long? = 0,
    val sunset: Long? = 0,
    val temp: TemperatureModel? = null,
    val weather: MutableList<WeatherInfoModel>? = null
)

data class TemperatureModel(
    val min: Double? = 0.0,
    val max: Double? = 0.0,
    val humidity: Int? = 0,
    val wind_speed: Double? = 0.0
)