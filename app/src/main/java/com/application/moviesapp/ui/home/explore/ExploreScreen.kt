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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.application.moviesapp.R
import com.application.moviesapp.domain.model.MovieSearch
import com.application.moviesapp.domain.model.MoviesPopular
import com.application.moviesapp.ui.detail.DetailActivity
import com.application.moviesapp.ui.detail.IS_TYPE
import com.application.moviesapp.ui.theme.MoviesAppTheme
import com.application.moviesapp.ui.utility.toImageUrl
import com.application.moviesapp.ui.utility.toOneDecimal
import com.application.moviesapp.ui.viewmodel.ExploreUiState

@Composable
fun ExploreScreen(modifier: Modifier = Modifier,
                  uiState: ExploreUiState = ExploreUiState.Loading,
                  moviesPopularFlow: LazyPagingItems<MoviesPopular>,
                  movieSearchFlow: LazyPagingItems<MovieSearch>,
                  searchClicked: Boolean = false,
                  lazyGridState: LazyGridState = LazyGridState(),
                  bottomPadding: PaddingValues = PaddingValues()
) {

//    when (moviesPopularFlow.loadState.refresh) {
//        is LoadState.Loading -> {
//            Column(modifier = modifier.fillMaxSize()) {
//                CircularProgressIndicator(modifier = modifier
//                    .fillMaxSize()
//                    .wrapContentSize(align = Alignment.Center))
//            }
//        }
//        is LoadState.Error -> {
//            Column(modifier = modifier
//                .fillMaxSize()
//                .wrapContentSize(align = Alignment.Center),
//                verticalArrangement = Arrangement.spacedBy(8.dp)) {
//
//                Text(text = "Not found",
//                    style = MaterialTheme.typography.displayMedium,
//                    color = MaterialTheme.colorScheme.primary,
//                    fontWeight = FontWeight.SemiBold,
//                    modifier = modifier.fillMaxWidth(),
//                    textAlign = TextAlign.Center)
//
//                Text(text = "Check you internet connection",
//                    style = MaterialTheme.typography.bodyLarge,
//                    modifier = modifier.fillMaxWidth(),
//                    textAlign = TextAlign.Center)
//            }
//        }
//        else -> {

            Column(modifier = modifier
                .fillMaxSize()
                .padding(top = bottomPadding.calculateTopPadding(), bottom = bottomPadding.calculateBottomPadding())) {

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

                        items(count = moviesPopularFlow.itemCount) { index ->
                            MovieImageCard(
                                imageUrl = moviesPopularFlow[index]?.posterPath ?: "",
                                rating = moviesPopularFlow[index]?.voteAverage.toString() ?: "",
                                movieId = moviesPopularFlow[index]?.id
                            )
                        }
                    }
                }
//            }
//        }
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