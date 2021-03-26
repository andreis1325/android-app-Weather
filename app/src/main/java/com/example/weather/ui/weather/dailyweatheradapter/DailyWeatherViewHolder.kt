package com.example.weather.ui.weather.dailyweatheradapter

import android.view.View
import com.bumptech.glide.Glide
import com.example.weather.R
import com.example.weather.net.responses.DailyWeatherModel
import com.example.weather.net.responses.TemperatureModel
import com.example.weather.ui.base.BaseViewHolder
import com.example.weather.ui.weather.WeatherPresenter
import com.example.weather.utils.extensions.toDateFormat
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.view_item_daily_weather.view.*
import kotlin.math.roundToInt

class DailyWeatherViewHolder(
    itemView: View,
    var dailyWeatherClickSubject: PublishSubject<DailyWeatherModel>
) : BaseViewHolder<DailyWeatherModel>(itemView) {

    companion object {
        const val DATA_FORMAT = "EE"
    }

    override fun bind(model: DailyWeatherModel) {
        initClickListener(model)

        setWeekDay(date = model.dt ?: 0)
        setWeatherIcon(icon = model.weather?.getOrNull(0)?.icon ?: "")
        setTemperatureDescription(temperature = model.temp ?: TemperatureModel())
    }

    private fun initClickListener(model: DailyWeatherModel) {

        itemView.vTvDailyWeatherItem.setOnClickListener{
            dailyWeatherClickSubject.onNext(model)
        }
    }


    private fun setTemperatureDescription(temperature: TemperatureModel) {

        itemView.vTvTemperatureDescription.text =

            (temperature.max?.roundToInt() ?: 0.0)
                .toString()
                .plus(WeatherPresenter.DEGREE)
                .plus("/")
                .plus((temperature.min?.roundToInt() ?: 0.0))
                .plus(WeatherPresenter.DEGREE)
    }

    private fun setWeatherIcon(icon: String) {

        Glide.with(context)
            .load("${WeatherPresenter.START_ICON_URL}${icon}${WeatherPresenter.END_ICON_URL}")
            .centerCrop()
            .error(R.drawable.ic_warning)
            .into(itemView.vIvWeatherIcon)
    }

    private fun setWeekDay(date: Long) {

        itemView.vTvWeekDay.text = date.toDateFormat(DATA_FORMAT)
    }
}