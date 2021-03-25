package com.example.gallery_settings.ui.base

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.annotation.LayoutRes
import com.delivery.ui.base.MvpFragment
import com.example.weather.ui.base.BaseMvpActivity

abstract class BaseMvpFragment : MvpFragment(),
    BaseMvpView {

    @LayoutRes
    abstract fun getLayoutId(): Int

    abstract fun onViewCreated(view: View)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)

        return inflater.inflate(getLayoutId(), container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        onViewCreated(view)
    }

    override fun showMessage(resId: Int) {
        (activity as? BaseMvpActivity)?.showMessage(resId)
    }

    override fun showMessage(msg: String?) {
        (activity as? BaseMvpActivity)?.showMessage(msg)
    }

    override fun handleRestError(e: Throwable) {
        (activity as? BaseMvpActivity)?.handleRestError(e)
    }


    override fun hideKeyboard() {
        (activity as? BaseMvpActivity)?.hideKeyboard()
    }

    open fun showKeyboard(view: View) {
        val imm =
            requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(view, 0)
    }
    open fun scrollToTheTop() {}

    open fun closeKeyboard() {
        val imm =
            requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view?.windowToken, 0)
    }
}