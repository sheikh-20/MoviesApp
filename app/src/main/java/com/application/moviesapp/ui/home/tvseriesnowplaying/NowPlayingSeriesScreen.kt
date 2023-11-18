package com.application.moviesapp.ui.home.tvseriesnowplaying

import android.app.Activity
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.paging.compose.LazyPagingItems
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.application.moviesapp.R
import com.application.moviesapp.domain.model.MovieNowPlaying
import com.application.moviesapp.domain.model.TvSeriesNowPlaying
import com.application.moviesapp.ui.detail.DetailActivity
import com.application.moviesapp.ui.detail.IS_TYPE
import com.application.moviesapp.ui.utility.toImageUrl
import com.application.moviesapp.ui.utility.toOneDecimal
import com.application.moviesapp.ui.viewmodel.MovieNewReleaseUiState

@Composable
fun NowPlayingSeriesScreen(modifier: Modifier = Modifier,
                           uiState: MovieNewReleaseUiState = MovieNewReleaseUiState.Loading,
                           moviesFlow: LazyPagingItems<TvSeriesNowPlaying>,
                           lazyGridState: LazyGridState = LazyGridState(),
                           bottomPadding: PaddingValues = PaddingValues()
                      ) {

//    when (uiState) {
//        is MovieNewReleaseUiState.Loading -> {
//            Column(modifier = modifier.fillMaxSize()) {
//                CircularProgressIndicator(modifier = modifier
//                    .fillMaxSize()
//                    .wrapContentSize(align = Alignment.Center))
//            }
//        }
//        is MovieNewReleaseUiState.Failure -> {
//            Text(text = "404")
//        }
//        is MovieNewReleaseUiState.Success -> {
//            Column(modifier = modifier
//                .fillMaxSize()
//                .padding(16.dp)) {
//
//                LazyVerticalGrid(columns = GridCells.Fixed(2),
//                    horizontalArrangement = Arrangement.spacedBy(8.dp),
//                    verticalArrangement = Arrangement.spacedBy(8.dp)) {
//                    items(uiState.moviesNewReleases.results ?: listOf()) {
//                        MovieImageCard(imageUrl = it?.posterPath ?: "", rating = it?.voteAverage.toString() ?: "")
//                    }
//                }
//            }
//        }
//    }

    Column(modifier = modifier
        .fillMaxSize()
        .padding(top = bottomPadding.calculateTopPadding(), bottom = bottomPadding.calculateBottomPadding())) {

        LazyVerticalGrid(columns = GridCells.Fixed(2),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            state = lazyGridState,
            contentPadding = PaddingValues(start = 16.dp, end = 16.dp)) {

            items(moviesFlow.itemCount) { index ->
                TvSeriesImageCard(imageUrl = moviesFlow[index]?.posterPath ?: "", rating = moviesFlow[index]?.voteAverage.toString() ?: "", tvSeriesId =  moviesFlow[index]?.id ?: 0)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TvSeriesImageCard(modifier: Modifier = Modifier, imageUrl: String = "", rating: String = "", tvSeriesId: Int = 0) {

    val context = LocalContext.current

    Card(shape = RoundedCornerShape(10), onClick = { DetailActivity.startActivity(context as Activity, IS_TYPE.TvSeries, tvSeriesId) }) {
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
private fun NowPlayingSeriesLightThemePreview() {
//    NewReleasesScreen()
}

@Preview(showBackground = true, showSystemUi = true, uiMode = UI_MODE_NIGHT_YES)
@Composable
private fun NowPlayingSeriesDarkThemePreview() {
//    NewReleasesScreen()
}