package com.example.weather.ui.base

import com.arellomobile.mvp.MvpPresenter
import com.example.gallery_settings.ui.base.BaseMvpView
import com.example.weather.MyApp
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import org.kodein.di.instance
import kotlin.collections.ArrayList

abstract class BaseMvpPresenter<V : BaseMvpView> : MvpPresenter<V>() {
    private val compositeDisposable: CompositeDisposable by MyApp.kodein.instance()
    private var newsItems = ArrayList<String>()


    fun addDisposable(disposable: Disposable) {
        compositeDisposable.add(disposable)
    }

    fun removeDisposable(disposable: Disposable) {
        compositeDisposable.remove(disposable)
    }
}
