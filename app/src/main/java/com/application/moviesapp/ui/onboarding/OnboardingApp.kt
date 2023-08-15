package com.application.moviesapp.ui.onboarding

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsTopHeight
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
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.application.moviesapp.R
import com.application.moviesapp.ui.onboarding.login.LoginScreen
import com.application.moviesapp.ui.onboarding.login.LoginWithPasswordScreen
import com.application.moviesapp.ui.onboarding.signup.SignupWithPasswordScreen
import timber.log.Timber

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun OnboardingApp(modifier: Modifier = Modifier, navController: NavHostController = rememberNavController()) {

    val backStackEntry by navController.currentBackStackEntryAsState()

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
                        onSigninClick = { navController.navigate(OnboardingScreen.LoginWithPassword.name) })
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
    SignupWithPassword
}