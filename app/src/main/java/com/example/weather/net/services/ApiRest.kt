package com.example.weather.net.services

import com.google.gson.GsonBuilder
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.concurrent.TimeUnit

class ApiRest {
    companion object {

        private const val TIMEOUT: Long = 10
        private const val BASE_URL = "http://api.openweathermap.org/data/2.5/"
        private const val API_KEY = "558d81c6a0db8431d26379aeccea2c62"
        private const val KEY = "appid"
        private const val TEMPERATURE_UNITS = "metric"
        private const val UNITS = "units"
        private const val EXCLUDE = "exclude"
        private const val EXCLUDE_INTERVAL = "minutely,hourly"
        private const val LANG = "lang"
        private const val LANGUAGE = "ru"

        internal fun getApi(): Retrofit {

            val client = getOkHttpClient()

            val builder = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(getGsonConverterFactory())
                .client(client)

            return builder.build()
        }

        private fun getGsonConverterFactory(): Converter.Factory {

            return GsonConverterFactory.create(
                GsonBuilder()
                    .setLenient()
                    .create()
            )
        }

        private fun getOkHttpClient(): OkHttpClient {
            return OkHttpClient()
                .newBuilder()
                .addInterceptor(HttpLoggingInterceptor().apply {
                    level = HttpLoggingInterceptor.Level.BODY
                })
                .addInterceptor { chain ->
                    val request = chain.request().newBuilder()
                    val requestBuilder = chain.request().url
                    val url = requestBuilder.newBuilder()
                        .addQueryParameter(KEY, API_KEY)
                        .addQueryParameter(UNITS, TEMPERATURE_UNITS)
                        .addQueryParameter(EXCLUDE, EXCLUDE_INTERVAL)
                        .addQueryParameter(LANG, LANGUAGE)
                        .build()

                    request.url(url)
                    chain.proceed(request.build())
                }
                .addNetworkInterceptor(Interceptor { chain ->
                    val requestBuilder = chain.request().newBuilder()
                    chain.proceed(requestBuilder.build())

                })
                .connectTimeout(TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(TIMEOUT, TimeUnit.SECONDS)
                .build()
        }
    }
}
