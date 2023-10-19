package com.application.moviesapp.ui.home

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.application.moviesapp.base.BaseActivity
import com.application.moviesapp.ui.theme.MoviesAppTheme
import com.application.moviesapp.ui.viewmodel.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UpcomingMoviesActivity: BaseActivity() {
    companion object {
        fun startActivity(activity: Activity?) {
            val intent = Intent(activity, UpcomingMoviesActivity::class.java)
            activity?.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTransparentStatusBar()

        setContent {
            MoviesAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    UpcomingMoviesApp()
                }
            }
        }
    }
}