package com.application.moviesapp.ui.accountsetup

import android.app.Activity
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.rounded.AccountCircle
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
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
import kotlinx.coroutines.delay
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

    var showSetupCompleteDialog by remember { mutableStateOf(false) }

    if (showSetupCompleteDialog) {
        AccountSetupCompleteDialog()

        LaunchedEffect(key1 = Unit) {
            delay(3_000L)
            showSetupCompleteDialog = false

            (context as Activity).finish()
            HomeActivity.startActivity(context as Activity)
        }
    }

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
                        showSetupCompleteDialog = true
                    },
                    onProfileClick = onboardingViewModel::uploadProfilePhoto,
                    profileUIState = profileUIState
                )
            }

//            composable(route = AccountSetupScreen.CreateNewPin.title) {
//                CreateNewPinScreen(
//                    modifier = modifier,
//                    paddingValues = paddingValues,
//                    onContinueClick = { navController.navigate(AccountSetupScreen.SetYourFingerprint.title) }
//                )
//            }
//
//            composable(route = AccountSetupScreen.SetYourFingerprint.title) {
//                SetYourFingerprintScreen(
//                    modifier = modifier,
//                    onContinueClick = {
//                        (context as Activity).finish()
//                        HomeActivity.startActivity(context as Activity)
//                    }
//                )
//            }
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

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
private fun AccountSetupCompleteDialog() {
    AlertDialog(onDismissRequest = { /*TODO*/ }, modifier = Modifier.background(color = MaterialTheme.colorScheme.surface, shape = RoundedCornerShape(10))) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally) {

            IconButton(onClick = { /*TODO*/ }, modifier = Modifier.size(150.dp)) {
                Icon(imageVector = Icons.Rounded.AccountCircle, contentDescription = null, modifier = Modifier.size(150.dp), tint = MaterialTheme.colorScheme.primary)
            }

            Text(text = "Congratulations",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.SemiBold)

            Text(text = "Your account is ready to use. You will be redirected to the Home page in a few seconds.",
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center)

            CircularProgressIndicator()
        }
    }
}

enum class AccountSetupScreen(val title: String) {
    ChooseYourInterest("Choose Your Interest"),
    FillYourProfile("Fill Your Profile"),
//    CreateNewPin("Create New Pin"),
//    SetYourFingerprint("Set Your Fingerprint")
}