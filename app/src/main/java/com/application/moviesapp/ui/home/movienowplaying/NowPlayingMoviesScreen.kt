package com.application.moviesapp.ui.home.movienowplaying

import android.app.Activity
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.application.moviesapp.R
import com.application.moviesapp.domain.model.MovieNowPlaying
import com.application.moviesapp.ui.detail.DetailActivity
import com.application.moviesapp.ui.detail.IS_TYPE
import com.application.moviesapp.ui.theme.MoviesAppTheme
import com.application.moviesapp.ui.utility.toImageUrl
import com.application.moviesapp.ui.utility.toOneDecimal
import com.application.moviesapp.ui.viewmodel.MovieTopRatedUiState

@Composable
fun NowPlayingMoviesScreen(modifier: Modifier = Modifier,
                    uiState: MovieTopRatedUiState = MovieTopRatedUiState.Loading,
                    moviesFlow: LazyPagingItems<MovieNowPlaying>,
                    lazyGridState: LazyGridState = LazyGridState(),
                    bottomPadding: PaddingValues = PaddingValues()
) {
//    when (uiState) {
//        is MovieTopRatedUiState.Loading -> {
//            Column(modifier = modifier.fillMaxSize()) {
//                CircularProgressIndicator(modifier = modifier
//                    .fillMaxSize()
//                    .wrapContentSize(align = Alignment.Center))
//            }
//        }
//        is MovieTopRatedUiState.Failure -> {
//            Text(text = "404")
//        }
//        is MovieTopRatedUiState.Success -> {
//            Column(modifier = modifier
//                .fillMaxSize()
//                .padding(16.dp)) {
//
//                LazyVerticalGrid(columns = GridCells.Fixed(2),
//                    horizontalArrangement = Arrangement.spacedBy(8.dp),
//                    verticalArrangement = Arrangement.spacedBy(8.dp)) {
//                    items(uiState.movieTopRated.results ?: listOf()) {
//                        MovieImageCard(imageUrl = it?.posterPath ?: "", rating = it?.voteAverage.toString() ?: "")
//                    }
//                }
//            }
//        }
//    }

    Column(modifier = modifier
        .fillMaxSize()
        .padding(
            top = bottomPadding.calculateTopPadding(),
            bottom = bottomPadding.calculateBottomPadding()
        )) {


        LazyVerticalGrid(columns = GridCells.Fixed(2),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            state = lazyGridState,
            contentPadding = PaddingValues(start = 16.dp, end = 16.dp)) {

            val loadState = moviesFlow.loadState.mediator

            
            items(moviesFlow.itemCount) { index ->
                MovieImageCard(imageUrl = moviesFlow[index]?.posterPath ?: "", rating = moviesFlow[index]?.voteAverage.toString() ?: "", movieId = moviesFlow[index]?.id ?: 0)
            }

//            item {
//                if (loadState?.refresh == LoadState.Loading) {
//                    Column(
//                        modifier = Modifier
//                            .fillMaxSize(),
//                        horizontalAlignment = Alignment.CenterHorizontally,
//                        verticalArrangement = Arrangement.Center,
//                    ) {
//                        Text(
//                            modifier = Modifier
//                                .padding(8.dp),
//                            text = "Refresh Loading"
//                        )
//
//                        CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
//                    }
//                }
//
//
//                if (loadState?.append == LoadState.Loading) {
//                    Box(
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .padding(16.dp),
//                        contentAlignment = Alignment.Center,
//                    ) {
//                        CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
//                    }
//                }
//
//                if (loadState?.refresh is LoadState.Error || loadState?.append is LoadState.Error) {
//                    val isPaginatingError =
//                        (loadState.append is LoadState.Error) || moviesFlow.itemCount > 1
//                    val error = if (loadState.append is LoadState.Error)
//                        (loadState.append as LoadState.Error).error
//                    else
//                        (loadState.refresh as LoadState.Error).error
//
//                    val modifier = if (isPaginatingError) {
//                        Modifier.padding(8.dp)
//                    } else {
//                        Modifier.fillMaxSize()
//                    }
//                    Column(
//                        modifier = modifier,
//                        verticalArrangement = Arrangement.Center,
//                        horizontalAlignment = Alignment.CenterHorizontally,
//                    ) {
//                        if (!isPaginatingError) {
//                            Icon(
//                                modifier = Modifier
//                                    .size(64.dp),
//                                imageVector = Icons.Rounded.Warning, contentDescription = null
//                            )
//                        }
//
//                        Text(
//                            modifier = Modifier
//                                .padding(8.dp),
//                            text = error.message ?: error.toString(),
//                            textAlign = TextAlign.Center,
//                        )
//
//                        Button(
//                            onClick = {
//                                moviesFlow.refresh()
//                            },
//                            content = {
//                                Text(text = "Refresh")
//                            },
//                        )
//                    }
//                }
//
//            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MovieImageCard(modifier: Modifier = Modifier, imageUrl: String = "", rating: String = "", movieId: Int = 0) {

    val context = LocalContext.current

    Card(shape = RoundedCornerShape(10), onClick = { DetailActivity.startActivity(context as Activity, IS_TYPE.Movie, movieId) }) {
        Box {
            AsyncImage(
                model = ImageRequest.Builder(context = LocalContext.current)
                    .data(imageUrl.toImageUrl)
                    .crossfade(true)
                    .build(),
                error = painterResource(id = R.drawable.ic_broken_image),
                placeholder = painterResource(id = R.drawable.ic_image_placeholder),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = modifier.height(250.dp))

            Card(modifier = modifier
                .fillMaxSize()
                .wrapContentSize(align = Alignment.TopStart)
                .padding(8.dp), shape = RoundedCornerShape(30), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary)) {
                Text(text = rating.toDoubleOrNull()?.toOneDecimal ?: "", modifier = modifier.padding(horizontal = 10.dp, vertical = 8.dp), style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun TopMoviesScreenLightThemePreview() {
    MoviesAppTheme(darkTheme = false) {
//        TopMoviesScreen()
    }
}

@Preview(showBackground = true, showSystemUi = true, uiMode = UI_MODE_NIGHT_YES)
@Composable
private fun TopMoviesScreenDarkThemePreview() {
    MoviesAppTheme(darkTheme = true) {
//        TopMoviesScreen()
    }
}

