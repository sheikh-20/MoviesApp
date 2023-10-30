package com.application.moviesapp.ui.home

import android.app.Activity
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.padding
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
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.collectAsLazyPagingItems
import com.application.moviesapp.ui.viewmodel.HomeViewModel
import com.application.moviesapp.ui.viewmodel.MovieNewReleaseUiState
import com.application.moviesapp.ui.viewmodel.MoviesWithNewReleaseUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewReleasesApp(modifier: Modifier = Modifier,
                   homeViewModel: HomeViewModel = hiltViewModel(),
                   ) {

    val uiState: MovieNewReleaseUiState by homeViewModel.moviesNewReleaseUiState.collectAsState()
    val moviesNewReleasePagingFlow = homeViewModel.moviesNewReleasePagingFlow().collectAsLazyPagingItems()

    val newReleaseScrollState = rememberLazyGridState()
    val newReleaseHideTopAppBar by remember(newReleaseScrollState) {
        derivedStateOf {
            newReleaseScrollState.firstVisibleItemIndex == 0
        }
    }

    Scaffold(
        topBar = { NewReleasesTopAppbar(newReleaseHideTopAppBar) },
        containerColor = Color.Transparent
    ) { paddingValues ->
        NewReleasesScreen(modifier = modifier, uiState = uiState, moviesFlow = moviesNewReleasePagingFlow, lazyGridState = newReleaseScrollState, bottomPadding = paddingValues)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun NewReleasesTopAppbar(newReleaseHideTopAppBar: Boolean) {
    
    val context = LocalContext.current

    AnimatedVisibility(
        visible = newReleaseHideTopAppBar,
        enter = slideInVertically(animationSpec = tween(durationMillis = 200)),
        exit = slideOutVertically(animationSpec = tween(durationMillis = 200))
    ) {
        TopAppBar(
            title = { Text(text = "New Releases") },
            navigationIcon = {
                IconButton(onClick = { (context as Activity).finish() }) {
                    Icon(imageVector = Icons.Rounded.ArrowBack, contentDescription = null)
                }
            },
            actions = {
                IconButton(onClick = {}) {
                    Icon(imageVector = Icons.Rounded.Search, contentDescription = null)
                }
            },
            colors = TopAppBarDefaults.smallTopAppBarColors(containerColor = Color.Transparent)
        )
    }
}