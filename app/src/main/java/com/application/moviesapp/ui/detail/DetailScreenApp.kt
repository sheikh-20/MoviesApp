package com.application.moviesapp.ui.detail

import android.app.Activity
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Cast
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.paging.compose.collectAsLazyPagingItems
import com.application.moviesapp.R
import com.application.moviesapp.data.common.Resource
import com.application.moviesapp.domain.model.TvSeriesDetail
import com.application.moviesapp.domain.model.TvSeriesEpisodes
import com.application.moviesapp.ui.viewmodel.DetailsViewModel
import com.application.moviesapp.ui.viewmodel.HomeViewModel
import kotlinx.coroutines.launch

@Composable
fun DetailScreenApp(modifier: Modifier = Modifier,
                    navController: NavHostController = rememberNavController(),
                    viewModel: DetailsViewModel = hiltViewModel(),
                    homeViewModel: HomeViewModel = hiltViewModel()) {

    val moviesDetailsUiState by viewModel.movieDetailResponse.collectAsState()
    val tvSeriesDetailsUiState by viewModel.tvSeriesDetailResponse.collectAsState()

    val movieTrailerUiState by viewModel.movieTrailerResponse.collectAsState()
    val tvSeriesTrailerUiState by viewModel.tvSeriesTrailerResponse.collectAsState()
    val moviesFlow = homeViewModel.nowPlayingMoviesPagingFlow().collectAsLazyPagingItems()
    val bookmarkUiState by viewModel.movieStateResponse.collectAsState()
    val downloaderUiState by viewModel.downloaderUiState.collectAsState()

    val tvSeriesEpisodesUiState by viewModel.tvSeriesEpisodesResponse.collectAsState()

    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    var bottomSheetEnabled by remember { mutableStateOf(false) }

    if (bottomSheetEnabled) {
        BottomSheet(
            onDismiss = { bottomSheetEnabled = false },
            onSeasonClick = { seriesId, seasonNumber ->
                viewModel.getTvSeriesEpisodes(seriesId = seriesId, seasonNumber = seasonNumber)
                bottomSheetEnabled = false
                            },
            tvSeriesDetailUiState = tvSeriesDetailsUiState
        )
    }

    Scaffold(
        topBar = { DetailTopAppbar() },
        containerColor = Color.Transparent,
        snackbarHost = { SnackbarHost(snackbarHostState) }
        ) { paddingValues ->

        NavHost(navController = navController, startDestination = DetailScreen.Detail.name) {
            composable(route = DetailScreen.Detail.name) {
                DetailScreen(modifier = modifier,
                    movieUIState = moviesDetailsUiState,
                    tvSeriesUIState = tvSeriesDetailsUiState,
                    moviesTrailerUiState = movieTrailerUiState,
                    tvSeriesTrailerUiState = tvSeriesTrailerUiState,
                    moviesFlow = moviesFlow,
                    onBookmarkClicked = { movieType: String, movieId: Int, isFavorite ->
                        viewModel.updateMovieFavourite(movieType, movieId, isFavorite)
//                viewModel.getMovieState(movieId)
                    },
                    snackbarHostState = snackbarHostState,
                    bookmarkUiState = bookmarkUiState,
                    onTrailerClick = viewModel::getVideoInfo,
                    downloaderUiState = downloaderUiState,
                    onTrailerDownloadClick = viewModel::videoDownload,
                    tvSeriesEpisodesUIState = tvSeriesEpisodesUiState,
                    onTvSeriesEpisode = viewModel::getTvSeriesEpisodes,
                    onSeasonClick = { bottomSheetEnabled = true },
                    onCastClick = { navController.navigate(DetailScreen.Cast.name) }
                )
            }

            composable(route = DetailScreen.Cast.name) {
                CastScreen(
                    modifier = modifier,
                    paddingValues = paddingValues
                )
            }
        }
    }
}

enum class DetailScreen {
    Detail, Cast
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


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun BottomSheet(modifier: Modifier = Modifier,
                        onDismiss: () -> Unit = {},
                        onSeasonClick: (Int, Int) -> Unit = { _, _ -> },
                        tvSeriesDetailUiState: Resource<TvSeriesDetail> = Resource.Loading) {

    val bottomSheet = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val coroutineScope = rememberCoroutineScope()

    ModalBottomSheet(
        modifier = modifier,
        onDismissRequest = {
            coroutineScope.launch {
                onDismiss()
                bottomSheet.hide()
            }
        },
        sheetState = bottomSheet,
        dragHandle = { BottomSheetDefaults.DragHandle() },
        containerColor = MaterialTheme.colorScheme.background,
        tonalElevation = 0.dp
    ) {
        Column(modifier = modifier.systemBarsPadding()) {
            Text(text = stringResource(R.string.seasons),
                modifier = modifier
                    .fillMaxWidth()
                    .wrapContentWidth(align = Alignment.CenterHorizontally),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.primary)
            when (tvSeriesDetailUiState) {
                is Resource.Loading -> {
                    CircularProgressIndicator(modifier = modifier
                        .fillMaxWidth()
                        .wrapContentWidth(align = Alignment.CenterHorizontally))
                }
                is Resource.Failure -> {
                    Text(text = "Failure")
                }
                is Resource.Success -> {
                    LazyColumn(modifier = modifier.fillMaxWidth(),
                        contentPadding = PaddingValues(vertical = 16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)) {

                        items(tvSeriesDetailUiState.data.seasons?.size ?: 0) { index ->
                            Row(modifier = modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp)
                                .clickable(onClick = {
                                    onSeasonClick(
                                        tvSeriesDetailUiState.data.id ?: 0, index
                                    )
                                }),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween) {
                                Text(
                                    text = "Season ${index.inc()}",
                                    style = MaterialTheme.typography.titleMedium,
                                    color = MaterialTheme.colorScheme.primary,
                                    fontWeight = FontWeight.SemiBold
                                )

                                Text(
                                    text = "Episodes (${tvSeriesDetailUiState.data.seasons?.get(index)?.episodeCount})",
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}