package com.application.moviesapp.ui.home.explore

import android.app.Activity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.paging.compose.LazyPagingItems
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.application.moviesapp.R
import com.application.moviesapp.domain.model.MovieSearch
import com.application.moviesapp.domain.model.MoviesDiscover
import com.application.moviesapp.ui.detail.DetailActivity
import com.application.moviesapp.ui.detail.IS_TYPE
import com.application.moviesapp.ui.theme.MoviesAppTheme
import com.application.moviesapp.ui.utility.toImageUrl
import com.application.moviesapp.ui.utility.toOneDecimal
import com.application.moviesapp.ui.viewmodel.ExploreUiState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ExploreScreen(modifier: Modifier = Modifier,
                  uiState: ExploreUiState = ExploreUiState.Loading,
                  moviesDiscoverFlow: LazyPagingItems<MoviesDiscover>,
                  movieSearchFlow: LazyPagingItems<MovieSearch>,
                  searchClicked: Boolean = false,
                  lazyGridState: LazyGridState = LazyGridState(),
                  bottomPadding: PaddingValues = PaddingValues(),
                  onGoToDownloadsClick: () -> Unit = {  }
) {

    val coroutineScope = rememberCoroutineScope()
    var isRefreshing by remember { mutableStateOf(false) }
    val pullRefreshState = rememberPullRefreshState(
        refreshing = isRefreshing,
        onRefresh = {
            coroutineScope.launch {
                isRefreshing = !isRefreshing
                moviesDiscoverFlow.refresh()

                delay(1_000L)
                isRefreshing = !isRefreshing
            }
        })

    LaunchedEffect(key1 = Unit) {
        moviesDiscoverFlow.refresh()
    }

    Box(modifier = modifier
        .fillMaxSize()
        .padding(
            top = bottomPadding.calculateTopPadding(),
            bottom = bottomPadding.calculateBottomPadding()
        )
        .pullRefresh(pullRefreshState)) {
        Column {
            if (moviesDiscoverFlow.itemCount == 0) {
                Column(
                    modifier = modifier
                        .fillMaxSize()
                        .wrapContentSize(align = Alignment.Center),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {

                    Text(
                        text = stringResource(id = R.string.not_found),
                        style = MaterialTheme.typography.displayMedium,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.SemiBold,
                        modifier = modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )

                    Text(
                        text = stringResource(id = R.string.check_you_internet_connection),
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )

                    TextButton(
                        onClick = onGoToDownloadsClick,
                        modifier = modifier
                            .fillMaxWidth()
                            .wrapContentWidth(align = Alignment.CenterHorizontally),
                    ) {
                        Text(
                            text = stringResource(id = R.string.go_to_downloads),
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            } else {
                if (searchClicked) {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        state = lazyGridState,
                        contentPadding = PaddingValues(top = 16.dp, start = 16.dp, end = 16.dp)
                    ) {

                        items(count = movieSearchFlow.itemCount) { index ->
                            MovieImageCard(
                                imageUrl = movieSearchFlow[index]?.posterPath ?: "",
                                rating = movieSearchFlow[index]?.voteAverage.toString() ?: "",
                                movieId = movieSearchFlow[index]?.id
                            )
                        }
                    }
                } else {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        state = lazyGridState,
                        contentPadding = PaddingValues(top = 16.dp, start = 16.dp, end = 16.dp)
                    ) {

                        items(count = moviesDiscoverFlow.itemCount) { index ->
                            MovieImageCard(
                                imageUrl = moviesDiscoverFlow[index]?.posterPath ?: "",
                                rating = moviesDiscoverFlow[index]?.voteAverage.toString() ?: "",
                                movieId = moviesDiscoverFlow[index]?.id
                            )
                        }
                    }
                }
            }
        }

        PullRefreshIndicator(
            refreshing = isRefreshing,
            state = pullRefreshState,
            modifier = Modifier.align(Alignment.TopCenter),
            contentColor = MaterialTheme.colorScheme.primary,
            backgroundColor = MaterialTheme.colorScheme.background
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MovieImageCard(modifier: Modifier = Modifier, imageUrl: String = "", rating: String = "", movieId: Int? = 0) {

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
private fun ExploreScreenLightThemePreview() {
    MoviesAppTheme(darkTheme = false) {
//        ExploreScreen()
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun ExploreScreenDarkThemePreview() {
    MoviesAppTheme(darkTheme = true) {
//        ExploreScreen()
    }
}