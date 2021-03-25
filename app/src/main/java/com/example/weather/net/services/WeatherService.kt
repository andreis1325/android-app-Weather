package com.example.weather.net.services

import com.example.weather.net.responses.WeatherResponseModel
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherService {

    companion object {
        private const val LATITUDE = "lat"
        private const val LONGITUDE = "lon"
        private const val GET_VALUE = "onecall"
        private const val DEFAULT_LATITUDE = 33.441792
        private const val DEFAULT_LONGITUDE = -94.037689
    }

    @GET(GET_VALUE)
    fun getWeather(
        @Query(LATITUDE) latitude: Double = DEFAULT_LATITUDE,
        @Query(LONGITUDE) longitude: Double = DEFAULT_LONGITUDE
    ): Observable<WeatherResponseModel>
}