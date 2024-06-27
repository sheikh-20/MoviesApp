package com.application.moviesapp.ui.onboarding

import android.app.Activity
import android.app.Activity.RESULT_OK
import android.content.IntentSender
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResultRegistryOwner
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.application.moviesapp.R
import com.application.moviesapp.domain.usecase.GetSignInFacebookInteractor
import com.application.moviesapp.domain.usecase.SignInFacebookUseCase
import com.application.moviesapp.ui.home.HomeActivity
import com.application.moviesapp.ui.onboarding.forgotpassword.ContactDetailsScreen
import com.application.moviesapp.ui.onboarding.forgotpassword.CreateNewPasswordScreen
import com.application.moviesapp.ui.onboarding.forgotpassword.OtpCodeScreen
import com.application.moviesapp.ui.onboarding.login.LoginScreen
import com.application.moviesapp.ui.onboarding.login.LoginWithPasswordScreen
import com.application.moviesapp.ui.onboarding.signup.ChooseYourInterestScreen
import com.application.moviesapp.ui.onboarding.signup.CreateNewPinScreen
import com.application.moviesapp.ui.onboarding.signup.FillYourProfileScreen
import com.application.moviesapp.ui.onboarding.signup.SetYourFingerprintScreen
import com.application.moviesapp.ui.onboarding.signup.SignupWithPasswordScreen
import com.application.moviesapp.ui.signin.SignInViewModel
import com.application.moviesapp.ui.viewmodel.OnboardingViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import timber.log.Timber

private const val TAG = "OnboardingApp"
@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun OnboardingApp(modifier: Modifier = Modifier,
                  navController: NavHostController = rememberNavController(),
                  onboardingViewModel: OnboardingViewModel = hiltViewModel()) {

    val backStackEntry by navController.currentBackStackEntryAsState()
    val uiState by onboardingViewModel.movieGenreUiState.collectAsState()

    val loginUIState by onboardingViewModel.loginUIState.collectAsState()
    val signupUIState by onboardingViewModel.signupUIState.collectAsState()

    val snackbarHostState = remember { SnackbarHostState() }

    val onSocialSignIn = onboardingViewModel.socialSignIn

    val coroutineScope = rememberCoroutineScope()

    val context = LocalContext.current

    Scaffold(
        topBar = { OnboardingAppBar(currentScreen = backStackEntry?.destination?.route ?: OnboardingScreen.Start.name, canNavigateBack = navController.previousBackStackEntry != null) { navController.navigateUp() } },
        snackbarHost = {
            SnackbarHost(snackbarHostState) {
                androidx.compose.material3.Snackbar(
                    modifier = modifier.padding(8.dp),
                    containerColor = MaterialTheme.colorScheme.background,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                ) {
                    Row(horizontalArrangement = Arrangement.SpaceBetween) {
                        Text(text = it.visuals.message, fontWeight = FontWeight.SemiBold)

                        Spacer(modifier = modifier.weight(1f))


                        Text(
                            text = it.visuals.actionLabel ?: "",
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = modifier.clickable(
                                onClick = { it.performAction() },
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null
                            )
                        )
                    }
                }
            }
        }
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
                        onSignupClick = { navController.navigate(OnboardingScreen.SignupWithPassword.name) },
                        onGoogleSignInClick = { activity, intent ->  onboardingViewModel.signInGoogle(activity, intent) },
                        onGithubSignInClick = { onboardingViewModel.signInGithub(context as Activity) },
                        onSocialSignIn = onSocialSignIn,
                        snackbarHostState = snackbarHostState
                    )
                }

                composable(route = OnboardingScreen.LoginWithPassword.name) {
                    LoginWithPasswordScreen(
                        modifier = modifier,
                        onSignInClick = { email: String?, password: String? -> onboardingViewModel.signInEmail(email, password) },
                        onSignupClick = { navController.navigate(OnboardingScreen.SignupWithPassword.name) },
                        onGoogleSignInClick = { activity, intent ->  onboardingViewModel.signInGoogle(activity, intent) },
                        onGithubSignInClick = { onboardingViewModel.signInGithub(context as Activity) },
                        onSocialSignIn = onSocialSignIn,
                        snackbarHostState = snackbarHostState,
                        onForgotPasswordClick = {
                            if (onboardingViewModel.email.isEmpty()) {
                                coroutineScope.launch {
                                    snackbarHostState.showSnackbar(message = context.getString(R.string.enter_email_address), duration = SnackbarDuration.Short)
                                }
                            } else {
                                navController.navigate(OnboardingScreen.ForgotPassword.name)
                            }
                        },
                        email = onboardingViewModel.email,
                        onEmailChange = onboardingViewModel::onEmailChange,
                        loginUIState = loginUIState
                    )
                }

                composable(route = OnboardingScreen.SignupWithPassword.name) {
                    SignupWithPasswordScreen(
                        modifier = modifier,
                        onSignInClick = { navController.navigate(OnboardingScreen.LoginWithPassword.name) },
                        onSignupClick = { email: String?, password: String? -> onboardingViewModel.signUpEmail(email, password) },
                        onGoogleSignInClick = { activity, intent ->  onboardingViewModel.signInGoogle(activity, intent) },
                        onGithubSignInClick = { onboardingViewModel.signInGithub(context as Activity) },
                        onSocialSignIn = onSocialSignIn,
                        snackbarHostState = snackbarHostState,
                        signupUIState = signupUIState,
                        sendVerificationEmail = { onSuccess, onFailure ->
                            onboardingViewModel.sendVerificationEmail(onSuccess, onFailure)
                        }
                    )
                }

                composable(route = OnboardingScreen.ForgotPassword.name) {
                    ContactDetailsScreen(
                        email = onboardingViewModel.email,
                        onPasswordResetOtp = onboardingViewModel::sendPasswordResetOtp,
                        snackbarHostState = snackbarHostState
                    )
                }

                composable(route = OnboardingScreen.OtpCode.name) {
                    OtpCodeScreen()
                }

                composable(route = OnboardingScreen.CreateNewPassword.name) {
                    CreateNewPasswordScreen()
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
    ForgotPassword,
    OtpCode,
    CreateNewPassword
}