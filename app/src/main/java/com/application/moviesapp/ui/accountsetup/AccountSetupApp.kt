package com.application.moviesapp.ui.accountsetup

import android.app.Activity
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.application.moviesapp.ui.home.HomeActivity
import com.application.moviesapp.ui.onboarding.signup.ChooseYourInterestScreen
import com.application.moviesapp.ui.onboarding.signup.CreateNewPinScreen
import com.application.moviesapp.ui.onboarding.signup.FillYourProfileScreen
import com.application.moviesapp.ui.onboarding.signup.SetYourFingerprintScreen
import com.application.moviesapp.ui.viewmodel.OnboardingViewModel
import timber.log.Timber

private const val TAG = "AccountSetupApp"
@Composable
fun AccountSetupApp(modifier: Modifier = Modifier,
                    navController: NavHostController = rememberNavController(),
                    onboardingViewModel: OnboardingViewModel = hiltViewModel()) {

    val context = LocalContext.current
    val backStackEntry by navController.currentBackStackEntryAsState()
    val uiState by onboardingViewModel.movieGenreUiState.collectAsState()

    val movieGenre by onboardingViewModel.readUserPreference.collectAsState()

    val profileUIState by onboardingViewModel.profilePhotoUIState.collectAsState()

    Timber.tag(TAG).d(movieGenre.genreList.toString())

    Scaffold(
        topBar = {
            AccountSetupAppBar(currentScreen = backStackEntry?.destination?.route ?: AccountSetupScreen.ChooseYourInterest.title) { navController.navigateUp() }
        }
    ) { paddingValues ->
        NavHost(modifier = modifier.padding(paddingValues), navController = navController, startDestination = AccountSetupScreen.ChooseYourInterest.title) {

            composable(route = AccountSetupScreen.ChooseYourInterest.title) {
                ChooseYourInterestScreen(
                    modifier = modifier,
                    uiState = uiState,
                    onContinueClick = {
                        onboardingViewModel.saveMovieGenre()
                        navController.navigate(AccountSetupScreen.FillYourProfile.title)
                                      },
                    onGenreClick = { onboardingViewModel.updateMovieGenre(it) }
                )
            }

            composable(route = AccountSetupScreen.FillYourProfile.title) {
                FillYourProfileScreen(
                    modifier = modifier,
                    onContinueClick = {
                        onboardingViewModel.updateProfile(it.fullName, it.nickName, it.email, it.phoneNumber, it.gender)
                        navController.navigate(AccountSetupScreen.CreateNewPin.title)
                    },
                    onProfileClick = onboardingViewModel::uploadProfilePhoto,
                    profileUIState = profileUIState
                )
            }

            composable(route = AccountSetupScreen.CreateNewPin.title) {
                CreateNewPinScreen(
                    modifier = modifier,
                    onContinueClick = { navController.navigate(AccountSetupScreen.SetYourFingerprint.title) }
                )
            }

            composable(route = AccountSetupScreen.SetYourFingerprint.title) {
                SetYourFingerprintScreen(
                    modifier = modifier,
                    onContinueClick = {
                        (context as Activity).finish()
                        HomeActivity.startActivity(context as Activity)
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccountSetupAppBar(currentScreen: String, onNavigateUp: () -> Unit) {
    TopAppBar(
        title = { Text(text = currentScreen, fontWeight = FontWeight.Bold) },
        navigationIcon = {
            IconButton(onClick = onNavigateUp) {
                Icon(imageVector = Icons.Outlined.ArrowBack,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSecondary)
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
    )
}

enum class AccountSetupScreen(val title: String) {
    ChooseYourInterest("Choose Your Interest"),
    FillYourProfile("Fill Your Profile"),
    CreateNewPin("Create New Pin"),
    SetYourFingerprint("Set Your Fingerprint")
}