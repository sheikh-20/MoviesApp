package com.application.moviesapp.ui.home

import android.app.Activity
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.application.moviesapp.ui.viewmodel.HomeViewModel
import com.application.moviesapp.ui.viewmodel.MovieTopRatedUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopMoviesApp(modifier: Modifier = Modifier, homeViewModel: HomeViewModel = hiltViewModel()) {

    val uiState: MovieTopRatedUiState by homeViewModel.movieTopRatedUiState.collectAsState()

    Scaffold(
        topBar = { TopMoviesTopAppbar() }
    ) { paddingValues ->
        TopMoviesScreen(modifier = modifier.padding(paddingValues), uiState = uiState)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TopMoviesTopAppbar() {

    val context = LocalContext.current

    TopAppBar(
        title = { Text(text = "Top 10 Movies This Week") },
        navigationIcon = {
            IconButton(onClick = { (context as Activity).finish() }) {
                Icon(imageVector = Icons.Rounded.ArrowBack, contentDescription = null)
            }
        },
        actions = {
            IconButton(onClick = {}) {
                Icon(imageVector = Icons.Rounded.Search, contentDescription = null)
            }
        }
    )
}