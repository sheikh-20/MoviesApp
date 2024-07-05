package com.application.moviesapp.ui.onboarding.signup

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedSuggestionChip
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.application.moviesapp.R
import com.application.moviesapp.data.api.response.MovieGenreResponse
import com.application.moviesapp.data.common.Resource
import com.application.moviesapp.domain.model.MovieGenre
import com.application.moviesapp.domain.model.MoviesDetail
import com.application.moviesapp.ui.theme.MoviesAppTheme
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class, ExperimentalPagerApi::class,
    ExperimentalMaterialApi::class
)
@Composable
fun ChooseYourInterestScreen(modifier: Modifier = Modifier,
                             uiState: Resource<MovieGenre> = Resource.Loading,
                             onContinueClick: () -> Unit = {},
                             onGenreClick: (MoviesDetail.Genre) -> Unit = { }) {

    val tabsList = listOf("Movies", "Tv Series")

    val pager = rememberPagerState()
    val coroutineScope = rememberCoroutineScope()

    Box(modifier = modifier
        .fillMaxSize()) {

        Column(
            modifier = modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {

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
                divider = { }
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
                            Text(
                                text = tabName,
                                textAlign = TextAlign.Center,
                                modifier = modifier.weight(1f),
                                color = MaterialTheme.colorScheme.onPrimary
                            )
                        }
                    )
                }
            }

            HorizontalPager(
                count = tabsList.size,
                state = pager,
                modifier = modifier.fillMaxWidth()
            ) { index ->
                when (index) {
                    0 -> {
                        Column(
                            modifier = modifier
                                .fillMaxSize()
                                .padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {

                            when (uiState) {
                                is Resource.Loading -> {
                                    CircularProgressIndicator(modifier = modifier
                                        .fillMaxSize()
                                        .wrapContentSize(align = Alignment.Center))
                                }
                                is Resource.Failure -> {
                                    Text(text = "404",
                                        style = MaterialTheme.typography.displayLarge,
                                        modifier = modifier
                                            .fillMaxSize()
                                            .wrapContentSize(align = Alignment.Center))
                                }
                                is Resource.Success -> {
                                    Text(
                                        text = stringResource(R.string.choose_your_interests_description),
                                        style = MaterialTheme.typography.bodyLarge
                                    )

                                    FlowRow(
                                        modifier = modifier.weight(1f),
                                        verticalArrangement = Arrangement.spacedBy(16.dp),
                                        horizontalArrangement = Arrangement.spacedBy(8.dp)) {

                                        uiState.data.genres?.forEachIndexed { index, genre ->
                                            ChipContent(onGenreClick = onGenreClick, genre = genre)
                                        }
                                    }
                                }
                            }
                        }
                    }
                    1 -> {  }
                }
            }
        }

        Row(modifier = modifier.fillMaxSize().wrapContentSize(align = Alignment.BottomCenter).padding(horizontal = 16.dp, vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            OutlinedButton(onClick = { /*TODO*/ }, modifier = modifier
                .shadow(
                    elevation = 4.dp,
                    ambientColor = MaterialTheme.colorScheme.outlineVariant,
                    spotColor = MaterialTheme.colorScheme.outlineVariant,
                    shape = RoundedCornerShape(50)
                )
                .weight(1f)) {
                Text(text = stringResource(id = R.string.skip),  modifier = modifier.padding(4.dp))
            }
            Button(onClick = onContinueClick, modifier = modifier
                .shadow(
                    elevation = 4.dp,
                    ambientColor = MaterialTheme.colorScheme.outlineVariant,
                    spotColor = MaterialTheme.colorScheme.outlineVariant,
                    shape = RoundedCornerShape(50)
                )
                .weight(1f)) {
                Text(text = stringResource(id = R.string.continue_text), modifier = modifier.padding(4.dp))
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ChipContent(modifier: Modifier = Modifier, onGenreClick: (MoviesDetail.Genre) -> Unit = { _ ->  }, genre: MovieGenre.Genre? = null) {

    var selected by remember { mutableStateOf(false) }

    FilterChip(
        modifier = modifier.heightIn(min = 40.dp),
        onClick = {
            selected = !selected
            onGenreClick(MoviesDetail.Genre(genre?.id, genre?.name))
        },
        label = { Text(text = genre?.name ?: "", textAlign = TextAlign.Center) },
        selected = selected,
        shape = RoundedCornerShape(50)
    )
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