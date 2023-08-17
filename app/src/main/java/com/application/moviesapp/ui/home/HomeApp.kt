package com.application.moviesapp.ui.home

import android.app.Activity
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.NotificationsNone
import androidx.compose.material.icons.rounded.Search
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.application.moviesapp.R
import com.application.moviesapp.ui.viewmodel.HomeViewModel
import com.application.moviesapp.ui.viewmodel.MoviesWithNewReleaseUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeApp(modifier: Modifier = Modifier, navController: NavHostController = rememberNavController(), homeViewModel: HomeViewModel = hiltViewModel()) {

    val uiState: MoviesWithNewReleaseUiState by homeViewModel.moviesWithNewReleaseUiState.collectAsState()

    Scaffold(
        topBar = { HomeTopAppbar() },
        containerColor = Color.Transparent
    ) { paddingValues ->
        NavHost(modifier = modifier.padding(paddingValues), navController = navController, startDestination = HomeScreen.Home.name) {
            composable(route = HomeScreen.Home.name) {
                HomeScreen(modifier = modifier, uiState = uiState)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HomeTopAppbar() {

    val context = LocalContext.current

    TopAppBar(
        title = {},
        navigationIcon = {
            IconButton(onClick = {   }) {
                Icon(painter = painterResource(id = R.drawable.ic_movie),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSecondary)
            }
        },
        actions = {
            IconButton(onClick = {}) {
                Icon(imageVector = Icons.Rounded.Search, contentDescription = null)
            }

            IconButton(onClick = { NotificationActivity.startActivity(context as Activity) }) {
                Icon(imageVector = Icons.Rounded.NotificationsNone, contentDescription = null)
            }
        },
        colors = TopAppBarDefaults.smallTopAppBarColors(containerColor = Color.Transparent)
    )
}

enum class HomeScreen {
    Home
}