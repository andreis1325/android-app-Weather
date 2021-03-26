package com.example.weather.ui.weather

import android.location.Location
import com.arellomobile.mvp.InjectViewState
import com.example.weather.MyApp
import com.example.weather.model.WeatherModel
import com.example.weather.net.repo.SharedPreferencesRepo
import com.example.weather.net.repo.WeatherRepo
import com.example.weather.net.responses.DailyWeatherModel
import com.example.weather.ui.base.BaseMvpPresenter
import com.example.weather.utils.extensions.toDateFormat
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.kodein.di.instance
import kotlin.math.roundToInt

@InjectViewState
class WeatherPresenter : BaseMvpPresenter<WeatherView>() {

    private val weatherRepo by MyApp.kodein.instance<WeatherRepo>()
    private val sharedPreferencesRepo by MyApp.kodein.instance<SharedPreferencesRepo>()

    companion object {
        const val FULL_DATE_FORMAT = "EE, dd MMMM HH:mm"
        const val HH_MM_DATE_FORMAT = "HH:mm"
        const val SUNRISE = "Восход - "
        const val SUNSET = "Закат - "
        const val START_ICON_URL = "http://openweathermap.org/img/wn/"
        const val END_ICON_URL = "@2x.png"
        const val DEGREE = "\u00B0"
        const val SPEED_UNITS = "м/с"
        const val FEEL_LIKE = "Ощущается как"
        const val BAD_CONNECTION_MESSAGE = "Bad connection"
    }

    fun onCreate(dailyWeatherClickObservable: Observable<DailyWeatherModel>) {

        updateWeather(sharedPreferencesRepo.getWeatherModel() ?: WeatherModel())
        initOnDailyWeatherClickListener(dailyWeatherClickObservable)
    }

    private fun initOnDailyWeatherClickListener(dailyWeatherClickObservable: Observable<DailyWeatherModel>) {

        addDisposable(
            dailyWeatherClickObservable.subscribe {

                viewState.openExtendedDailyWeatherAndShowBlur(
                    sunrise = SUNRISE.plus(it.sunrise.toDateFormat(HH_MM_DATE_FORMAT)),
                    sunset = SUNSET.plus(it.sunset.toDateFormat(HH_MM_DATE_FORMAT))
                )
            }
        )
    }

    fun onLocationChanged(location: Location) {

        loadAndUpdateWeather(location)
    }

    private fun loadAndUpdateWeather(location: Location) {
        addDisposable(

            weatherRepo.getWeather(location.latitude, location.longitude)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ weather ->

                    sharedPreferencesRepo.setWeatherModel(weather)
                    updateWeather(weather ?: WeatherModel())
                },
                    {
                        viewState.showMessage(BAD_CONNECTION_MESSAGE)
                        updateWeather(sharedPreferencesRepo.getWeatherModel() ?: WeatherModel())
                    }
                )
        )
    }

    private fun updateWeather(weather: WeatherModel) {

        viewState.updateWeatherInfoAndFinishRefresh(
            timezone = weather.timezone,
            date = weather.currentWeather.dt.toDateFormat(FULL_DATE_FORMAT),
            icon = "${START_ICON_URL}${weather.currentWeather.weather?.getOrNull(0)?.icon}${END_ICON_URL}",
            temperature = "${(weather.currentWeather.temp ?: 0.0).roundToInt()}$DEGREE",
            temperatureFeelLike = "$FEEL_LIKE ${(weather.currentWeather.feels_like ?: 0.0).roundToInt()}$DEGREE",
            description = weather.currentWeather.weather?.getOrNull(0)?.description ?: "",
            sunriseTime = weather.currentWeather.sunrise.toDateFormat(HH_MM_DATE_FORMAT),
            sunsetTime = weather.currentWeather.sunset.toDateFormat(HH_MM_DATE_FORMAT),
            humidity = "${weather.currentWeather.humidity}%",
            windSpeed = "${(weather.currentWeather.wind_speed ?: 0.0).roundToInt()} $SPEED_UNITS",
            dailyForecastWeather = weather.daily
        )
    }

    fun onWeatherRefreshed() {

        viewState.updateWeatherAndStartRefresh()
    }

    fun onBackClicked(isVisible: Boolean) {

        if(isVisible)
            viewState.closeExtendedDailyWeatherAndHideBlur()
        else
            viewState.exitOrShowMessage()
    }
}