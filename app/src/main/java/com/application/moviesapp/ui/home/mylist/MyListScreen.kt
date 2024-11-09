package com.application.moviesapp.ui.home.mylist

import android.app.Activity
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.FileDownload
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
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
import androidx.compose.ui.zIndex
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.application.moviesapp.R
import com.application.moviesapp.data.common.Resource
import com.application.moviesapp.domain.model.MovieFavourite
import com.application.moviesapp.domain.model.TvSeriesFavourite
import com.application.moviesapp.ui.detail.DetailActivity
import com.application.moviesapp.ui.detail.IS_TYPE
import com.application.moviesapp.ui.theme.MoviesAppTheme
import com.application.moviesapp.ui.utility.MovieImageShimmerCard
import com.application.moviesapp.ui.utility.toImageUrl
import com.application.moviesapp.ui.utility.toOneDecimal
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class, ExperimentalMaterialApi::class,
    ExperimentalMaterialApi::class, ExperimentalPagerApi::class
)
@Composable
fun MyListScreen(modifier: Modifier = Modifier,
                 moviesFavouriteFlow: LazyPagingItems<MovieFavourite>,
                 tvSeriesFavouriteFlow: LazyPagingItems<TvSeriesFavourite>,
                 lazyGridState: LazyGridState = LazyGridState(),
                 lazyTvSeriesGridState: LazyGridState = LazyGridState(),
                 searchText: String = "",
                 bottomPadding: PaddingValues = PaddingValues()
) {


    val tabsList = listOf("Movies", "Tv Series")

    val pager = rememberPagerState()
    val coroutineScope = rememberCoroutineScope()

    var isRefreshing by remember { mutableStateOf(false) }
    val pullRefreshState = rememberPullRefreshState(
        refreshing = isRefreshing,
        onRefresh = {
            coroutineScope.launch {
                isRefreshing = !isRefreshing

                when (pager.currentPage) {
                    0 -> moviesFavouriteFlow.refresh()
                    1 -> tvSeriesFavouriteFlow.refresh()
                }

                delay(1_000L)
                isRefreshing = !isRefreshing
            }
        })

    LaunchedEffect(key1 = Unit) {
        moviesFavouriteFlow.refresh()
        tvSeriesFavouriteFlow.refresh()
    }

    Box(modifier = modifier
        .fillMaxSize()
        .padding(
            top = bottomPadding.calculateTopPadding(),
            bottom = bottomPadding.calculateBottomPadding()
        )
        .pullRefresh(pullRefreshState)) {

        Column(modifier = modifier.fillMaxSize(), verticalArrangement = Arrangement.spacedBy(10.dp)) {

            TabRow(
                modifier = modifier.fillMaxWidth(),
                selectedTabIndex = pager.currentPage,
                indicator = { tabPositions ->
                    Column(
                        modifier = Modifier
                            .tabIndicatorOffset(tabPositions[pager.currentPage])
                            .fillMaxSize()
                            .padding(horizontal = 16.dp, vertical = 4.dp)
                            .background(
                                MaterialTheme.colorScheme.primaryContainer,
                                FilterChipDefaults.shape,
                            )
                    ) {}
                },
                divider = {  }
            ) {
                tabsList.forEachIndexed { tabIndex, tabName ->
                    FilterChip(
                        modifier = modifier
                            .weight(1f)
                            .zIndex(2f),
                        selected = false,
                        border = null,
                        onClick = {
                            coroutineScope.launch {
                                pager.animateScrollToPage(page = tabIndex)
                            }
                        },
                        label = {
                            Text(text = tabName, textAlign = TextAlign.Center, modifier = modifier.weight(1f), color = MaterialTheme.colorScheme.onPrimary)
                        }
                    )
                }
            }


            HorizontalPager(count = tabsList.size, state = pager, modifier = modifier.fillMaxWidth()) { index ->
                when (index) {
                    0 -> {
                        Column(modifier = modifier.fillMaxSize()) {
                            when (moviesFavouriteFlow.loadState.refresh){
                                is LoadState.Error -> Column(
                                    verticalArrangement = Arrangement.spacedBy(16.dp),
                                    modifier = modifier
                                        .fillMaxSize()
                                        .wrapContentSize(align = Alignment.Center)
                                        .padding(32.dp)
                                ) {
                                    Image(
                                        painter = painterResource(id = R.drawable.ic_empty_list),
                                        contentDescription = null,
                                        modifier = modifier
                                            .fillMaxWidth()
                                            .wrapContentWidth(align = Alignment.CenterHorizontally)
                                            .size(200.dp),
                                        contentScale = ContentScale.Crop,
                                    )

                                    Text(
                                        text = stringResource(R.string.you_list_is_empty),
                                        style = MaterialTheme.typography.titleLarge,
                                        modifier = modifier
                                            .fillMaxWidth()
                                            .wrapContentWidth(align = Alignment.CenterHorizontally)
                                    )

                                    Text(
                                        text = stringResource(R.string.it_seems_you_haven_t_listed_any_movies_or_series),
                                        textAlign = TextAlign.Center,
                                        style = MaterialTheme.typography.bodyLarge,
                                        modifier = modifier
                                            .fillMaxWidth()
                                            .wrapContentWidth(align = Alignment.CenterHorizontally)
                                    )
                                }
                                is LoadState.Loading -> {
                                    LazyVerticalGrid(columns = GridCells.Fixed(2),
                                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                                        verticalArrangement = Arrangement.spacedBy(8.dp),
                                        state = lazyGridState,
                                        contentPadding = PaddingValues(top = 16.dp, start = 16.dp, end = 16.dp)) {

                                        items(10) {
                                            MovieImageShimmerCard()
                                        }
                                    }
                                }
                                is LoadState.NotLoading ->  {
                                    Column(modifier = modifier
                                        .fillMaxSize()
                                        .wrapContentSize(align = Alignment.TopStart)) {

                                        if (moviesFavouriteFlow.itemCount == 0) {
                                            Column(
                                                verticalArrangement = Arrangement.spacedBy(16.dp),
                                                modifier = modifier
                                                    .fillMaxSize()
                                                    .wrapContentSize(align = Alignment.Center)
                                                    .padding(32.dp)
                                            ) {
                                                Image(
                                                    painter = painterResource(id = R.drawable.ic_empty_list),
                                                    contentDescription = null,
                                                    modifier = modifier
                                                        .fillMaxWidth()
                                                        .wrapContentWidth(align = Alignment.CenterHorizontally)
                                                        .size(200.dp),
                                                    contentScale = ContentScale.Crop,
                                                )

                                                Text(
                                                    text = stringResource(R.string.you_list_is_empty),
                                                    style = MaterialTheme.typography.titleLarge,
                                                    modifier = modifier
                                                        .fillMaxWidth()
                                                        .wrapContentWidth(align = Alignment.CenterHorizontally)
                                                )

                                                Text(
                                                    text = stringResource(R.string.it_seems_you_haven_t_listed_any_movies_or_series),
                                                    textAlign = TextAlign.Center,
                                                    style = MaterialTheme.typography.bodyLarge,
                                                    modifier = modifier
                                                        .fillMaxWidth()
                                                        .wrapContentWidth(align = Alignment.CenterHorizontally)
                                                )
                                            }
                                        }
                                        else {
                                            LazyVerticalGrid(
                                                columns = GridCells.Fixed(2),
                                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                                verticalArrangement = Arrangement.spacedBy(8.dp),
                                                state = lazyGridState,
                                                contentPadding = PaddingValues(start = 16.dp, end = 16.dp)) {

                                                items(moviesFavouriteFlow.itemCount) { index ->
                                                    MovieImageCard(imageUrl = moviesFavouriteFlow[index]?.posterPath ?: "", rating = moviesFavouriteFlow[index]?.voteAverage.toString(), movieId = moviesFavouriteFlow[index]?.id ?: 0)
                                                }
                                            }
                                        }
                                    }
                                }
                            }

                            when (moviesFavouriteFlow.loadState.append) {
                                is LoadState.Loading -> {
                                    CircularProgressIndicator(modifier = modifier
                                        .fillMaxWidth()
                                        .wrapContentWidth(align = Alignment.CenterHorizontally)
                                        .padding(16.dp))
                                }
                                is LoadState.NotLoading -> {   }
                                is LoadState.Error -> {
                                    Text(text = if (moviesFavouriteFlow.loadState.append.endOfPaginationReached) "You have reached the end" else "",
                                        style = MaterialTheme.typography.displayMedium,
                                        color = MaterialTheme.colorScheme.primary,
                                        fontWeight = FontWeight.SemiBold,
                                        modifier = modifier
                                            .fillMaxWidth()
                                            .wrapContentWidth(align = Alignment.CenterHorizontally)
                                            .padding(16.dp),
                                        textAlign = TextAlign.Center)
                                }
                            }
                        }
                    }
                    1 -> {
                        Column(modifier = modifier.fillMaxSize()) {
                            when (tvSeriesFavouriteFlow.loadState.refresh){
                                is LoadState.Error -> Column(
                                    verticalArrangement = Arrangement.spacedBy(16.dp),
                                    modifier = modifier
                                        .fillMaxSize()
                                        .wrapContentSize(align = Alignment.Center)
                                        .padding(32.dp)
                                ) {
                                    Image(
                                        painter = painterResource(id = R.drawable.ic_empty_list),
                                        contentDescription = null,
                                        modifier = modifier
                                            .fillMaxWidth()
                                            .wrapContentWidth(align = Alignment.CenterHorizontally)
                                            .size(200.dp),
                                        contentScale = ContentScale.Crop,
                                    )

                                    Text(
                                        text = stringResource(R.string.you_list_is_empty),
                                        style = MaterialTheme.typography.titleLarge,
                                        modifier = modifier
                                            .fillMaxWidth()
                                            .wrapContentWidth(align = Alignment.CenterHorizontally)
                                    )

                                    Text(
                                        text = stringResource(R.string.it_seems_you_haven_t_listed_any_movies_or_series),
                                        textAlign = TextAlign.Center,
                                        style = MaterialTheme.typography.bodyLarge,
                                        modifier = modifier
                                            .fillMaxWidth()
                                            .wrapContentWidth(align = Alignment.CenterHorizontally)
                                    )
                                }
                                is LoadState.Loading -> {
                                    LazyVerticalGrid(columns = GridCells.Fixed(2),
                                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                                        verticalArrangement = Arrangement.spacedBy(8.dp),
                                        state = lazyGridState,
                                        contentPadding = PaddingValues(top = 16.dp, start = 16.dp, end = 16.dp)) {

                                        items(10) {
                                            MovieImageShimmerCard()
                                        }
                                    }
                                }
                                is LoadState.NotLoading ->  {
                                    Column(modifier = modifier
                                        .fillMaxSize()
                                        .wrapContentSize(align = Alignment.TopStart)) {

                                        if (tvSeriesFavouriteFlow.itemCount == 0) {
                                            Column(
                                                verticalArrangement = Arrangement.spacedBy(16.dp),
                                                modifier = modifier
                                                    .fillMaxSize()
                                                    .wrapContentSize(align = Alignment.Center)
                                                    .padding(32.dp)
                                            ) {
                                                Image(
                                                    painter = painterResource(id = R.drawable.ic_empty_list),
                                                    contentDescription = null,
                                                    modifier = modifier
                                                        .fillMaxWidth()
                                                        .wrapContentWidth(align = Alignment.CenterHorizontally)
                                                        .size(200.dp),
                                                    contentScale = ContentScale.Crop,
                                                )

                                                Text(
                                                    text = stringResource(R.string.you_list_is_empty),
                                                    style = MaterialTheme.typography.titleLarge,
                                                    modifier = modifier
                                                        .fillMaxWidth()
                                                        .wrapContentWidth(align = Alignment.CenterHorizontally)
                                                )

                                                Text(
                                                    text = stringResource(R.string.it_seems_you_haven_t_listed_any_movies_or_series),
                                                    textAlign = TextAlign.Center,
                                                    style = MaterialTheme.typography.bodyLarge,
                                                    modifier = modifier
                                                        .fillMaxWidth()
                                                        .wrapContentWidth(align = Alignment.CenterHorizontally)
                                                )
                                            }
                                        }
                                        else {
                                            LazyVerticalGrid(
                                                columns = GridCells.Fixed(2),
                                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                                verticalArrangement = Arrangement.spacedBy(8.dp),
                                                state = lazyTvSeriesGridState,
                                                contentPadding = PaddingValues(start = 16.dp, end = 16.dp)) {

                                                items(tvSeriesFavouriteFlow.itemCount) { index ->
                                                    MovieImageCard(imageUrl = tvSeriesFavouriteFlow[index]?.posterPath ?: "", rating = tvSeriesFavouriteFlow[index]?.voteAverage.toString(), movieId = tvSeriesFavouriteFlow[index]?.id ?: 0, isType = IS_TYPE.TvSeries)
                                                }
                                            }
                                        }
                                    }
                                }
                            }

                            when (tvSeriesFavouriteFlow.loadState.append) {
                                is LoadState.Loading -> {
                                    CircularProgressIndicator(modifier = modifier
                                        .fillMaxWidth()
                                        .wrapContentWidth(align = Alignment.CenterHorizontally)
                                        .padding(16.dp))
                                }
                                is LoadState.NotLoading -> {   }
                                is LoadState.Error -> {
                                    Text(text = if (tvSeriesFavouriteFlow.loadState.append.endOfPaginationReached) "You have reached the end" else "",
                                        style = MaterialTheme.typography.displayMedium,
                                        color = MaterialTheme.colorScheme.primary,
                                        fontWeight = FontWeight.SemiBold,
                                        modifier = modifier
                                            .fillMaxWidth()
                                            .wrapContentWidth(align = Alignment.CenterHorizontally)
                                            .padding(16.dp),
                                        textAlign = TextAlign.Center)
                                }
                            }
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
private fun MovieImageCard(modifier: Modifier = Modifier, imageUrl: String = "", rating: String = "", movieId: Int = 0, isType: IS_TYPE = IS_TYPE.Movie) {

    val context = LocalContext.current

    Card(shape = RoundedCornerShape(10), onClick = { DetailActivity.startActivity(context as Activity, isType, movieId) }) {
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


//@Preview(showBackground = true, showSystemUi = true)
//@Composable
//private fun MyListScreenLightThemePreview() {
//    MoviesAppTheme(darkTheme = false) {
//        MyListScreen()
//    }
//}
//
//@Preview(showBackground = true, showSystemUi = true, uiMode = UI_MODE_NIGHT_YES)
//@Composable
//private fun MyListScreenDarkThemePreview() {
//    MoviesAppTheme(darkTheme = true) {
//        MyListScreen()
//    }
//}