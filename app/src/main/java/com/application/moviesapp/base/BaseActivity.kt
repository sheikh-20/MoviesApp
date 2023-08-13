package com.application.moviesapp.base

import android.graphics.Color
import android.view.View
import android.view.WindowManager
import androidx.activity.ComponentActivity

open class BaseActivity: ComponentActivity() {
    fun setTransparentStatusBar() {
        this.let {
            it.window?.let {
                it.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
                it.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
                it.statusBarColor = Color.TRANSPARENT
                it.decorView.systemUiVisibility =
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            }
        }
    }
}