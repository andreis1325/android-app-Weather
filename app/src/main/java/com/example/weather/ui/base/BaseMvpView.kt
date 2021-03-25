package com.example.gallery_settings.ui.base

import com.arellomobile.mvp.MvpView

interface BaseMvpView : MvpView {
    fun showMessage(resId: Int)
    fun showMessage(msg: String?)
    fun hideKeyboard()
    fun handleRestError(e: Throwable)
}