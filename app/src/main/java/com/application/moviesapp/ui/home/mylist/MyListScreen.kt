package com.application.moviesapp.ui.home.mylist

import android.app.Activity
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.Image
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.FileDownload
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.paging.compose.LazyPagingItems
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.application.moviesapp.R
import com.application.moviesapp.data.common.Resource
import com.application.moviesapp.domain.model.MovieFavourite
import com.application.moviesapp.ui.detail.DetailActivity
import com.application.moviesapp.ui.detail.IS_TYPE
import com.application.moviesapp.ui.theme.MoviesAppTheme
import com.application.moviesapp.ui.utility.toImageUrl
import com.application.moviesapp.ui.utility.toOneDecimal

@Composable
fun MyListScreen(modifier: Modifier = Modifier,
                 moviesFavouriteFlow: LazyPagingItems<MovieFavourite>? = null,
                 lazyGridState: LazyGridState = LazyGridState(),
                 bottomPadding: PaddingValues = PaddingValues()
) {

//    when (uiState) {
//            is Resource.Loading -> {
//                Column(modifier = modifier.fillMaxSize()) {
//                    CircularProgressIndicator(modifier = modifier
//                        .fillMaxSize()
//                        .wrapContentSize(align = Alignment.Center))
//                }
//            }
//
//            is Resource.Failure -> {
//                Column(modifier = modifier
//                    .fillMaxSize()
//                    .wrapContentSize(align = Alignment.Center),
//                    verticalArrangement = Arrangement.spacedBy(8.dp)) {
//
//                    Text(text = "Not found",
//                        style = MaterialTheme.typography.displayMedium,
//                        color = MaterialTheme.colorScheme.primary,
//                        fontWeight = FontWeight.SemiBold,
//                        modifier = modifier.fillMaxWidth(),
//                        textAlign = TextAlign.Center)
//
//                    Text(text = "Check you internet connection",
//                        style = MaterialTheme.typography.bodyLarge,
//                        modifier = modifier.fillMaxWidth(),
//                        textAlign = TextAlign.Center)
//                }
//            }
//
//            is Resource.Success -> {

//                if (uiState.data.isNullOrEmpty()) {
//                    Column(verticalArrangement = Arrangement.spacedBy(16.dp),
//                        modifier = modifier
//                            .fillMaxSize()
//                            .wrapContentSize(align = Alignment.Center)
//                            .padding(32.dp)) {
//                        Image(
//                            painter = painterResource(id = R.drawable.ic_empty_list),
//                            contentDescription = null,
//                            modifier = modifier
//                                .fillMaxWidth()
//                                .wrapContentWidth(align = Alignment.CenterHorizontally)
//                                .size(200.dp),
//                            contentScale = ContentScale.Crop,
//                        )
//
//                        Text(text = "You List is Empty",
//                            style = MaterialTheme.typography.titleLarge,
//                            modifier = modifier
//                                .fillMaxWidth()
//                                .wrapContentWidth(align = Alignment.CenterHorizontally))
//
//                        Text(text = "It seems you haven't downloaded any movies or series",
//                            textAlign = TextAlign.Center,
//                            style = MaterialTheme.typography.bodyLarge,
//                            modifier = modifier
//                                .fillMaxWidth()
//                                .wrapContentWidth(align = Alignment.CenterHorizontally))
//                    }
//                } else {
                    Column(modifier = modifier
                        .fillMaxSize()
                        .padding(
                            top = bottomPadding.calculateTopPadding(),
                            bottom = bottomPadding.calculateBottomPadding()
                        )) {

                        LazyVerticalGrid(
                            columns = GridCells.Fixed(2),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                            state = lazyGridState,
                            contentPadding = PaddingValues(start = 16.dp, end = 16.dp)) {

                            items(moviesFavouriteFlow?.itemCount ?: return@LazyVerticalGrid) { index ->
                                MovieImageCard(imageUrl = moviesFavouriteFlow[index]?.posterPath ?: "", rating = moviesFavouriteFlow[index]?.voteAverage.toString(), movieId = moviesFavouriteFlow[index]?.id ?: 0)
                            }
                        }
                    }
//                }
//            }
//        }
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
private fun MyListScreenLightThemePreview() {
    MoviesAppTheme(darkTheme = false) {
        MyListScreen()
    }
}

@Preview(showBackground = true, showSystemUi = true, uiMode = UI_MODE_NIGHT_YES)
@Composable
private fun MyListScreenDarkThemePreview() {
    MoviesAppTheme(darkTheme = true) {
        MyListScreen()
    }
}