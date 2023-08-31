package com.application.moviesapp

import android.app.Application
import com.facebook.FacebookSdk
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class MoviesApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        FacebookSdk.sdkInitialize(this)
    }
}