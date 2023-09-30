package com.application.moviesapp.ui.onboarding

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.coroutineScope
import androidx.lifecycle.lifecycleScope
import com.application.moviesapp.base.BaseActivity
import com.application.moviesapp.data.common.Resource
import com.application.moviesapp.ui.home.HomeActivity
import com.application.moviesapp.ui.theme.MoviesAppTheme
import com.application.moviesapp.ui.viewmodel.OnboardingViewModel
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import timber.log.Timber
import kotlin.coroutines.coroutineContext

@AndroidEntryPoint
class OnboardingActivity: BaseActivity() {

    companion object {
        private const val TAG = "OnboardingActivity"

        fun startActivity(activity: Activity?) {
            val intent = Intent(activity, OnboardingActivity::class.java)
            activity?.startActivity(intent)
        }
    }

    private val viewModel: OnboardingViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen().apply {
            setKeepOnScreenCondition {

                if (viewModel.getUserInfo() != null) {
                    Timber.tag(TAG).d("Get user Success")

                    this@OnboardingActivity.finish()
                    HomeActivity.startActivity(this@OnboardingActivity)

                } else {
                    Timber.tag(TAG).e("Get user Failed")
                }

                viewModel.loading.value
            }
        }
        setTransparentStatusBar()
        setContent {
            MoviesAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    OnboardingApp()
                }
            }
        }
    }
}