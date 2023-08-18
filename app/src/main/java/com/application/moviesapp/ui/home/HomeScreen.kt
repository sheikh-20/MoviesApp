package com.application.moviesapp.ui.home

import android.app.Activity
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.PlayCircle
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.application.moviesapp.ui.theme.MoviesAppTheme
import com.application.moviesapp.R
import com.application.moviesapp.ui.utility.toImageUrl
import com.application.moviesapp.ui.viewmodel.MoviesWithNewReleaseUiState

@Composable
fun HomeScreen(modifier: Modifier = Modifier, uiState: MoviesWithNewReleaseUiState = MoviesWithNewReleaseUiState.Loading) {

    val context = LocalContext.current

    when (uiState) {
        is MoviesWithNewReleaseUiState.Loading -> {
            Column(modifier = modifier.fillMaxSize()) {
                CircularProgressIndicator(modifier = modifier
                    .fillMaxSize()
                    .wrapContentSize(align = Alignment.Center))
            }
        }
        is MoviesWithNewReleaseUiState.Failure -> {
            Column(modifier = modifier.fillMaxSize()) {
                Text(text = "Failure")
            }
        }
        is MoviesWithNewReleaseUiState.Success -> {

            val titleImage = uiState.moviesWithNewReleases.topRatedResponse.results?.first()

            Column(modifier = modifier.fillMaxSize()) {
                Box(modifier = modifier.height(300.dp)) {
                    AsyncImage(model = ImageRequest.Builder(context = LocalContext.current)
                        .data(titleImage?.backdropPath?.toImageUrl ?: "")
                        .crossfade(true)
                        .build(),
                        placeholder = painterResource(id = R.drawable.doctor_strange),
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
                        contentScale = ContentScale.FillHeight)

                    Column(modifier = modifier
                        .fillMaxSize()
                        .wrapContentSize(align = Alignment.BottomStart)
                        .padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text(text = titleImage?.title ?: "", style = MaterialTheme.typography.titleLarge)
                        Text(text = uiState.moviesWithNewReleases.titleGenre, style = MaterialTheme.typography.bodyMedium)

                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Button(onClick = {}) {
                                Icon(imageVector = Icons.Rounded.PlayCircle, contentDescription = null)
                                Text(text = "Play")
                            }
                            OutlinedButton(onClick = {}) {
                                Icon(imageVector = Icons.Rounded.Add, contentDescription = null)
                                Text(text = "My List")
                            }
                        }
                    }
                }

                Column(modifier = modifier
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    Row(modifier = modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
                        Text(
                            text = "Top 10 Movies This Week",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.SemiBold
                        )

                        TextButton(onClick = { TopMoviesActivity.startActivity(context as Activity) }) {
                            Text(
                                text = "See all",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }

                    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        items(uiState.moviesWithNewReleases.topRatedResponse.results?.take(10) ?: listOf()) {
                            MovieImageCard(imageUrl = it?.posterPath ?: "", rating = it?.voteAverage.toString() ?: "")
                        }
                    }

                    Row(modifier = modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
                        Text(
                            text = "New Releases",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.SemiBold
                        )

                        TextButton(onClick = { NewReleasesActivity.startActivity(context as Activity) }) {
                            Text(
                                text = "See all",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }

                    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {

                        items(uiState.moviesWithNewReleases.newReleasesResponse.results ?: listOf()) {
                            MovieImageCard(imageUrl = it?.posterPath ?: "", rating = it?.voteAverage.toString() ?: "")
                        }
                    }
                }
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
                placeholder = painterResource(id = R.drawable.ic_loading_placeholder),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = modifier.size(height = 200.dp, width = 150.dp),
            )

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