package com.example.weather.utils.extensions

import android.view.View
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import java.text.SimpleDateFormat
import java.util.*

fun View.visible() {
    if (this.visibility != View.VISIBLE)
        this.visibility = View.VISIBLE
}

fun View.gone() {
    if (this.visibility != View.GONE)
        this.visibility = View.GONE
}

fun SwipeRefreshLayout.startRefresh() {
    if (!this.isRefreshing)
        this.isRefreshing = true
}

fun SwipeRefreshLayout.finishRefresh() {
    if (this.isRefreshing)
        this.isRefreshing = false
}

fun Long?.toDateFormat(dateFormat: String): String {

    val simpleDataFormat = SimpleDateFormat(dateFormat, Locale.getDefault())
    val date = Date(this?.times(1000) ?: 0)

    return simpleDataFormat.format(date) ?: ""
}


