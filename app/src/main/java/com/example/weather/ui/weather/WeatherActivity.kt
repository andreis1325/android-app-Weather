package com.example.weather.ui.weather

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.arellomobile.mvp.presenter.InjectPresenter
import com.bumptech.glide.Glide
import com.eightbitlab.supportrenderscriptblur.SupportRenderScriptBlur
import com.example.weather.R
import com.example.weather.net.responses.DailyWeatherModel
import com.example.weather.ui.base.BaseMvpActivity
import com.example.weather.ui.weather.dailyweatheradapter.DailyWeatherAdapter
import com.example.weather.utils.extensions.finishRefresh
import com.example.weather.utils.extensions.gone
import com.example.weather.utils.extensions.startRefresh
import com.example.weather.utils.extensions.visible
import kotlinx.android.synthetic.main.activity_weather.*

class WeatherActivity : BaseMvpActivity(), WeatherView, LocationListener {

    @InjectPresenter
    lateinit var weatherPresenter: WeatherPresenter

    private lateinit var dailyWeatherAdapter: DailyWeatherAdapter
    private lateinit var locationManager: LocationManager
    private var backPressedTime: Long = 0

    override fun getLayoutId(): Int = R.layout.activity_weather

    companion object {
        private const val MIN_TIME_TO_UPDATE: Long = 0
        private const val MIN_DISTANCE_TO_UPDATE = 0f
        private const val EXIT_TIME: Int = 2000
        private const val BLUR_RADIUS = 10f
    }

    override fun onCreateActivity(savedInstanceState: Bundle?) {
        initLocationManager()
        initDailyWeatherAdapter()
        getLocation()
        initOnActionListeners()
        weatherPresenter.onCreate(dailyWeatherAdapter.dailyWeatherClickObservable)
    }

     private fun showBlur() {

        vBvDailyWeather.setupWith(vFlRoot)
            .setFrameClearDrawable(window.decorView.background)
            .setBlurAlgorithm(SupportRenderScriptBlur(this))
            .setBlurRadius(BLUR_RADIUS)
            .setBlurEnabled(true)
    }

    private fun initLocationManager() {
        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
    }

    private fun initDailyWeatherAdapter() {
        dailyWeatherAdapter = DailyWeatherAdapter()
        vRvDailyWeather.adapter = dailyWeatherAdapter
    }

    private fun getLocation() {

        if (isLocationPermissionsGranted())
            requestLocationPermission()
        else
            updateLocation()
    }

    private fun initOnActionListeners() {

        vSrlRefreshWeather.setOnRefreshListener {
            weatherPresenter.onWeatherRefreshed()
        }

        vLlExtendedDailyWeather.setOnClickListener{}
    }

    private fun isLocationPermissionsGranted(): Boolean {

        return ActivityCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED
    }

    private fun requestLocationPermission() {

        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ),
            LOCATION_PERMISSION_CODE
        )
    }

    @SuppressLint("MissingPermission")
    override fun updateLocation() {

        locationManager.requestLocationUpdates(
            LocationManager.NETWORK_PROVIDER,
            MIN_TIME_TO_UPDATE,
            MIN_DISTANCE_TO_UPDATE,
            this
        )
    }

    override fun onBackPressed() {
        weatherPresenter.onBackClicked(isVisible = vLlExtendedDailyWeather.visibility != View.GONE)
    }

    override fun updateWeatherInfoAndFinishRefresh(
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
    ) {
        setCity(timezone)
        setDate(date)
        setWeatherIcon(icon)
        setTemperature(temperature)
        setTemperatureFeelLike(temperatureFeelLike)
        setDescription(description)
        setSunriseTime(sunriseTime)
        setSunsetTime(sunsetTime)
        setHumidity(humidity)
        setWindSpeed(windSpeed)
        setDailyWeatherAdapter(dailyForecastWeather)

        locationManager.removeUpdates(this)
        vSrlRefreshWeather.finishRefresh()
    }

    override fun onLocationChanged(location: Location) {
        weatherPresenter.onLocationChanged(location)
    }

    override fun updateWeatherAndStartRefresh() {
        vSrlRefreshWeather.startRefresh()
        getLocation()
    }

    override fun openExtendedDailyWeatherAndShowBlur(sunrise: String, sunset: String) {

        vTvDailySunrise.text = sunrise
        vTvDailySunset.text = sunset

        vLlExtendedDailyWeather.visible()
        showBlur()
    }

    override fun closeExtendedDailyWeatherAndHideBlur() {
        vLlExtendedDailyWeather.gone()
        hideBlur()
    }

    override fun exitOrShowMessage() {

        if (backPressedTime + EXIT_TIME > System.currentTimeMillis()) {
            super.onBackPressed()
        } else {
            Toast.makeText(this, getString(R.string.exit_hint), Toast.LENGTH_SHORT).show()
        }
        backPressedTime = System.currentTimeMillis()
    }

    private fun hideBlur() {

        vBvDailyWeather.setBlurEnabled(false)
    }

    private fun setDailyWeatherAdapter(dailyForecastWeather: MutableList<DailyWeatherModel>) {
        dailyWeatherAdapter.setItems(dailyForecastWeather)
    }

    private fun setWindSpeed(windSpeed: String) {

        vTvWindSpeed.text = windSpeed
    }

    private fun setHumidity(humidity: String) {

        vTvHumidity.text = humidity
    }

    private fun setSunsetTime(sunsetTime: String) {

        vTvSunsetTime.text = sunsetTime
    }

    private fun setSunriseTime(sunriseTime: String) {

        vTvSunriseTime.text = sunriseTime
    }

    private fun setDescription(description: String) {

        vTvDescription.text = description
    }

    private fun setTemperatureFeelLike(temperatureFeelLike: String) {

        vTvTemperatureFeelLike.text = temperatureFeelLike
    }

    private fun setTemperature(temperature: String) {
        vTvTemperature.text = temperature
    }

    private fun setWeatherIcon(icon: String) {

        Glide.with(this)
            .load(icon)
            .centerCrop()
            .error(R.drawable.ic_warning)
            .into(vIvWeatherIcon)
    }

    private fun setDate(date: String) {

        vTvDate.text = date
    }

    private fun setCity(timezone: String) {

        vTvCity.text = timezone
    }
}