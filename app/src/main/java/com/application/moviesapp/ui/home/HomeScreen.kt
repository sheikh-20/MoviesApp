package com.application.moviesapp.ui.home

import android.app.Activity
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.gestures.animateScrollBy
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.PlayCircle
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.application.moviesapp.ui.theme.MoviesAppTheme
import com.application.moviesapp.R
import com.application.moviesapp.data.common.Resource
import com.application.moviesapp.domain.model.MovieWithTvSeries
import com.application.moviesapp.ui.detail.DetailActivity
import com.application.moviesapp.ui.detail.IS_TYPE
import com.application.moviesapp.ui.home.movienowplaying.NowPlayingMoviesActivity
import com.application.moviesapp.ui.home.tvseriesnowplaying.NowPlayingSeriesActivity
import com.application.moviesapp.ui.utility.toImageUrl
import com.application.moviesapp.ui.utility.toOneDecimal
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterialApi::class)
@Composable
fun HomeScreen(modifier: Modifier = Modifier,
               uiState: Resource<MovieWithTvSeries> = Resource.Loading,
               bottomPadding: PaddingValues = PaddingValues(),
               goToDownloadClick: () -> Unit = {  }, 
               goToMyListClick: () -> Unit = {   },
               onMovieWithTvSeries: () -> Unit = {  }
) {

    val context = LocalContext.current

    val coroutineScope = rememberCoroutineScope()
    var isRefreshing by remember { mutableStateOf(false) }
    val pullRefreshState = rememberPullRefreshState(
        refreshing = isRefreshing,
        onRefresh = {
            coroutineScope.launch {
                isRefreshing = !isRefreshing
                onMovieWithTvSeries()

                delay(1_000L)
                isRefreshing = !isRefreshing
            }
        })

    LaunchedEffect(key1 = Unit) {
        onMovieWithTvSeries()
    }

    when (uiState) {
        is Resource.Loading -> {
            Column(modifier = modifier.fillMaxSize()) {
                CircularProgressIndicator(modifier = modifier
                    .fillMaxSize()
                    .wrapContentSize(align = Alignment.Center))
            }
        }
        is Resource.Failure -> {
            Column(modifier = modifier
                .fillMaxSize()
                .wrapContentSize(align = Alignment.Center),
                verticalArrangement = Arrangement.spacedBy(8.dp)) {

                Text(text = stringResource(R.string.not_found),
                    style = MaterialTheme.typography.displayMedium,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.SemiBold,
                    modifier = modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center)

                Text(text = stringResource(R.string.check_you_internet_connection),
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center)

                TextButton(onClick = goToDownloadClick, modifier = modifier
                    .fillMaxWidth()
                    .wrapContentWidth(align = Alignment.CenterHorizontally),) {
                    Text(text = stringResource(R.string.go_to_downloads),
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.SemiBold)
                }
            }
        }
        is Resource.Success -> {

            val pagerState = rememberPagerState { uiState.data.movies?.size ?: 0 }

            LaunchedEffect(pagerState) {
                while(true) {
                    delay(5_000L)
                    pagerState.animateScrollToPage(page = pagerState.currentPage.inc() % pagerState.pageCount, animationSpec = tween(2000))
                }
            }

            Box(modifier = modifier
                .fillMaxSize()
                .padding(bottom = bottomPadding.calculateBottomPadding())
                .pullRefresh(pullRefreshState)) {
                Column {
                    HorizontalPager(state = pagerState) { index ->

                        Box(modifier = modifier.height(350.dp)) {

                            val titleImage = uiState.data.movies?.get(index)

                            AsyncImage(
                                model = ImageRequest.Builder(context = LocalContext.current)
                                    .data(titleImage?.backdropPath?.toImageUrl ?: "")
                                    .crossfade(true)
                                    .build(),
                                placeholder = painterResource(id = R.drawable.ic_image_placeholder),
                                contentDescription = null,
                                modifier = modifier
                                    .fillMaxSize()
                                    .drawWithCache {
                                        val gradient = Brush.verticalGradient(
                                            colors = listOf(Color.Transparent, Color.Black),
                                            startY = size.height / 3,
                                            endY = size.height
                                        )
                                        onDrawWithContent {
                                            drawContent()
                                            drawRect(gradient, blendMode = BlendMode.Multiply)
                                        }
                                    },
                                contentScale = ContentScale.FillHeight
                            )


                            Column(
                                modifier = modifier
                                    .fillMaxSize()
                                    .wrapContentSize(align = Alignment.BottomStart)
                                    .padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Text(
                                    text = titleImage?.title ?: "",
                                    style = MaterialTheme.typography.titleLarge,
                                    color = Color.White,
                                    fontWeight = FontWeight.SemiBold,
                                    maxLines = 2,
                                    overflow = TextOverflow.Ellipsis
                                )
                                Text(
                                    text = uiState.data.titleGenre,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = Color.White
                                )

                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Button(onClick = {}) {
                                        Icon(
                                            imageVector = Icons.Rounded.PlayCircle,
                                            contentDescription = null
                                        )
                                        Text(text = stringResource(R.string.play))
                                    }
                                    OutlinedButton(onClick = goToMyListClick) {
                                        Icon(imageVector = Icons.Rounded.Add, contentDescription = null)
                                        Text(text = stringResource(R.string.my_list))
                                    }
                                }
                            }
                        }
                    }

                    Column(modifier = modifier
                        .verticalScroll(rememberScrollState())
                        .padding(vertical = 16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)) {
                        Row(modifier = modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
                            Text(
                                text = stringResource(R.string.now_playing_movies),
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.SemiBold
                            )

                            TextButton(onClick = { NowPlayingMoviesActivity.startActivity(context as Activity) }) {
                                Text(
                                    text = stringResource(R.string.see_all),
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }

                        LazyRow(contentPadding = PaddingValues(horizontal = 16.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            items(uiState.data.movies ?: emptyList()) {
                                MovieImageCard(imageUrl = it?.posterPath ?: "", rating = it?.voteAverage.toString() ?: "", movieId = it?.id)
                            }
                        }

                        Row(modifier = modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
                            Text(
                                text = stringResource(R.string.now_playing_series),
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.SemiBold
                            )

                            TextButton(onClick = { NowPlayingSeriesActivity.startActivity(context as Activity) }) {
                                Text(
                                    text = stringResource(id = R.string.see_all),
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }

                        LazyRow(contentPadding = PaddingValues(horizontal = 16.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            items(uiState.data.tvSeries ?: emptyList()) {
                                TvSeriesImageCard(imageUrl = it?.posterPath ?: "", rating = it?.voteAverage.toString() ?: "", tvSeriesId = it?.id)
                            }
                        }

                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MovieImageCard(modifier: Modifier = Modifier, imageUrl: String = "", rating: String = "", movieId: Int? = null) {

    val context = LocalContext.current

    Timber.tag("Card").d(movieId.toString())

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
                modifier = modifier.size(height = 200.dp, width = 150.dp),
            )

            Card(modifier = modifier
                .fillMaxSize()
                .wrapContentSize(align = Alignment.TopStart)
                .padding(8.dp), shape = RoundedCornerShape(30), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary)) {
                Text(text = rating.toDoubleOrNull()?.toOneDecimal ?: "", modifier = modifier.padding(horizontal = 10.dp, vertical = 8.dp), style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TvSeriesImageCard(modifier: Modifier = Modifier, imageUrl: String = "", rating: String = "", tvSeriesId: Int? = null) {

    val context = LocalContext.current

    Timber.tag("Card").d(tvSeriesId.toString())

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
                modifier = modifier.size(height = 200.dp, width = 150.dp),
            )

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
private fun HomeScreenLightThemePreview() {
    MoviesAppTheme(darkTheme = false) {
        HomeScreen()
    }
}

@Preview(showBackground = true, showSystemUi = true, uiMode = UI_MODE_NIGHT_YES)
@Composable
private fun HomeScreenDarkThemePreview() {
    MoviesAppTheme(darkTheme = true) {
        HomeScreen()
    }
}