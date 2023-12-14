package com.application.moviesapp.ui.editprofile

import android.app.Activity
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Cast
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.application.moviesapp.ui.accountsetup.AccountSetupScreen
import com.application.moviesapp.ui.viewmodel.OnboardingViewModel

@Composable
fun EditProfileApp(modifier: Modifier = Modifier, onboardingViewModel: OnboardingViewModel = hiltViewModel()) {

    val snackbarHostState = remember { SnackbarHostState() }

    val profileUIState by onboardingViewModel.profilePhotoUIState.collectAsState()
    val userDetailUIState by onboardingViewModel.profileUIState.collectAsState()

    LaunchedEffect(key1 = Unit, block = {
        onboardingViewModel.getProfilePhoto()
        onboardingViewModel.getProfile()
    })

    Scaffold(
        topBar = { EditProfileTopAppbar() },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        EditProfileScreen(
            modifier = modifier,
            paddingValues = paddingValues,
            onContinueClick = { onboardingViewModel.updateProfile(it.fullName, it.nickName, it.email, it.phoneNumber, it.gender) },
            onProfileClick = onboardingViewModel::uploadProfilePhoto,
            profileUIState = profileUIState,
            userDetailUIState = userDetailUIState,
            snackbarHostState = snackbarHostState,
            onProfileUpdated = onboardingViewModel::getProfile
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun EditProfileTopAppbar(modifier: Modifier = Modifier) {

    val context = LocalContext.current

    TopAppBar(
        title = { Text(text = "Edit Profile") },
        navigationIcon = {
            IconButton(onClick = { (context as Activity).finish() }) {
                Icon(imageVector = Icons.Rounded.ArrowBack, contentDescription = null, tint = Color.White)
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent))
}