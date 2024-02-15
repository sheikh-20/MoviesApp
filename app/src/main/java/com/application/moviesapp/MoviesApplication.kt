package com.application.moviesapp

import android.app.Application
import android.content.Context
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import androidx.work.ListenableWorker
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import com.application.moviesapp.data.python.DownloaderRepository
import com.application.moviesapp.data.python.WorkManagerRepository
import com.application.moviesapp.worker.VideoInfoWorker
import com.chaquo.python.Python
import com.chaquo.python.android.AndroidPlatform
import com.facebook.FacebookSdk
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber
import javax.inject.Inject

@HiltAndroidApp
class MoviesApplication: Application(), Configuration.Provider {

    @Inject lateinit var workerFactory: HiltWorkerFactory

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        FacebookSdk.sdkInitialize(this)

        if (!Python.isStarted()) {
            Python.start(AndroidPlatform(this))
        }
    }

    override fun getWorkManagerConfiguration() =
        Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()
}