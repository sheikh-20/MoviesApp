package com.application.moviesapp.ui.onboarding.signup

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedSuggestionChip
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.application.moviesapp.R
import com.application.moviesapp.data.api.response.MovieGenreResponse
import com.application.moviesapp.domain.model.MoviesDetail
import com.application.moviesapp.ui.theme.MoviesAppTheme
import com.application.moviesapp.ui.viewmodel.MovieGenreUiState
import kotlinx.coroutines.flow.MutableStateFlow

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun ChooseYourInterestScreen(modifier: Modifier = Modifier,
                             uiState: MovieGenreUiState = MovieGenreUiState.Loading,
                             onContinueClick: () -> Unit = {},
                             onGenreClick: (MoviesDetail.Genre) -> Unit = { }) {

    var selectedGenre by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        when (uiState) {
            is MovieGenreUiState.Loading -> {
                CircularProgressIndicator(modifier = modifier
                    .fillMaxSize()
                    .wrapContentSize(align = Alignment.Center))
            }
            is MovieGenreUiState.Failure -> {
                Text(text = "404",
                    style = MaterialTheme.typography.displayLarge,
                    modifier = modifier
                        .fillMaxSize()
                        .wrapContentSize(align = Alignment.Center))
            }
            is MovieGenreUiState.Success -> {
                Text(
                    text = stringResource(R.string.choose_your_interests_description),
                    style = MaterialTheme.typography.bodyLarge
                )

                FlowRow(
                    modifier = modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    uiState.genreResponse.genres?.forEachIndexed { index, genre ->
                        FilterChip(
                            modifier = modifier.heightIn(min = 40.dp),
                            onClick = {
                                onGenreClick(MoviesDetail.Genre(genre?.id, genre?.name))
                                      },
                            label = { Text(text = genre?.name ?: "", textAlign = TextAlign.Center) },
                            selected = selectedGenre,
                            shape = RoundedCornerShape(50)
                        )
                    }
                }

                Row(modifier = modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    OutlinedButton(onClick = { /*TODO*/ }, modifier = modifier.weight(1f)) {
                        Text(text = "Skip")
                    }
                    Button(onClick = onContinueClick, modifier = modifier.weight(1f)) {
                        Text(text = "Continue")
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun ChooseYourInterestScreenLightThemePreview() {
    MoviesAppTheme(darkTheme = false) {
        ChooseYourInterestScreen()
    }
}

@Preview
@Composable
private fun ChooseYourInterestScreenDarkThemePreview() {
    MoviesAppTheme(darkTheme = true) {
        ChooseYourInterestScreen()
    }
}