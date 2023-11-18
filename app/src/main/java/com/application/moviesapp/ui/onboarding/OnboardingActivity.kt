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
import com.application.moviesapp.ui.viewmodel.ProfileViewModel
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
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
        val loginManager = LoginManager.getInstance()
        val callbackManager = CallbackManager.Factory.create()


        private const val TAG = "OnboardingActivity"

        fun startActivity(activity: Activity?) {
            val intent = Intent(activity, OnboardingActivity::class.java)
            activity?.startActivity(intent)
        }
    }

    private val viewModel: OnboardingViewModel by viewModels()
    private val profileViewModel: ProfileViewModel by viewModels()

    private val facebookCallback  = object : FacebookCallback<LoginResult> {
        override fun onCancel() {

        }

        override fun onError(error: FacebookException) {
            Timber.tag(TAG).e(error.message.toString())
        }

        override fun onSuccess(result: LoginResult) {
            Timber.tag(TAG).d(result.accessToken.token)
            viewModel.signInFacebook(result.accessToken.token)
        }
    }

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

        lifecycle.coroutineScope.launch {
            profileViewModel.isDarkMode.collect {
                setContent {
                    MoviesAppTheme(darkTheme = it.data) {
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
    }

    init {
        loginManager.registerCallback(callbackManager, facebookCallback)
    }
}