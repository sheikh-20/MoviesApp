package com.application.moviesapp.ui.accountsetup

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.coroutineScope
import com.application.moviesapp.base.BaseActivity
import com.application.moviesapp.ui.theme.MoviesAppTheme
import com.application.moviesapp.ui.viewmodel.OnboardingViewModel
import com.application.moviesapp.ui.viewmodel.ProfileViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AccountSetupActivity : BaseActivity() {

    private val viewModel: OnboardingViewModel by viewModels()
    private val profileViewModel: ProfileViewModel by viewModels()

    companion object {
        fun startActivity(activity: Activity?) {
            val intent = Intent(activity, AccountSetupActivity::class.java)
            activity?.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setTransparentStatusBar()
        viewModel.getMoviesGenreList()

        lifecycle.coroutineScope.launch {
            profileViewModel.isDarkMode.collect {
                setContent {
                    MoviesAppTheme(darkTheme = it.data) {
                        // A surface container using the 'background' color from the theme
                        Surface(
                            modifier = Modifier.fillMaxSize(),
                            color = MaterialTheme.colorScheme.background
                        ) {
                            AccountSetupApp()
                        }
                    }
                }
            }
        }
    }
}