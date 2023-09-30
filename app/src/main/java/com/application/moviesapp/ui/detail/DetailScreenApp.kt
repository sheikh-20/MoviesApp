package com.application.moviesapp.ui.detail

import android.app.Activity
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Cast
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.collectAsLazyPagingItems
import com.application.moviesapp.ui.viewmodel.DetailsViewModel
import com.application.moviesapp.ui.viewmodel.HomeViewModel

@Composable
fun DetailScreenApp(modifier: Modifier = Modifier,
                    viewModel: DetailsViewModel = hiltViewModel(),
                    homeViewModel: HomeViewModel = hiltViewModel()) {

    val uiState by viewModel.movieDetailResponse.collectAsState()
    val trailerUiState by viewModel.movieTrailerResponse.collectAsState()
    val moviesFlow = homeViewModel.moviesUpcomingPagingFlow().collectAsLazyPagingItems()
    val bookmarkUiState by viewModel.movieStateResponse.collectAsState()

    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        topBar = { DetailTopAppbar() },
        containerColor = Color.Transparent,
        snackbarHost = { SnackbarHost(snackbarHostState) }
        ) { paddingValues ->
        DetailScreen(modifier = modifier,
            uiState = uiState,
            trailerUiState = trailerUiState,
            moviesFlow = moviesFlow,
            onBookmarkClicked = { movieType: String, movieId: Int, isFavorite ->
                viewModel.updateMovieFavourite(movieType, movieId, isFavorite)
//                viewModel.getMovieState(movieId)
                                },
            snackbarHostState = snackbarHostState,
            bookmarkUiState = bookmarkUiState
            )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DetailTopAppbar(modifier: Modifier = Modifier) {

    val context = LocalContext.current

    TopAppBar(
        title = {  },
        navigationIcon = {
            IconButton(onClick = { (context as Activity).finish() }) {
                Icon(imageVector = Icons.Rounded.ArrowBack, contentDescription = null, tint = Color.White)
            }
        },
        actions = {
            IconButton(onClick = {}) {
                Icon(imageVector = Icons.Rounded.Cast, contentDescription = null, tint = Color.White)
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent))
}