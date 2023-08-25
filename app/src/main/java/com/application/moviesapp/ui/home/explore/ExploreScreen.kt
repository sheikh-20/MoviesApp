package com.application.moviesapp.ui.home.explore

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Image
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.paging.PagingConfig
import androidx.paging.compose.LazyPagingItems
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.application.moviesapp.R
import com.application.moviesapp.domain.Movies
import com.application.moviesapp.ui.theme.MoviesAppTheme
import com.application.moviesapp.ui.utility.toImageUrl
import com.application.moviesapp.ui.viewmodel.ExploreUiState

@Composable
fun ExploreScreen(modifier: Modifier = Modifier, uiState: ExploreUiState = ExploreUiState.Loading, moviesFlow: LazyPagingItems<Movies>) {

//    when (uiState) {
//        is ExploreUiState.Loading -> {
//            Column(modifier = modifier.fillMaxSize()) {
//                CircularProgressIndicator(modifier = modifier
//                    .fillMaxSize()
//                    .wrapContentSize(align = Alignment.Center))
//            }
//        }
//        is ExploreUiState.Failure -> {
//            Image(painter = painterResource(id = R.drawable.ic_error),
//                contentDescription = null,
//                modifier = modifier.fillMaxSize().wrapContentSize(align = Alignment.Center).padding(16.dp))
//        }
//        is ExploreUiState.Success -> {
//            Column(modifier = modifier
//                .fillMaxSize()
//                .padding(16.dp)) {
//
//                LazyVerticalGrid(columns = GridCells.Fixed(2),
//                    horizontalArrangement = Arrangement.spacedBy(8.dp),
//                    verticalArrangement = Arrangement.spacedBy(8.dp)) {
//                    items(uiState.response.results ?: listOf()) {
//                        MovieImageCard(imageUrl = it?.posterPath ?: "", rating = it?.voteAverage.toString() ?: "")
//                    }
//                }
//            }
//        }
//    }

    Column(modifier = modifier
        .fillMaxSize()
        .padding(16.dp)) {

        LazyVerticalGrid(columns = GridCells.Fixed(2),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)) {

            items(moviesFlow.itemCount) { index ->
                MovieImageCard(imageUrl = moviesFlow[index]?.posterPath ?: "", rating = moviesFlow[index]?.voteAverage.toString() ?: "")
            }
        }
    }
}

@Composable
private fun MovieImageCard(modifier: Modifier = Modifier, imageUrl: String = "", rating: String = "") {
    Card(shape = RoundedCornerShape(10)) {
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
                Text(text = rating, modifier = modifier.padding(horizontal = 10.dp, vertical = 8.dp), style = MaterialTheme.typography.bodySmall)
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