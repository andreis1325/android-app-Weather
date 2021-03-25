package com.example.weather.ui.base

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.IBinder
import android.util.DisplayMetrics
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.authorization.ui.base.MvpActivity
import com.example.gallery_settings.ui.base.BaseMvpView

abstract class BaseMvpActivity : MvpActivity(),
    BaseMvpView {

    companion object {
        const val LOCATION_PERMISSION_CODE = 1
    }

    abstract fun getLayoutId(): Int

    val statusBarHeight: Int by lazy {
        val resourceId = resources.getIdentifier("status_bar_height", "dimen", "android")

        return@lazy if (resourceId > 0) {
            resources.getDimensionPixelSize(resourceId)
        } else {
            0
        }
    }

    open fun onPreCreate() {}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
//                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//
//        window.statusBarColor = Color.TRANSPARENT
        onPreCreate()
        setContentView(getLayoutId())
        onCreateActivity(savedInstanceState)
    }

    abstract fun onCreateActivity(savedInstanceState: Bundle?)

//    override fun attachBaseContext(context: Context) {
//           super.attachBaseContext(CalligraphyContextWrapper.wrap(context))
//    }

    override fun showMessage(resId: Int) {
        Toast.makeText(this, getString(resId), Toast.LENGTH_SHORT).show()
    }

    override fun hideKeyboard() {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        var token: IBinder? = null

        currentFocus?.let {
            if (token == null) {
                token = it.windowToken
            }
        }

        if (token != null) {
            imm.hideSoftInputFromWindow(token, 0)
        }
    }

    override fun showMessage(msg: String?) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }

    override fun handleRestError(e: Throwable) {
        showMessage("error during api call")
    }

    fun showSoftKeyboard(view: View?) {
        val inputMethodManager =
            getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
    }

    fun isPermissionGranted(permission: String): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            permission
        ) != PackageManager.PERMISSION_GRANTED
    }

    fun getDisplayWidth(): Int {
        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)

        return displayMetrics.widthPixels
    }

    open fun updateLocation() {}

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == LOCATION_PERMISSION_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                updateLocation()
            }
        }
    }

    open fun closeKeyboard() {
        val imm =
            this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

        imm.hideSoftInputFromWindow(this.currentFocus?.windowToken, 0)
    }
}
