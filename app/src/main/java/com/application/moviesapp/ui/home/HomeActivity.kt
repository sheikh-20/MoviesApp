package com.application.moviesapp.ui.home

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.coroutineScope
import com.application.moviesapp.base.BaseActivity
import com.application.moviesapp.ui.onboarding.OnboardingApp
import com.application.moviesapp.ui.theme.MoviesAppTheme
import com.application.moviesapp.ui.viewmodel.ExploreViewModel
import com.application.moviesapp.ui.viewmodel.HomeViewModel
import com.application.moviesapp.ui.viewmodel.OnboardingViewModel
import com.application.moviesapp.ui.viewmodel.ProfileViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeActivity : BaseActivity() {

    private val viewModel: OnboardingViewModel by viewModels()
    private val homeViewModel: HomeViewModel by viewModels()
    private val exploreViewModel: ExploreViewModel by viewModels()
    private val profileViewModel: ProfileViewModel by viewModels()

    companion object {
        fun startActivity(activity: Activity?) {
            val intent = Intent(activity, HomeActivity::class.java)
            activity?.startActivity(intent)
        }
    }

    @OptIn(ExperimentalPermissionsApi::class)
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        installSplashScreen().apply {
            setKeepOnScreenCondition {
                viewModel.loading.value
            }
        }
        setTransparentStatusBar()

        homeViewModel.getMovieWithTvSeries()
//        exploreViewModel.getTrendingMovies()


        lifecycle.coroutineScope.launch {
            profileViewModel.isDarkMode.collect {
                setContent {
                    val context = LocalContext.current
                    val coroutineScope = rememberCoroutineScope()


                    MoviesAppTheme(darkTheme = it.data) {

                        // A surface container using the 'background' color from the theme
                        Surface(
                            modifier = Modifier.fillMaxSize(),
                            color = MaterialTheme.colorScheme.background
                        ) {
                            if (viewModel.getUserInfo() != null) {
                                HomeApp()
                            } else {
                                OnboardingApp()
                            }
                        }
                    }
                }
            }
        }
    }
}