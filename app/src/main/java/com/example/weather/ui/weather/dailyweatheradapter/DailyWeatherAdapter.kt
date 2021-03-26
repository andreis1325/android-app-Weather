package com.example.weather.ui.weather.dailyweatheradapter

import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.weather.R
import com.example.weather.net.responses.DailyWeatherModel
import com.example.weather.ui.base.BaseListAdapter
import com.example.weather.ui.base.BaseViewHolder
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject

class DailyWeatherAdapter(weather: MutableList<DailyWeatherModel> = mutableListOf()) :
    BaseListAdapter<DailyWeatherModel>(weather) {

    private val dailyWeatherClickSubject = PublishSubject.create<DailyWeatherModel>()
    val dailyWeatherClickObservable: Observable<DailyWeatherModel> = dailyWeatherClickSubject

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<DailyWeatherModel> {

        return DailyWeatherViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.view_item_daily_weather, parent, false),
            dailyWeatherClickSubject
        )
    }
}