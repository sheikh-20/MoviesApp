package com.application.moviesapp.ui.notification

import android.app.Activity
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.hilt.navigation.compose.hiltViewModel
import com.application.moviesapp.domain.model.AppUpdatesPreference
import com.application.moviesapp.domain.model.GeneralNotificationPreference
import com.application.moviesapp.ui.viewmodel.ProfileViewModel

@Composable
fun NotificationApp(modifier: Modifier = Modifier, profileViewModel: ProfileViewModel = hiltViewModel()) {

    val generalNotificationUIState by profileViewModel.isGeneralNotification.collectAsState(initial = GeneralNotificationPreference(false))
    val appUpdatesUIState by profileViewModel.isAppUpdates.collectAsState(initial = AppUpdatesPreference(false))

    Scaffold(
        topBar = {  NotificationTopAppbar() }
    ) { paddingValues ->

        NotificationScreen(
            paddingValues = paddingValues,
            generalNotificationPreference = generalNotificationUIState,
            appUpdatesPreference = appUpdatesUIState,
            onGeneralNotificationChange = profileViewModel::updateGeneraNotification,
            onAppUpdateChange = profileViewModel::updateAppUpdates)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun NotificationTopAppbar(modifier: Modifier = Modifier) {

    val context = LocalContext.current

    TopAppBar(
        title = { Text(text = "Notification") },
        navigationIcon = {
            IconButton(onClick = { (context as Activity).finish() }) {
                Icon(imageVector = Icons.Rounded.ArrowBack, contentDescription = null, tint = Color.White)
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent))
}