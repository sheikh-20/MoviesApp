package com.application.moviesapp.ui.onboarding

import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.application.moviesapp.ui.onboarding.login.LoginScreen
import com.application.moviesapp.ui.onboarding.login.LoginWithPasswordScreen
import com.application.moviesapp.ui.onboarding.signup.ChooseYourInterestScreen
import com.application.moviesapp.ui.onboarding.signup.CreateNewPinScreen
import com.application.moviesapp.ui.onboarding.signup.FillYourProfileScreen
import com.application.moviesapp.ui.onboarding.signup.SetYourFingerprintScreen
import com.application.moviesapp.ui.onboarding.signup.SignupWithPasswordScreen
import com.application.moviesapp.ui.viewmodel.OnboardingViewModel

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun OnboardingApp(modifier: Modifier = Modifier, navController: NavHostController = rememberNavController(), onboardingViewModel: OnboardingViewModel = hiltViewModel()) {

    val backStackEntry by navController.currentBackStackEntryAsState()
    val uiState by onboardingViewModel.movieGenreUiState.collectAsState()

    Scaffold(topBar = { OnboardingAppBar(currentScreen = backStackEntry?.destination?.route ?: OnboardingScreen.Start.name, canNavigateBack = navController.previousBackStackEntry != null) { navController.navigateUp() } },
     ) { innerPadding ->
            NavHost(modifier = modifier.padding(innerPadding),
                navController = navController,
                startDestination = OnboardingScreen.Start.name) {

                composable(route = OnboardingScreen.Start.name) {
                    OnboardingScreen(modifier = modifier) {
                        navController.navigate(OnboardingScreen.Login.name)
                    }
                }

                composable(route = OnboardingScreen.Login.name) {
                    LoginScreen(modifier = modifier,
                        onSignInClick = { navController.navigate(OnboardingScreen.LoginWithPassword.name) },
                        onSignupClick = { navController.navigate(OnboardingScreen.SignupWithPassword.name) }
                    )
                }

                composable(route = OnboardingScreen.LoginWithPassword.name) {
                    LoginWithPasswordScreen(
                        modifier = modifier,
                        onSignupClick = { navController.navigate(OnboardingScreen.SignupWithPassword.name) }
                    )
                }

                composable(route = OnboardingScreen.SignupWithPassword.name) {
                    SignupWithPasswordScreen(
                        modifier = modifier,
                        onSigninClick = { navController.navigate(OnboardingScreen.LoginWithPassword.name) },
                        onSignupClick = {
                            onboardingViewModel.getMoviesGenreList()
                            navController.navigate(OnboardingScreen.ChooseYourInterest.name) }
                    )
                }

                composable(route = OnboardingScreen.ChooseYourInterest.name) {
                    ChooseYourInterestScreen(
                        modifier = modifier,
                        uiState = uiState,
                        onContinueClick = { navController.navigate(OnboardingScreen.FillYourProfile.name) }
                    )
                }

                composable(route = OnboardingScreen.FillYourProfile.name) {
                    FillYourProfileScreen(
                        modifier = modifier,
                        onContinueClick = { navController.navigate(OnboardingScreen.CreateNewPin.name) }
                    )
                }

                composable(route = OnboardingScreen.CreateNewPin.name) {
                    CreateNewPinScreen(
                        modifier = modifier,
                        onContinueClick = { navController.navigate(OnboardingScreen.SetYourFingerprint.name) }
                    )
                }

                composable(route = OnboardingScreen.SetYourFingerprint.name) {
                    SetYourFingerprintScreen()
                }
            }
        }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OnboardingAppBar(currentScreen: String, canNavigateBack: Boolean, onNavigateUp: () -> Unit) {
    TopAppBar(
        title = {},
        navigationIcon = {
            if (canNavigateBack) {
                IconButton(onClick = onNavigateUp) {
                    Icon(imageVector = Icons.Outlined.ArrowBack,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSecondary)
                }
            }
        },
        colors = TopAppBarDefaults.smallTopAppBarColors(containerColor = Color.Transparent)
    )
}

enum class OnboardingScreen {
    Start,
    Login,
    LoginWithPassword,
    SignupWithPassword,
    ChooseYourInterest,
    FillYourProfile,
    CreateNewPin,
    SetYourFingerprint
}