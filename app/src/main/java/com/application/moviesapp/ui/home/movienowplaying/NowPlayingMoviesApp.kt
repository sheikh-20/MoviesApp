package com.application.moviesapp.ui.home.movienowplaying

import android.app.Activity
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
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
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.collectAsLazyPagingItems
import com.application.moviesapp.ui.viewmodel.HomeViewModel
import com.application.moviesapp.ui.viewmodel.MovieTopRatedUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NowPlayingMoviesApp(modifier: Modifier = Modifier, homeViewModel: HomeViewModel = hiltViewModel()) {

    val uiState: MovieTopRatedUiState by homeViewModel.movieTopRatedUiState.collectAsState()
    val moviesFlow = homeViewModel.nowPlayingMoviesPagingFlow().collectAsLazyPagingItems()

    val upcomingScrollState = rememberLazyGridState()
    val upcomingHideTopAppBar by remember(upcomingScrollState) {
        derivedStateOf {
            upcomingScrollState.firstVisibleItemIndex == 0
        }
    }

    Scaffold(
        topBar = { TopMoviesTopAppbar(upcomingHideTopAppBar) }
    ) { paddingValues ->
        NowPlayingMoviesScreen(modifier = modifier, uiState = uiState, moviesFlow = moviesFlow, lazyGridState = upcomingScrollState, bottomPadding = paddingValues)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TopMoviesTopAppbar(upcomingHideTopAppBar: Boolean) {

    val context = LocalContext.current

    AnimatedVisibility(
        visible = upcomingHideTopAppBar,
        enter = slideInVertically(animationSpec = tween(durationMillis = 200)),
        exit = slideOutVertically(animationSpec = tween(durationMillis = 200))
    ) {

        TopAppBar(
            title = { Text(text = "Now Playing Movies") },
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
}